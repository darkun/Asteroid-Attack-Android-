package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by Jag on 06.10.2016.
 */
public class Missile extends GameObject implements Fly {
    final int WIDTH = 2; // for accuracy calc
    final int HEIGHT = 30; // for accuracy calc
    final int DY = 30;
    final int SPEED = 16; // speed of missile
    final int DAMAGE = 35; // what damage it do to asteroid / enemy
    int bonus = 15; // score bonus for destroying asteroid
    int x, y, flyTime, lastLunch;
    boolean exists;

    public Missile(int x, int y) {
        if (!exists) {
            this.exists = true;
            this.x = x + (AsteroidAttack.playership.getWidth() - WIDTH) / 2;
            this.y = y - HEIGHT;
            this.flyTime = 0;
        }
    }

    public void start(int x, int y) {
        if (!exists) {
            this.exists = true;
            this.x = x + (AsteroidAttack.playership.getWidth() - WIDTH) / 2;
            this.y = y - HEIGHT;
            this.flyTime = 0;
        }
    }

    public boolean checkTarget(int x, int y) {
        for (Asteroid asteroid : AsteroidAttack.asteroids) {
            // here we check, if our missile got to radius of asteroid
            if((asteroid.isEnable()) && (Math.sqrt(Math.pow((double)asteroid.getX()-(double)x, 2) + Math.pow((double)asteroid.getY()-(double)y, 2)) <= asteroid.getRadius())) {
                asteroid.getDamage(DAMAGE);
                return true;
            }
        }
        return false;
    }

    public int getLastLunch() { return lastLunch; }

    @Override
    public boolean isEnable() { return exists; }

    @Override
    public void fly() {
        if (exists) {
            if(flyTime > SPEED) // checking, if our missile not too hurry )
            {
                y -= DY;
                this.exists = (y + DY) > 0;
                this.flyTime = 0;
            }
            else this.flyTime++;

            if(checkTarget(this.x, this.y)) {
                this.exists = false; // our missile is exploded
                AsteroidAttack.m_explosions.add(new MissileBoom(x, y));
                //countScore += bonus;
                AsteroidAttack.setGameScore(AsteroidAttack.getGameScore() + bonus);
            }
        }
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(AsteroidAttack.missile, x-7, y, null);
    }
}

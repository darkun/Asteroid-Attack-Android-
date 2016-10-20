package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;
import java.util.ArrayList;

/*
 * Missile class.
 * fly() - calculate right params for drawing object
 * checkTarget() - if missile target any object
 *
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
*/
public class Missile extends GameObject implements Fly, CheckEnable {
    static final int WIDTH = 2; // for accuracy calc
    static final int HEIGHT = 30; // for accuracy calc
    static final int DY = 30;
    static final int SPEED = 16; // speed of missile
    static final int DAMAGE = 35; // what damage it do to asteroid / enemy
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

    /*public void start(int x, int y) {
        if (!exists) {
            this.exists = true;
            this.x = x + (AsteroidAttack.playership.getWidth() - WIDTH) / 2;
            this.y = y - HEIGHT;
            this.flyTime = 0;
        }
    }*/

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

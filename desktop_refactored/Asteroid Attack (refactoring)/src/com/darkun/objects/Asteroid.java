package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/**
 * Created by Jag on 06.10.2016.
 */
public class Asteroid extends GameObject implements Fly {
    final int SPRITE_CELL = 72; // size of sprite cell
    final int RADIUS = 35; // for interaction calc
    final int DY = 30;
    final int DAMAGE = 11; // what damage it do on crash
    final int ANIM_FRAMES = 18; // how many frames do we have for animation

    int speed = 7; // speed of asteroid flying
    int anim_speed = 12; // speed of animation
    int animTime, animPhase; // current position of asteroid, what course it is flying and phase of animation
    double traectory; // coeff for X dislocation
    boolean exists; // is it is?
    int live; // current level of durability (how much damage it needed for destroying)

    public Asteroid (int x, int y, double direction, int speed)
    {
        this.x = x; // starting position
        this.y = y;
        this.traectory = direction;
        this.flyTime = 0;
        this.animTime = 0;
        this.speed = speed;
        this.anim_speed = speed / 2;
        this.live = 20;
        this.exists = true;
    }

    @Override
    public void fly() {
        if (this.exists) {
            if(this.getFlyTime() > this.speed) {// checking, if our asteroid not too hurry )
                this.y += DY;
                this.x += this.traectory;
                if((y + DY) < FIELD_HEIGHT + RADIUS) this.exists = true;
                else {
                    this.exists = false;
                    AsteroidAttack.setMissedScore(AsteroidAttack.getMissedScore() + 1);
                }
                this.flyTime = 0;
                //System.out.println(exists);
                //System.out.println(x + "," + y);
            }
            else this.flyTime++;

            if(this.animTime >= this.anim_speed) {
                if(this.animPhase < ANIM_FRAMES) this.animPhase++;
                else this.animPhase = 0;
                animTime = 0;
            }
            else this.animTime++;

            checkCrash(x, y);
        }
    }

    public void getDamage(int damage) {
        this.live -= damage;
        if(this.live <=0 ) this.exists = false; // if asteroid is destroed, let's kill it
    }
    public void doDamage() {
        this.exists = false; // if asteroid is destroed, let's kill it
        AsteroidAttack.playership.getDamage(DAMAGE);
    }
    public boolean checkCrash(int x, int y) { // check, if asteroid touches playership (are radiuses of asteroid and spaceship in touch)
        if((this.isEnable()) && (Math.sqrt(Math.pow((double)AsteroidAttack.playership.getX() - (double)this.x, 2) + Math.pow((double)AsteroidAttack.playership.getY() - (double)this.y, 2)) <= this.getRadius() + AsteroidAttack.playership.getRadius())) {
            this.doDamage();
        }
        return false;
    }
    public int getRadius() { return RADIUS; }

    @Override
    public boolean isEnable() { return exists; }

    @Override
    public void paint(Graphics g) {
        g.drawImage(AsteroidAttack.ast, x-RADIUS, y-RADIUS, x-RADIUS+SPRITE_CELL, y-RADIUS+SPRITE_CELL, this.animPhase*SPRITE_CELL, 0, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL, null);
    }
}

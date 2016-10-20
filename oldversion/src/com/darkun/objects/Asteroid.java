package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/*
 * Asteroid class.
 * fly() - calculate right params for drawing object
 * move() - moving playership in selected direction
 * getDamage() - possibly asteroid will not destroy after getting damage, but we need to calc it's durability after
 * doDamage() - after crash asteroid do damage
 * checkCrash() - if asteroid target some object
 * getXXX() - get XXX parametr of playership
 *
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
*/
public class Asteroid extends GameObject implements Fly, CheckEnable {
    final int SPRITE_CELL = 72; // size of sprite cell
    final int DY = 30;
    final int DAMAGE = 11; // what damage it do on crash

    int radius = 35; // for interaction calc
    int anim_frames = 18; // how many frames do we have for animation

    int speed = 7; // speed of asteroid flying
    int anim_speed = 12; // speed of animation
    int animTime, animPhase; // current position of asteroid, what course it is flying and phase of animation
    double traectory; // coeff for X dislocation
    boolean exists; // is it is?
    int live; // current level of durability (how much damage it needed for destroying)
    int type; // what type of asteroid is

    public Asteroid (int x, int y, double direction, int speed, int type)
    {
        this.x = x; // starting position
        this.y = y;
        this.traectory = direction;
        this.flyTime = 0;
        this.animTime = 0;
        this.speed = speed;
        this.anim_speed = speed / 2;
        this.exists = true;
        this.type = type;
        if(type == 1) {
            anim_frames = 60;
            radius = 23;
            this.live = 11;
        }
        else {
            anim_frames = 18;
            this.live = 20;
        }
    }

    @Override
    public void fly() {
        if (this.exists) {
            if(this.getFlyTime() > this.speed) {// checking, if our asteroid not too hurry )
                this.y += DY;
                this.x += this.traectory;
                if((y + DY) < AsteroidAttack.FIELD_HEIGHT + radius) this.exists = true;
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
                if(this.animPhase < anim_frames) this.animPhase++;
                else this.animPhase = 0;
                animTime = 0;
            }
            else this.animTime++;

            checkCrash(x, y);
        }
    }

    public void getDamage(int damage) {
        this.live -= damage;
        if(this.live <=0 ) {
            this.exists = false; // if asteroid is destroed, let's kill it
            if(AsteroidAttack.isTrue()) {
                Bonus bonus = new Bonus(this.x, this.y, (int) AsteroidAttack.rnd(35.0, 45.4), (int) AsteroidAttack.rnd(17.0, 27.0));
                AsteroidAttack.bonuses.add(bonus);
            }
        }
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
    public int getRadius() { return radius; }

    @Override
    public boolean isEnable() { return exists; }

    @Override
    public void paint(Graphics g) {
        switch (type) {
            case 0:
                g.drawImage(AsteroidAttack.ast, x-radius, y-radius, x-radius+SPRITE_CELL, y-radius+SPRITE_CELL, this.animPhase*SPRITE_CELL, 0, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL, null);
                return;
            case 1:
                g.drawImage(AsteroidAttack.ast, x-radius, y-radius, x-radius+SPRITE_CELL, y-radius+SPRITE_CELL, this.animPhase * SPRITE_CELL, SPRITE_CELL, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL * 2, null);
                return;
        }
    }
}

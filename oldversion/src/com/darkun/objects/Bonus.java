package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/**
 * Here we wil work with bonus object - creating, drawing and etc. Remember, that bonus object exists limited
 * period of time. If player will not pick it up, it gone.
 */
public class Bonus extends GameObject implements Fly, CheckEnable {

    static final int DY = 35;
    static final int RADIUS = 10; // size of sprite cell in pixels 72 x 72
    static final int ANIM_SPEED = 10; // speed of animation
    static final int ANIM_FRAMES = 200; // how many frames do we have for animation

    int animTime, animPhase, delay, speed, anim_speed; // position of explosion, phase of animation and delay to creation

    public Bonus (int x, int y, int delay, int speed)
    {
        this.x = x; // starting position
        this.y = y;
        this.animTime = 0;
        this.exists = true;
        this.delay = delay; // delay to appear
        this.speed = speed;
        this.anim_speed = speed / 2;
    }

    //
    public void getBonus() {
       //Bonus bonus = new Bonus(this.x, this.y, (int) AsteroidAttack.rnd(35.0, 45.4), (int) AsteroidAttack.rnd(17.0, 27.0));
       //AsteroidAttack.bonuses.add(bonus);
        this.exists = false;
        System.out.println("Gotta!");
    }

    public int getRadius() { return RADIUS; }

    public boolean checkPlayerPickup(int x, int y, int radius) {
        for (Bonus bonus : AsteroidAttack.bonuses) {
            // here we check, if our bonus got to radius of playership
            if((bonus.isEnable()) && (Math.sqrt(Math.pow((double)bonus.getX() - (double)x, 2) + Math.pow((double)bonus.getY() - (double)y, 2)) <= radius)) {
                bonus.getBonus();
                return true;
            }
        }
        return false;
    }

    @Override
    public void fly() {
        if (exists) {
            if(this.getFlyTime() > this.speed) {// checking, if our asteroid not too hurry )
                this.y += DY;
                if((y + DY) < AsteroidAttack.FIELD_HEIGHT + RADIUS) this.exists = true;
                else {
                    this.exists = false;
                }
                this.flyTime = 0;
            }
            else this.flyTime++;

            if(this.animTime >= this.anim_speed) {
                if(this.animPhase < ANIM_FRAMES) this.animPhase+=15;
                else this.animPhase = 0;
                animTime = 0;
            }
            else this.animTime++;
        }
    }

    @Override
    public boolean isEnable() { return exists; }

    @Override
    public void paint(Graphics g) {
        //g.drawImage(AsteroidAttack.bonus_sprite, x-(SPRITE_CELL/2), y-(SPRITE_CELL/2), x-(SPRITE_CELL/2)+SPRITE_CELL, y-(SPRITE_CELL/2)+SPRITE_CELL, this.animPhase*SPRITE_CELL, 0, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL, null);
        //g.draw3DRect(x-(SPRITE_CELL/2), y-(SPRITE_CELL/2), 15, 15, true);
        g.setColor(new Color(255 - this.animPhase, 255 - this.animPhase, 0));
        g.fillOval(x-(RADIUS/2), y-(RADIUS/2), RADIUS, RADIUS);
        g.setColor(new Color(0, 255 - this.animPhase, 0));
        g.drawOval(x-(RADIUS/2), y-(RADIUS/2), RADIUS, RADIUS);
    }
}
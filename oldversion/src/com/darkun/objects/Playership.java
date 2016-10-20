package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/*
 * Player class.
 * setDirection() - setting direction of spaceship
 * move() - moving playership in selected direction
 * shotMissile() - missile launch
 * fly() - calculate right params for drawing object
 * getXXX() - get XXX parametr of playership
 *
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
*/
public class Playership extends GameObject {
    private static final int M_FLY_OFFSET = 19; // distance from center of spaceship to lunch missile

    final int RADIUS = 25; // radius of playership to check damages
    final int DX = 2;
    final int DY = 2;
    final int WIDTH = 30;
    final int HEIGHT = 30;
    final int FLAME_CELL = 5; // width of flame sprite cell
    final int FLAME_FRAMES = 19;
    final int ANIM_SPEED = 25; // speed of flame animation
    final long DELAY = 750; // delay for next lunch
    int x, y, direction;
    long lastLunch, lastShot; // here we will store last lunch time
    int health, flameTime, flamePhase; // if it is less then 1 - game over
    boolean wing = true; // from what wing of ship do we start missile this time
    private final int BORDER_LEFT_OFFSET = 0; // offset from left side of screen for spaceship
    private final int BORDER_RIGHT_OFFSET = AsteroidAttack.FIELD_WIDTH - WIDTH;
    private final int BORDER_BOTTOP_OFFSET = AsteroidAttack.FIELD_HEIGHT - HEIGHT - RADIUS;
    private final int BORDER_TOP_OFFSET = AsteroidAttack.FIELD_HEIGHT/2 + HEIGHT + 16;

    public Playership() {
        this.x = 400;
        this.y = AsteroidAttack.FIELD_HEIGHT - HEIGHT - 100;
        this.health = 100;
        this.lastLunch = System.currentTimeMillis() - 1500; // this is needed to fire from fist seconds of game
    }

    public void move() { // spaceship can move
        if (direction == AsteroidAttack.LEFT && x > BORDER_LEFT_OFFSET) x -= DX;
        if (direction == AsteroidAttack.RIGHT && x < BORDER_RIGHT_OFFSET) x += DX;
        if (direction == AsteroidAttack.DOWN && y < BORDER_BOTTOP_OFFSET) y += DY;
        if (direction == AsteroidAttack.UP && y > BORDER_TOP_OFFSET) y -= DY;
        if (direction == AsteroidAttack.UP_AND_RIGHT && y > BORDER_TOP_OFFSET && x < BORDER_RIGHT_OFFSET ) {
            y -= DY;
            x += DX;
        }
        if (direction == AsteroidAttack.UP_AND_LEFT && y > BORDER_TOP_OFFSET && x > BORDER_LEFT_OFFSET) {
            y -= DY;
            x -= DX;
        }
        if (direction == AsteroidAttack.DOWN_AND_RIGHT && y < BORDER_BOTTOP_OFFSET) {
            y += DY;
            x += DX;
        }
        if (direction == AsteroidAttack.DOWN_AND_LEFT && y < BORDER_BOTTOP_OFFSET) {
            y += DY;
            x -= DX;
        }
    }

    public void setDirection(int direction) { this.direction = direction; }

    public void shotMissile() {
        /*playSound(new File("sounds/shoot.wav"));
        ray.start(x, y);
        System.out.println(System.currentTimeMillis() - this.lastLunch);*/
        if((System.currentTimeMillis() - this.lastLunch) > DELAY) {
            if(wing) {
                AsteroidAttack.missiles.add(new Missile(x + M_FLY_OFFSET, y));
                this.wing = false;
            }
            else {
                AsteroidAttack.missiles.add(new Missile(x - M_FLY_OFFSET, y));
                this.wing = true;
            }
            this.lastLunch = System.currentTimeMillis();
        }
    }

    public void fly() { // drawing ship animation
        if(this.flameTime >= ANIM_SPEED) {
            if(this.flamePhase < FLAME_FRAMES) this.flamePhase++;
            else this.flamePhase = 0;

            this.flameTime = 0;
        }
        else this.flameTime++;

        for (Bonus bonus : AsteroidAttack.bonuses) {
            // here we check, if our bonus got to radius of playership
            if(bonus.checkPlayerPickup(x, y, RADIUS)) {
                bonus.getBonus();
            }
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public int getRadius() { return RADIUS; }
    public int getWidth() { return WIDTH; }
    public int getHealth() { return health; }
    public void getDamage(int damage) {
        AsteroidAttack.m_explosions.add(new MissileBoom(x, y));
        health -= damage;
        if(health <= 0) AsteroidAttack.gameOver = true;
    }

    @Override
    public void paint(Graphics g) {
        g.drawImage(AsteroidAttack.ship, x-RADIUS, y-RADIUS, null);
        g.drawImage(AsteroidAttack.driverflame, x-RADIUS+38, y-RADIUS+67, x-RADIUS+38+FLAME_CELL, y-RADIUS+67+12,  this.flamePhase*FLAME_CELL, 0, this.flamePhase*FLAME_CELL+FLAME_CELL, FLAME_CELL, null);
    }
}

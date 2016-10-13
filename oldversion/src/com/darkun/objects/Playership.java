package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/**
 * Created by Jag on 06.10.2016.
 */
public class Playership extends GameObject {
    private static final int M_FLY_OFFSET = 19; // distance from center of center of spaceship to lunch missile
    final int RADIUS = 25; // radius of playership to check damages
    final int DX = 2;
    final int DY = 2;
    final int WIDTH = 30;
    final int HEIGHT = 30;
    final int FLAME_CELL = 5; // width of flame sprite cell
    final int FLAME_FRAMES = 19;
    final int ANIM_SPEED = 25; // speed of flame animation
    final long DELAY = 1500; // delay for next lunch
    int x, y, direction;
    long lastLunch, lastShot; // here we will store last lunch time
    int health, flameTime, flamePhase; // if it is less then 1 - game over
    boolean wing = true; // from what wing of ship do we start missile this time

    public Playership() {
        this.x = 400;
        this.y = FIELD_HEIGHT - HEIGHT - 100;
        this.health = 100;
        this.lastLunch = System.currentTimeMillis() - 1500; // this is needed to fire from fist seconds of game
    }

    public void move() { // spaceship can move
        if (direction == AsteroidAttack.LEFT && x > 40) x -= DX;
        if (direction == AsteroidAttack.RIGHT && x < AsteroidAttack.FIELD_WIDTH - WIDTH) x += DX;
        if (direction == AsteroidAttack.DOWN && y < AsteroidAttack.FIELD_HEIGHT - HEIGHT - RADIUS*2) y += DY;
        if (direction == AsteroidAttack.UP && y > AsteroidAttack.FIELD_HEIGHT/2 - HEIGHT - 16) y -= DY;
        if (direction == AsteroidAttack.UP_AND_RIGHT && y > AsteroidAttack.FIELD_HEIGHT/2 - HEIGHT - 16 && x < FIELD_WIDTH - WIDTH ) { y -= DY; x += DX; }
        if (direction == AsteroidAttack.UP_AND_LEFT && y > AsteroidAttack.FIELD_HEIGHT/2 - HEIGHT - 16 && x > 40) { y -= DY; x -= DX; }
        if (direction == AsteroidAttack.DOWN_AND_RIGHT && y < AsteroidAttack.FIELD_HEIGHT - HEIGHT - RADIUS*2) { y += DY; x += DX; }
        if (direction == AsteroidAttack.DOWN_AND_LEFT && y < AsteroidAttack.FIELD_HEIGHT - HEIGHT - RADIUS*2) { y += DY; x -= DX; }
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

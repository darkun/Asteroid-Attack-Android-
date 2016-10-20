package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/**
 * Abstract class of game object
 *
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
 */
public abstract class GameObject implements CheckEnable {
    protected int x, y, flyTime; // coords of object and animation staff
    protected boolean exists; // is object alive?

    public int getX() { return x; } // return object X coord
    public int getY() { return y; } // return object Y coord
    public int getFlyTime() { return flyTime; } // return object flyTime
    public boolean isEnable() { return exists; }

    public abstract void paint(Graphics g); // drawing game object
}

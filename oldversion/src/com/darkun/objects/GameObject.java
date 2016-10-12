package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/**
 * Created by Jag on 06.10.2016.
 */
public abstract class GameObject {
    final int POINT_SCALE = 2;
    final int FIELD_WIDTH = 400*POINT_SCALE;
    final int FIELD_HEIGHT = 300*POINT_SCALE;
    protected int x, y, flyTime; // coords of object and animation staff
    protected boolean exists; // is object alive?

    public boolean isEnable() { return this.exists; } // and if we need to know, is object active?

    public int getX() { return x; } // return object X coord
    public int getY() { return y; } // return object Y coord
    public int getFlyTime() { return flyTime; } // return object flyTime
    public void increaseFlyTime() { flyTime++; } // increase flyTime by 1

    public abstract void paint(Graphics g); // drawing game object
}

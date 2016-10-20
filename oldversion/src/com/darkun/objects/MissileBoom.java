package com.darkun.objects;

import com.darkun.AsteroidAttack;

import java.awt.*;

/*
 * Missile Boom is a missile explosion staff
 * explode() - init explosion start
 *
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
*/
public class MissileBoom extends GameObject implements CheckEnable {

    static final int SPRITE_CELL = 72; // size of sprite cell
    static final int ANIM_SPEED = 1; // speed of animation
    static final int ANIM_FRAMES = 71; // how many frames do we have for animation

    int animTime, animPhase; // position of explosion and phase of animation

    public MissileBoom (int x, int y)
    {
        this.x = x; // starting position
        this.y = y;
        this.animTime = 0;
        this.exists = true;
    }

    public void explode() {
        if (exists) {
            if(this.animTime >= ANIM_SPEED) {
                if(this.animPhase < ANIM_FRAMES) this.animPhase++;
                else this.exists = false; // end of explosion
                animTime = 0;
            }
            else this.animTime++;
        }
    }

    @Override
    public boolean isEnable() { return exists; }

    @Override
    public void paint(Graphics g) {
        g.drawImage(AsteroidAttack.m_explosion, x-(SPRITE_CELL/2), y-(SPRITE_CELL/2), x-(SPRITE_CELL/2)+SPRITE_CELL, y-(SPRITE_CELL/2)+SPRITE_CELL, this.animPhase*SPRITE_CELL, 0, this.animPhase*SPRITE_CELL+SPRITE_CELL, SPRITE_CELL, null);
    }
}

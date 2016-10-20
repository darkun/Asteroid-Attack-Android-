package com.darkun.environment;

import com.darkun.AsteroidAttack;

import java.awt.*;

/*
 * Space enviroment work. slide() - just for scroll space (motion imitation)
 * @author Dmitry Kartsev, based on SpaceInviders by Sergey (biblelamp) - https://github.com/biblelamp
 * @version 0.5.2 19/10/2016
*/
public class Space { // wave of asteroid attack
    static final int STEP = 1; // step for space wallpaper scroll
    static final int SPEED = 35; // speed of animation
    static final int ANIM_FRAMES = 621;

    //protected AsteroidAttack asteroidattack;

    int animPhase, animTime;

    public Space () {
        this.animPhase = ANIM_FRAMES; // starting frame
        this.animTime = 0;
    }

    public void slide() {
        if(this.animTime >= SPEED) {
            if(this.animPhase > 0) this.animPhase-=STEP;
            else this.animPhase = ANIM_FRAMES;
            animTime = 0;
        }
        else this.animTime++;
    }

    public void paint(Graphics g) {
        g.drawImage(AsteroidAttack.space, 0, 0, AsteroidAttack.FIELD_WIDTH, AsteroidAttack.FIELD_HEIGHT, 0, this.animPhase * STEP, AsteroidAttack.FIELD_WIDTH, AsteroidAttack.FIELD_HEIGHT + this.animPhase * STEP, null);
    }
}
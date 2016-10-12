package com.darkun.environment;

import com.darkun.AsteroidAttack;

import java.awt.*;

/**
 * Created by Jag on 06.10.2016.
 */
public class Space { // wave of asteroid attack
    final int STEP = 1; // step for space wallpaper scroll
    final int SPEED = 35; // speed of animation
    final int ANIM_FRAMES = 821;

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
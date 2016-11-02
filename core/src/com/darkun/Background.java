package com.darkun;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 27.10.16
 */
public class Background {
    private static int STEP = 1;  // step for space wallpaper scroll
    private static int SPEED = 10; // speed of animation

    private Texture texture;
    private int counter = 0;
    private int offset = 0;

    public Background(Texture texture) {
        this.texture = texture;
    }

    public void draw(Batch batch) {
        if (counter >= SPEED) {
            counter = 0;
            offset = (offset >= texture.getHeight() - SCREEN_HEIGHT) ? 0 : offset + STEP;
        }
        else counter++;

        batch.draw(texture, 0, -offset);
    }
}
package com.darkun.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 27.10.16
 */
public class Asteroid {

    private Animation animation;
    private float stateTime = 0f;

    public Asteroid(Texture texture, int srcWidth, int srcHeight) {
        TextureRegion[][] split = TextureRegion.split(texture, srcWidth, srcHeight);
        Array<TextureRegion> regions = new Array<>();

        for (TextureRegion[] i : split) {
            for (TextureRegion j : i) {
                regions.add(j);
            }
        }
        // the last frame is empty, a feature of the sprite
        regions.pop();

        if (MathUtils.randomBoolean())
            regions.reverse();

        animation = new Animation(0.1f, regions);
    }


    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        batch.draw(frame, 100, 150);
    }
}

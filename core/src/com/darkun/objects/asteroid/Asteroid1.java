package com.darkun.objects.asteroid;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;

import static com.darkun.objects.Utils.textureToRegions;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 27.10.16
 */
public class Asteroid1 extends AsteroidImpl {
    public Asteroid1(Texture texture, int srcWidth, int srcHeight) {
        super(texture, srcWidth, srcHeight);
        Array<TextureRegion> regions = textureToRegions(texture, srcWidth, srcHeight);

        // the last frame is empty, a feature of the sprite
        regions.pop();

        if (MathUtils.randomBoolean())
            regions.reverse();

        animation = new Animation(FRAME_DURATION, regions);
    }
}

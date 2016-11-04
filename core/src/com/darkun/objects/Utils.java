package com.darkun.objects;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 04.11.16
 */
public class Utils {
    public static Array<TextureRegion> textureToRegions(Texture texture, int srcWidth, int srcHeight) {
        Array<TextureRegion> regions = new Array<>();

        TextureRegion[][] split = TextureRegion.split(texture, srcWidth, srcHeight);
        for (TextureRegion[] i : split) {
            for (TextureRegion j : i) {
                regions.add(j);
            }
        }
        return regions;
    }
}

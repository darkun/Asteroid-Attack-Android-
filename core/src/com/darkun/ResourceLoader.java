package com.darkun;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 25.10.16
 */
public class ResourceLoader {
    public static final String SPACE = "space.jpg";
    public static final String SPACESHIP = "spaceship.png";

    public static void load(AssetManager assets) {
        assets.load(SPACE, Texture.class);
        assets.load(SPACESHIP, Texture.class);
    }
}

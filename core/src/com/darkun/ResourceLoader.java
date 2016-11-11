package com.darkun;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 25.10.16
 */
public class ResourceLoader {
    public static final String SPACE = "space.jpg";
    public static final String SPACESHIP = "spaceship.png";
    public static final String ASTEROID_1 = "asteroid-1.png";
    public static final String ASTEROID_2 = "asteroid-2.png";
    public static final String MISSILE = "missile.png";
    public static final String BACKMUSIC = "sounds/music/song.mp3";

    public static void load(AssetManager assets) {
        assets.load(SPACE, Texture.class);
        assets.load(SPACESHIP, Texture.class);
        assets.load(ASTEROID_1, Texture.class);
        assets.load(ASTEROID_2, Texture.class);
        assets.load(MISSILE, Texture.class);
        assets.load(BACKMUSIC, Music.class);
    }
}

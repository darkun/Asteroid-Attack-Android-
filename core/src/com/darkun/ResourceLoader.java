package com.darkun;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 25.10.16
 */
public class ResourceLoader {

    public static final String GOVER_SPLASH = "images/splash/go_splash.jpg";
    public static final String SPACE = "images/space.jpg";
    public static final String SPACESHIP = "images/spaceship.png";
    public static final String MISSILE = "images/missile.png";

    public static final String ASTEROID_1 = "sprites/asteroid-1.png";
    public static final String ASTEROID_2 = "sprites/asteroid-2.png";

    public static final String EXPLOSION = "sprites/m_explosion.png";

    public static final String BACK_MUSIC = "sounds/song.mp3";
    public static final String BACK_MUSIC_MENU = "sounds/main_menu.mp3";

    public static final String EXPLOSION_SND = "sounds/missile_explode.wav";
    public static final String MISSILE_LAUNCH_SND = "sounds/missile_launch.wav";

    public static final String BIG_WHITE_FONT = "fonts/white.fnt";
    public static final String BIG_BLACK_FONT = "fonts/black.fnt";
    public static final String SYS_WHITE_FONT = "fonts/system.fnt";


    public static void load(AssetManager assets) {
        assets.load(SPACE, Texture.class);
        assets.load(GOVER_SPLASH, Texture.class);
        assets.load(SPACESHIP, Texture.class);
        assets.load(ASTEROID_1, Texture.class);
        assets.load(ASTEROID_2, Texture.class);
        assets.load(EXPLOSION, Texture.class);
        assets.load(MISSILE, Texture.class);
        assets.load(BACK_MUSIC, Music.class);
        assets.load(BACK_MUSIC_MENU, Music.class);
        assets.load(EXPLOSION_SND, Sound.class);
        assets.load(MISSILE_LAUNCH_SND, Sound.class);
        assets.load(BIG_WHITE_FONT, BitmapFont.class);
        assets.load(BIG_BLACK_FONT, BitmapFont.class);
        assets.load(SYS_WHITE_FONT, BitmapFont.class);
    }
}

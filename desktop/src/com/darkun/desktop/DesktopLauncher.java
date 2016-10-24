package com.darkun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.darkun.AsteroidAttack;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Asteroid Attack! Scroller Game v2";
        config.width = AsteroidAttack.SCREEN_WIDTH;
        config.height = AsteroidAttack.SCREEN_HEIGHT;
        new LwjglApplication(new AsteroidAttack(), config);
	}
}

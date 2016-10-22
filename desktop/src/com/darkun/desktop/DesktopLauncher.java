package com.darkun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.darkun.Game;

public class DesktopLauncher {
	public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Asteroid Attack! Scroller Game v2";
        config.width = 500;
        config.height = 800;
        new LwjglApplication(new Game(), config);
	}
}

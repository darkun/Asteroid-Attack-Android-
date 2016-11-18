package com.darkun;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkun.screen.AttackScreen;
import com.darkun.screen.MainMenuScreen;
import lombok.Getter;


public class AsteroidAttack extends Game {
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 800;

    @Getter
    private SpriteBatch batch;
    @Getter
    private AssetManager assetManager;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();

        assetManager = new AssetManager();
        ResourceLoader.load(assetManager);
        assetManager.finishLoading();

        //this.setScreen(new AttackScreen(this));
        this.setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }
}

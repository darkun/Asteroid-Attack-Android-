package com.darkun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


public class AsteroidAttack extends Game {
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 800;

    private SpriteBatch batch;
    private AssetManager assetManager;

    @Override
    public void create() {
        batch = new SpriteBatch();

        assetManager = new AssetManager();
        ResourceLoader.load(assetManager);
        assetManager.finishLoading();

        this.setScreen(new AttackScreen(this));
    }

    @Override
    public void render() {
        super.render();
    }

    @Override
    public void dispose() {
        batch.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public AssetManager getAssetManager() {
        return assetManager;
    }
}

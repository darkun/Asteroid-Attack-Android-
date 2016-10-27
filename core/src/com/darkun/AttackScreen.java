package com.darkun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkun.objects.SpaceShip;

import static com.darkun.ResourceLoader.SPACE;
import static com.darkun.ResourceLoader.SPACESHIP;
import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 24.10.16
 */
public class AttackScreen implements Screen {
    private AsteroidAttack game;
    private Background background;
    private SpaceShip spaceShip;
    private OrthographicCamera camera;

    public AttackScreen(final AsteroidAttack game) {
        this.game = game;

        AssetManager assets = game.getAssetManager();
        background = new Background(assets.get(SPACE, Texture.class));
        spaceShip = new SpaceShip(assets.get(SPACESHIP, Texture.class), SCREEN_WIDTH / 2, 80);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        background.draw(batch);
        spaceShip.draw(batch);
        batch.end();

        spaceShip.processKeys();
    }

    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        game.getAssetManager().dispose();
        spaceShip.dispose();
    }
}

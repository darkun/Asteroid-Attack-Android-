package com.darkun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.darkun.objects.AsteroidPool;
import com.darkun.objects.Player;
import com.darkun.objects.asteroid.Asteroid;
import com.darkun.objects.SpaceShip;

import java.util.ArrayList;
import java.util.List;

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
    private Player player;
    private BitmapFont font;

    private AsteroidPool asteroidPool;
    private List<Asteroid> activeAsteroids = new ArrayList<>();

    public AttackScreen(final AsteroidAttack game) {
        this.game = game;

        AssetManager assets = game.getAssetManager();
        background = new Background(assets.get(SPACE, Texture.class));
        spaceShip = new SpaceShip(assets.get(SPACESHIP, Texture.class), SCREEN_WIDTH / 2, 80);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        asteroidPool = new AsteroidPool(assets);
        activeAsteroids.add(asteroidPool.obtain());
        player = new Player();

        font = new BitmapFont();
        font.setColor(Color.BLUE);
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

        //todo change the operating logic
        if (MathUtils.random(100) > 99) {
            activeAsteroids.add(asteroidPool.obtain());
        }

        for (Asteroid i : activeAsteroids) {
            i.draw(batch);
        }

        font.draw(batch, String.valueOf(player.getHealth()), SCREEN_WIDTH - 30, SCREEN_HEIGHT - 10);
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

package com.darkun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkun.objects.Asteroid;
import com.darkun.objects.Missile;
import com.darkun.objects.SpaceShip;

import java.util.ArrayList;

import static com.darkun.ResourceLoader.ASTEROID;
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
    public static ArrayList<Missile> missiles = new ArrayList<>(); // missiles, launched by player
    private OrthographicCamera camera;
    private Asteroid asteroid;

    public AttackScreen(final AsteroidAttack game) {
        this.game = game;

        AssetManager assets = game.getAssetManager();
        background = new Background(assets.get(SPACE, Texture.class));
        spaceShip = new SpaceShip(assets.get(SPACESHIP, Texture.class), SCREEN_WIDTH / 2, 80);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        asteroid = new Asteroid(assets.get(ASTEROID, Texture.class), 72, 72);
    }

    @Override
    public void show() {}

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateGameLogic();
        camera.update();
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        background.draw(batch);
        for (Missile missile : missiles) {
            if (missile.isEnable()) missile.draw(batch);
        }
        spaceShip.draw(batch);
        asteroid.draw(batch);
        batch.end();

        spaceShip.processKeys();
    }

    private void updateGameLogic() { // let's update our game situation
        for (Missile missile : missiles) {
            if (missile.isEnable()) missile.update();
        }

        clearObjects();
    }

    void clearObjects() { // let's delete empty objects from array lists
        for(int i = 0; i < missiles.size(); i++) { // for missiles
            if(!missiles.get(i).isEnable()) missiles.remove(i);
        }
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

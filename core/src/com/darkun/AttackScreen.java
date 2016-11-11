package com.darkun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.darkun.objects.AsteroidPool;
import com.darkun.objects.MissilePool;
import com.darkun.objects.Player;
import com.darkun.objects.asteroid.Asteroid;
import com.darkun.objects.SpaceShip;
import com.darkun.objects.missile.Missile;

import java.util.ArrayList;
import java.util.List;

import static com.darkun.ResourceLoader.BACKMUSIC;
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
    private BackgroundMusic backgroundMusic;

    private AsteroidPool asteroidPool;
    private List<Asteroid> activeAsteroids = new ArrayList<>();

    private MissilePool missilePool;
    private List<Missile> activeMissiles = new ArrayList<>();

    //public static ArrayList<Missile> missiles = new ArrayList<>(); // missiles, launched by player

    public AttackScreen(final AsteroidAttack game) {
        this.game = game;

        AssetManager assets = game.getAssetManager();
        background = new Background(assets.get(SPACE, Texture.class));
        spaceShip = new SpaceShip(assets.get(SPACESHIP, Texture.class), SCREEN_WIDTH / 2, 80, this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        asteroidPool = new AsteroidPool(assets);
        activeAsteroids.add(asteroidPool.obtain());
        missilePool = new MissilePool(assets);
        player = new Player();

        font = new BitmapFont();
        font.setColor(Color.BLUE);

        backgroundMusic = new BackgroundMusic(assets.get(BACKMUSIC, Music.class));
        backgroundMusic.play();
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

        for (Missile m : activeMissiles) {
            if(m.isEnable()) m.draw(batch);
        }

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

        clearObjects();
    }

    private void updateGameLogic() { // let's update our game situation
        // TODO now we update game situation right in draw() method of objects. I think, later we will need to feel this.
    }

    void clearObjects() { // let's delete empty objects from array lists
        // that's why I use "boolean active" for missiles. Else we will get an error in draw() - not initialized cords
        // So, I do not understand, why do we have in activeMissiles arrow NOT active missiles. U can check it yourself.
        Missile mis;
        for (int i = activeMissiles.size(); --i >= 0;) {
            mis = activeMissiles.get(i);
            if (!mis.isEnable()) {
                activeMissiles.remove(i);
                missilePool.free(mis);
            }
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

    public void addMissileToPool(float x, float y) {
        Missile mis = missilePool.obtain();
        mis.start(x, y);
        activeMissiles.add(mis);
    }
}

package com.darkun.screen;

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
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.darkun.AsteroidAttack;
import com.darkun.Background;
import com.darkun.BackgroundMusic;
import com.darkun.entity.*;
import com.darkun.pool.AsteroidPool;
import com.darkun.pool.ExplodePool;
import com.darkun.pool.MissilePool;

import java.util.ArrayList;
import java.util.List;

import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;
import static com.darkun.ResourceLoader.*;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 24.10.16
 */
public class AttackScreen implements Screen {
    private final boolean DEBUG_BOUNDS = true;

    private AsteroidAttack game;
    private Background background;
    private SpaceShip spaceShip;
    private OrthographicCamera camera;
    private Player player;
    private BitmapFont font;
    private BackgroundMusic backgroundMusic;

    private AsteroidPool asteroidPool;
    private MissilePool missilePool;
    private ExplodePool explodePool;

    private List<AsteroidImpl> activeAsteroids = new ArrayList<>();
    private List<MissileImpl> activeMissiles = new ArrayList<>();
    private List<ExplodeImpl> activeExplodes = new ArrayList<>();

    public AttackScreen(final AsteroidAttack game) {
        this.game = game;

        AssetManager assets = game.getAssetManager();
        background = new Background(assets.get(SPACE, Texture.class));
        spaceShip = new SpaceShip(assets.get(SPACESHIP, Texture.class), SCREEN_WIDTH / 2, 80, this);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        asteroidPool = new AsteroidPool(assets);
        missilePool = new MissilePool(assets);
        explodePool = new ExplodePool(assets);
        player = new Player();

        font = assets.get(SYS_WHITE_FONT, BitmapFont.class);
        font.setColor(Color.BLUE);

        backgroundMusic = new BackgroundMusic(assets.get(BACK_MUSIC, Music.class));
        backgroundMusic.play();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        updateGameLogic();

        batch.begin();
        background.draw(batch);

        spaceShip.draw(batch);
        activeMissiles.forEach(m -> m.draw(batch));
        activeAsteroids.forEach(asteroid -> asteroid.draw(batch));
        activeExplodes.forEach(e -> e.draw(batch));

        font.draw(batch, String.valueOf(player.getHealth()), SCREEN_WIDTH - 40, SCREEN_HEIGHT - 12);
        font.draw(batch, String.valueOf(player.getGamePoints()), SCREEN_WIDTH - 120, SCREEN_HEIGHT - 12);
        batch.end();

        if (DEBUG_BOUNDS) {
            ShapeRenderer shapeRenderer = new ShapeRenderer();
            shapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            activeAsteroids.forEach(a -> a.debugBounds(shapeRenderer));
            activeMissiles.forEach(m -> m.debugBounds(shapeRenderer));
            activeExplodes.forEach(e -> e.debugBounds(shapeRenderer));
            spaceShip.debugBounds(shapeRenderer);
            shapeRenderer.end();
        }

        spaceShip.processKeys();
    }

    private void updateGameLogic() {
        if(player.gameOver) {
            backgroundMusic.stop();
            game.setScreen(new GameOverScreen(game));
        } else {
            activeAsteroids.forEach(a -> {
                if (a.getPosition().y < 0) { asteroidPool.free(a); }
                else if (a.contains(spaceShip.getCrashPoint())) {
                    createExplode(a.getPosition());
                    player.getDamage(a.getBonusPoints() / 10);
                    asteroidPool.free(a);
                }
            });

            activeMissiles.forEach(m -> {
                if (m.getPosition().y > SCREEN_HEIGHT) {
                    missilePool.free(m);
                    return;
                }

                activeAsteroids.forEach(a -> {
                    if ((a.isActive()) && (a.contains(m.getBoomPoint()))) {
                        createExplode(a.getPosition());
                        player.addGamePoints(a.getBonusPoints());
                        player.getDamage(a.getBonusPoints());
                        asteroidPool.free(a);
                        missilePool.free(m);
                    }
                });
            });

            activeMissiles.removeIf(m -> !m.isActive());
            activeAsteroids.removeIf(a -> !a.isActive());
            activeExplodes.removeIf(e -> !e.isActive());

            //todo change the operating logic
            if (MathUtils.random(100) > 99) {
                AsteroidImpl asteroid = asteroidPool.obtain();

                float maxWidth = SCREEN_WIDTH - asteroid.getBounds().radius * 2;
                Vector2 position = new Vector2(MathUtils.random(maxWidth), SCREEN_HEIGHT);
                asteroid.start(position);

                if (activeAsteroids.stream().anyMatch(i -> i.getBounds().overlaps(asteroid.getBounds()))) {
                    Gdx.app.debug(AsteroidImpl.LOG_TAG, "Canceling - " + asteroid.toString());
                    asteroidPool.free(asteroid);
                } else {
                    activeAsteroids.add(asteroid);
                }
            }
        }
    }

    private void createExplode(Vector2 boomPoint) {
        ExplodeImpl explode = explodePool.obtain();
        explode.start(boomPoint);
        activeExplodes.add(explode);
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
        game.getAssetManager().dispose();
        spaceShip.dispose();
    }

    public void addMissileToPool(float x, float y) {
        MissileImpl mis = missilePool.obtain();
        activeMissiles.add(mis);
        mis.start(x, y);
    }
}

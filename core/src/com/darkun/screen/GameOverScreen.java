package com.darkun.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkun.AsteroidAttack;
import com.darkun.BackgroundMusic;

import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;
import static com.darkun.ResourceLoader.BACK_MUSIC_MENU;
import static com.darkun.ResourceLoader.GOVER_SPLASH;

/**
 * @author Kartsev Dmitry. <dek.alpha@mail.ru>
 * @since 19.11.16
 */
public class GameOverScreen implements Screen{

    private String MESSAGE_GAME_OVER = "You loose!";
    private String MESSAGE_TO_BEGIN = "Press ENTER to try again!";
    private AsteroidAttack game;

    private Texture background;
    private OrthographicCamera camera;
    private BitmapFont font;
    private BackgroundMusic backgroundMusic;

    public GameOverScreen(final AsteroidAttack gam) {
        this.game = gam;

        AssetManager assets = game.getAssetManager();
        background = assets.get(GOVER_SPLASH, Texture.class);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        font = new BitmapFont();
        font.getData().setScale(1.5f);
        font.setColor(Color.RED);

        backgroundMusic = new BackgroundMusic(assets.get(BACK_MUSIC_MENU, Music.class));
        backgroundMusic.play();
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
        batch.draw(background, 0, 0);

        font.draw(batch, MESSAGE_GAME_OVER, 140, 250);
        font.draw(batch, MESSAGE_TO_BEGIN, 120, 300);
        batch.end();

        if (Gdx.input.isKeyPressed(Input.Keys.ENTER)) {
            backgroundMusic.stop();
            game.setScreen(new AttackScreen(game));
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
    }
}
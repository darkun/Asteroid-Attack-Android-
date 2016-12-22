package com.darkun.screen;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenManager;
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
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.darkun.AsteroidAttack;
import com.darkun.BackgroundMusic;
import com.darkun.tween.SpriteAccessor;

import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;
import static com.darkun.ResourceLoader.*;

/**
 * @author Kartsev Dmitry. <dek.alpha@mail.ru>
 * @since 19.11.16
 */
public class GameOverScreen implements Screen{

    private static final float FADE_DURATION = 2.0f;
    private String MESSAGE_GAME_OVER = "You loose!";
    private String MESSAGE_TO_BEGIN = "Press ENTER to try again!";
    private AsteroidAttack game;


    private Texture background;
    private OrthographicCamera camera;
    private BitmapFont font, fontBlack;
    private BackgroundMusic backgroundMusic;
    private TweenManager tweenManager;
    private Sprite splash;

    public GameOverScreen(final AsteroidAttack gam) {
        this.game = gam;

        AssetManager assets = game.getAssetManager();
        background = assets.get(GOVER_SPLASH, Texture.class);
        splash = new Sprite(background);
        splash.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        camera = new OrthographicCamera();
        camera.setToOrtho(false, SCREEN_WIDTH, SCREEN_HEIGHT);

        font = assets.get(BIG_WHITE_FONT, BitmapFont.class);
        font.setColor(Color.RED);
        fontBlack = assets.get(BIG_BLACK_FONT, BitmapFont.class);

        backgroundMusic = new BackgroundMusic(assets.get(BACK_MUSIC_MENU, Music.class));
        backgroundMusic.play();

        tweenManager = new TweenManager();
        Tween.registerAccessor(Sprite.class, new SpriteAccessor());
    }

    @Override
    public void show() {
        Tween.set(splash, SpriteAccessor.ALPHA).target(0).start(tweenManager);
        Tween.to(splash, SpriteAccessor.ALPHA, FADE_DURATION).target(1).start(tweenManager);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        tweenManager.update(delta);

        camera.update();
        SpriteBatch batch = game.getBatch();
        batch.setProjectionMatrix(camera.combined);

        batch.begin();
        splash.draw(batch);

        fontBlack.draw(batch, MESSAGE_GAME_OVER, 120, 440);
        font.draw(batch, MESSAGE_TO_BEGIN, 100, 720);
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
        splash.getTexture().dispose();
    }
}
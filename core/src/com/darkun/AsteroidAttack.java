package com.darkun;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.darkun.screen.MainMenuScreen;
import lombok.Getter;


public class AsteroidAttack extends Game {
    public static final int SCREEN_WIDTH = 500;
    public static final int SCREEN_HEIGHT = 800;
    /*
    private static final String FONT_CHARACTERS = "абвгдеёжзийклмнопрстуфхцчшщъыьэюяАБВГДЕЁЖЗИЙКЛМНОПРСТУФХЦЧШЩЪЫЬЭЮЯabcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789][_!$%#@|/?-+=()*&.;,{}\"?`'<>";

    @Getter
    public BitmapFont mainFont;
     */
    @Getter
    private SpriteBatch batch;
    @Getter
    private AssetManager assetManager;

    @Override
    public void create() {
        Gdx.app.setLogLevel(Application.LOG_DEBUG);

        batch = new SpriteBatch();

        /*
        generateFont();
         */

        assetManager = new AssetManager();
        ResourceLoader.load(assetManager);
        assetManager.finishLoading();

        this.setScreen(new MainMenuScreen(this));
    }

    private void generateFont() {
        /*
        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Micra-Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.size = Gdx.graphics.getHeight() / 18;
        param.characters = FONT_CHARACTERS;
        mainFont = generator.generateFont(param);
        mainFont.setColor(Color.RED);
        generator.dispose();
        */
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

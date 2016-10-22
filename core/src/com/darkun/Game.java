package com.darkun;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public class Game extends ApplicationAdapter {
	private static final int WIDTH = 500;
	private static final int HEIGHT = 800;
	private SpriteBatch batch;
	private Sprite backgroundSprite;

	private Texture backgroundImg;
	private Texture spaceShipImg;
	private Rectangle spaceShip;

	private OrthographicCamera camera;
	
	@Override
	public void create () {
		backgroundImg = new Texture(Gdx.files.internal("space.jpg"));
		spaceShipImg = new Texture(Gdx.files.internal("spaceship.png"));

		backgroundSprite = new Sprite(backgroundImg);

		spaceShip = new Rectangle();
		spaceShip.setHeight(80).setWidth(80);
		spaceShip.setCenter(WIDTH / 2, 80);

		camera = new OrthographicCamera();
		camera.setToOrtho(false, WIDTH, HEIGHT);
		batch = new SpriteBatch();
	}

	@Override
	public void render () {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		camera.update();
		batch.setProjectionMatrix(camera.combined);

		batch.begin();
		backgroundSprite.draw(batch);
		batch.draw(spaceShipImg, spaceShip.getX(), spaceShip.getY());
		batch.end();

		if(Gdx.input.isKeyPressed(Input.Keys.LEFT))
			spaceShip.x -= 200 * Gdx.graphics.getDeltaTime();
		if(Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			spaceShip.x += 200 * Gdx.graphics.getDeltaTime();
	}
	
	@Override
	public void dispose () {
		batch.dispose();
		backgroundImg.dispose();
		spaceShipImg.dispose();
	}
}

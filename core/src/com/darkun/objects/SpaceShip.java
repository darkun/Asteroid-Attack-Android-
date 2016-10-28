package com.darkun.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;

import static com.darkun.AsteroidAttack.SCREEN_WIDTH;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 22.10.16
 */
public class SpaceShip implements Disposable {
    private Vector2 position;
    private Rectangle bounds;
    private Texture texture;

    private float offset_x; // наш корабль может чуть вылетать за пределы экрана, а то астероиды тоже вылетают

    public SpaceShip(Texture texture, float x, float y) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle()
                .setHeight(80)
                .setWidth(80)
                .setCenter(position);
        this.offset_x = bounds.getWidth() / 2;
    }

    public void processKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bounds.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bounds.x += 300 * Gdx.graphics.getDeltaTime();

        if (bounds.getX() < -offset_x) bounds.setX(-offset_x);
        if (bounds.getX() > SCREEN_WIDTH - offset_x)
            bounds.setX(SCREEN_WIDTH - offset_x);
    }

    public void draw(final Batch batch) {
        batch.draw(texture, bounds.getX(), bounds.getY());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}

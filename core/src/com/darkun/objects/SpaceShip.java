package com.darkun.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.darkun.AsteroidAttack;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 22.10.16
 */
public class SpaceShip implements Disposable {
    private Vector2 position;
    private Rectangle bounds;
    private Texture texture;

    public SpaceShip(Texture texture, float x, float y) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle()
                .setHeight(80)
                .setWidth(80)
                .setCenter(position);
    }

    public void processKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bounds.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bounds.x += 300 * Gdx.graphics.getDeltaTime();

        if (bounds.getX() < 0) bounds.setX(0);
        if (bounds.getX() > AsteroidAttack.SCREEN_WIDTH - bounds.getWidth())
            bounds.setX(AsteroidAttack.SCREEN_WIDTH - bounds.getWidth());
    }

    public void draw(final Batch batch) {
        batch.draw(texture, bounds.getX(), bounds.getY());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}

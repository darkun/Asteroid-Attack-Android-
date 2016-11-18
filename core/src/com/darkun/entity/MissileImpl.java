package com.darkun.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import lombok.Getter;
import lombok.ToString;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 06.11.2016.
 */
@ToString(exclude = "texture")
public class MissileImpl implements Missile {
    public static final String LOG_TAG = MissileImpl.class.getSimpleName().toUpperCase();
    @Getter
    private Vector2 position;
    @Getter
    private boolean active = false;

    private Texture texture;
    private Rectangle bounds;
    private int SPEED = 5;

    public MissileImpl(Texture texture) {
        this.texture = texture;
        position = new Vector2(0, 0);
        bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setPosition(position);
    }

    @Override
    public void draw(Batch batch) {
        position.y += SPEED;
        bounds.setPosition(position);
        batch.draw(texture, position.x, position.y);
    }

    @Override
    public void reset() {
        active = false;
        position.set(0, 0);
        Gdx.app.debug(LOG_TAG, "Removed - " + this.toString());
    }

    @Override
    public void start(float x, float y) {
        position.set(x, y);
        bounds.setPosition(position);
        active = true;
        Gdx.app.debug(LOG_TAG, "Started - " + this.toString());
    }

    public Vector2 getBoomPoint() {
        return new Vector2(position).add(0, bounds.getHeight() / 2);
    }

    public void debugBounds(ShapeRenderer renderer) {
        renderer.setColor(Color.RED);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}

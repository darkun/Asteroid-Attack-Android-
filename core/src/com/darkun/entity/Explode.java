package com.darkun.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 26.11.2016
 */
public interface Explode extends Pool.Poolable {
    void draw(Batch batch);

    void start(Vector2 boomPoint);
}

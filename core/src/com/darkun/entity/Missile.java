package com.darkun.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;
import com.darkun.GameSound;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 06.11.2016.
 */
public interface Missile extends Pool.Poolable {
    void draw(Batch batch);

    void start(float x, float y, GameSound sound);
}

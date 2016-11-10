package com.darkun.objects.missile;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 06.11.2016.
 */
public interface Missile extends Pool.Poolable {
    void draw(Batch batch);

    boolean isEnable();

    void start(float x, float y);
}

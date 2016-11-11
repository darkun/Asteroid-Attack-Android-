package com.darkun.entity;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.utils.Pool;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 03.11.16
 */
public interface Asteroid extends Pool.Poolable {
    void draw(Batch batch);
}

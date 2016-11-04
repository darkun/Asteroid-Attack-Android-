package com.darkun.objects;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Pool;
import com.darkun.objects.asteroid.Asteroid;
import com.darkun.objects.asteroid.Asteroid1;
import com.darkun.objects.asteroid.AsteroidImpl;

import static com.darkun.ResourceLoader.ASTEROID_1;
import static com.darkun.ResourceLoader.ASTEROID_2;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @todo remove 72
 * @since 03.11.16
 */
public class AsteroidPool extends Pool<Asteroid> {
    private AssetManager assets;

    public AsteroidPool(AssetManager manager) {
        this.assets = manager;
    }
    @Override
    protected Asteroid newObject() {
        if (MathUtils.randomBoolean())
            return new AsteroidImpl(assets.get(ASTEROID_2, Texture.class), 72, 72);
        else
            return new Asteroid1(assets.get(ASTEROID_1, Texture.class), 72, 72);
    }
}

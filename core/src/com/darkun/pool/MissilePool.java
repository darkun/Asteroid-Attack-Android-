package com.darkun.pool;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.utils.Pool;
import com.darkun.entity.MissileImpl;
import com.darkun.entity.Missile;

import static com.darkun.ResourceLoader.MISSILE;

/**
 * @author Kartsev Dmitry. <dek.alpha@mail.ru>
 * @since 06.11.16
 */
public class MissilePool extends Pool<Missile> {
    private AssetManager assets;

    public MissilePool(AssetManager manager) {
        this.assets = manager;
    }

    @Override
    protected Missile newObject() {
        return new MissileImpl(assets.get(MISSILE, Texture.class));
    }
}

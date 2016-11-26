package com.darkun.pool;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.utils.Pool;
import com.darkun.entity.ExplodeImpl;

/**
 * @author Jag. <dek.alpha@mail.ru>
 * @since 26.11.2016
 */
public class ExplodePool extends Pool<ExplodeImpl> {
    private AssetManager assets;

    public ExplodePool(AssetManager manager) {
        this.assets = manager;
    }

    @Override
    protected ExplodeImpl newObject() {
        return new ExplodeImpl(assets, 72, 72);
    }
}

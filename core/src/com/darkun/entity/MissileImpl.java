package com.darkun.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.darkun.AsteroidAttack;
import lombok.ToString;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 06.11.2016.
 */
@ToString
public class MissileImpl implements Missile {
    private Vector2 position;
    private float stateTime = 0f;
    private Texture texture;
    private Rectangle bounds;
    private boolean active; // is missile exists and not exploded yet?
    private int SPEED = 3; // speed of missile
    private int flyTime;

    public MissileImpl(Texture texture) {
        this.position = new Vector2();
        this.texture = texture;
        this.active = false;
    }

    @Override
    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        if(flyTime > SPEED) // checking, if our missile not too hurry )
        {
            position.y += bounds.getY();
            this.active = (position.y + bounds.getY()) < AsteroidAttack.SCREEN_HEIGHT;
            this.flyTime = 0;
        }
        else this.flyTime++;
        // TODO need to update
        position.y++;
        batch.draw(texture, position.x, position.y);
    }

    @Override
    public void reset() {
        position.set(0, 0);
    }

    @Override
    public void start(float x, float y) {
        this.position.set(x, y);
        this.bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setCenter(position);
        this.active = true;
    }

    @Override
    public boolean isEnable() { return this.active; }
}

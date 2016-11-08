package com.darkun.objects.missile;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.darkun.AsteroidAttack;

import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 06.11.2016.
 */
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
        this.active = false;
    }

    @Override
    public void shootMissile(float x, float y) {
        this.position.set(x, y);
        this.bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setCenter(position);
        this.active = true;
    }

    @Override
    public boolean isEnable() { return this.active; }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("MissileImpl{");
        sb.append("position=").append(position);
        sb.append(", stateTime=").append(stateTime);
        sb.append('}');
        return sb.toString();
    }
}

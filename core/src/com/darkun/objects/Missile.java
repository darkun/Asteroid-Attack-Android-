package com.darkun.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import com.darkun.AsteroidAttack;

/**
 * @author Kartsev D. <dek.alpha@gmail.com>
 * @since 03.11.16
 */
public class Missile {
    private Vector2 position; // coords of missile
    private static Texture texture = null;
    private Rectangle bounds;
    private boolean exists; // is missile exists and not exploded yet?
    static private final int SPEED = 3; // speed of missile
    private int flyTime;

    public Missile(float x, float y) {
        if(texture == null)
            texture = new Texture("missile.png"); // do not know, is it right, but can't understand, how to wright with assets
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setCenter(position);
        this.exists = true;
    }

    public void draw(final Batch batch) {
        batch.draw(texture, position.x, position.y);
    }

    public boolean isEnable() {
        return exists;
    }

    public void update() {
        if (exists) {
            if(flyTime > SPEED) // checking, if our missile not too hurry )
            {
                position.y += bounds.getY();
                this.exists = (position.y + bounds.getY()) < AsteroidAttack.SCREEN_HEIGHT;
                this.flyTime = 0;
            }
            else this.flyTime++;
        }
    }
}

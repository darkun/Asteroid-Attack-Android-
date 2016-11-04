package com.darkun.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.darkun.AsteroidAttack;
import com.darkun.AttackScreen;

import static com.darkun.AsteroidAttack.SCREEN_WIDTH;
import static com.darkun.ResourceLoader.SPACESHIP;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 22.10.16
 */
public class SpaceShip implements Disposable {
    private Vector2 position;
    private Rectangle bounds;
    private Texture texture;

    private static final double MISSILE_LAUNCH_DELAY = 750.0d; // delay between launching missiles
    private static final float M_FLY_X_OFFSET = 53.0f; // distance from center of spaceship to shoot missile
    private static final float M_FLY_Y_OFFSET = 30.0f; // distance from bottom of spaceship to shoot missile
    private static double lastShoot; // where was last missile launching
    private boolean rightWing; // we can start missiles from both wings
    private float offsetX; // наш корабль может чуть вылетать за пределы экрана, а то астероиды тоже вылетают

    public SpaceShip(Texture texture, float x, float y) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setCenter(position);
        this.offsetX = bounds.getWidth() / 2;
        this.rightWing = true;
        this.lastShoot = 0;
    }

    public void processKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            bounds.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            bounds.x += 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            startMissile(bounds.x, bounds.y);

        if (bounds.getX() < -offsetX) bounds.setX(-offsetX);
        if (bounds.getX() > SCREEN_WIDTH - offsetX)
            bounds.setX(SCREEN_WIDTH - offsetX);
    }

    private void startMissile(float x, float y) {
        if((System.currentTimeMillis() - lastShoot) > MISSILE_LAUNCH_DELAY) {
            if(rightWing) {
                AttackScreen.missiles.add(new Missile(x + M_FLY_X_OFFSET, y + M_FLY_Y_OFFSET));
                this.rightWing = false;
            }
            else {
                AttackScreen.missiles.add(new Missile(x + M_FLY_X_OFFSET / 3, y + M_FLY_Y_OFFSET));
                this.rightWing = true;
            }
            lastShoot = System.currentTimeMillis();
        }
    }

    public void draw(final Batch batch) {
        batch.draw(texture, bounds.getX(), bounds.getY());
    }

    @Override
    public void dispose() {
        texture.dispose();
    }
}

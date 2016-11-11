package com.darkun.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.darkun.AttackScreen;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 22.10.16
 */
public class SpaceShip implements Disposable {
    private Vector2 position;
    private Rectangle bounds;
    private Texture texture;
    private AttackScreen attackScreen;

    private static final double MISSILE_LAUNCH_DELAY = 750.0d; // delay between launching missiles
    private static final float M_FLY_X_OFFSET = 53.0f; // distance from center of spaceship to shoot missile
    private static final float M_FLY_Y_OFFSET = 30.0f; // distance from bottom of spaceship to shoot missile
    private static long lastShoot = 0; // where was last missile launching
    private boolean rightWing = true; // we can start missiles from both wings
    private float offsetX; // наш корабль может чуть вылетать за пределы экрана, а то астероиды тоже вылетают

    public SpaceShip(Texture texture, float x, float y, AttackScreen screen) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setCenter(position);
        this.offsetX = bounds.getWidth() / 2;
        this.attackScreen = screen;
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
        if(timeSinceMillis(lastShoot) > MISSILE_LAUNCH_DELAY) {
            if(rightWing) {
                attackScreen.addMissileToPool(x + M_FLY_X_OFFSET, y + M_FLY_Y_OFFSET);
                this.rightWing = false;
            }
            else {
                attackScreen.addMissileToPool(x + M_FLY_X_OFFSET / 3, y + M_FLY_Y_OFFSET);
                this.rightWing = true;
            }
            lastShoot = millis();
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

package com.darkun.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Disposable;
import com.darkun.screen.AttackScreen;
import lombok.Getter;
import lombok.ToString;

import static com.badlogic.gdx.utils.TimeUtils.millis;
import static com.badlogic.gdx.utils.TimeUtils.timeSinceMillis;
import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 22.10.16
 */
public class SpaceShip implements Disposable {
    public static final String LOG_TAG = SpaceShip.class.getSimpleName().toUpperCase();
    @Getter
    private Vector2 position;
    @Getter
    private Rectangle bounds;
    private Texture texture;
    private AttackScreen attackScreen;

    private static final double MISSILE_LAUNCH_DELAY = 750.0d; // delay between launching missiles
    private static final float M_FLY_X_OFFSET = 53.0f; // distance from center of spaceship to shoot missile
    private static final float M_FLY_Y_OFFSET = 30.0f; // distance from bottom of spaceship to shoot missile
    private static long lastShoot = 0; // when was last missile launching
    private boolean rightWing = true; // we can start missiles from both wings
    private float offsetX; // let's spaceship fly less over screen
    private float offsetY;

    public SpaceShip(Texture texture, float x, float y, AttackScreen screen) {
        this.texture = texture;
        this.position = new Vector2(x, y);
        this.bounds = new Rectangle()
                .setHeight(texture.getHeight())
                .setWidth(texture.getWidth())
                .setCenter(position);
        this.offsetX = bounds.getWidth() / 2;
        this.offsetY = bounds.getHeight();
        this.attackScreen = screen;
    }

    public void processKeys() {
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
            position.x -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.UP))
            position.y += 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN))
            position.y -= 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
            position.x += 300 * Gdx.graphics.getDeltaTime();
        if (Gdx.input.isKeyPressed(Input.Keys.SPACE))
            startMissile(position.x, position.y);

        if (position.x < -offsetX) position.x = -offsetX;
        if (position.x > SCREEN_WIDTH - offsetX)
            position.x = SCREEN_WIDTH - offsetX;
        if (position.y < 0) position.y = 0;
        if (position.y > SCREEN_HEIGHT / 3 - offsetY)
            position.y = SCREEN_HEIGHT / 3 - offsetY;
        bounds.setPosition(position);
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

    public Vector2 getCrashPoint() {
        return new Vector2(position).add(0 - bounds.getWidth() / 2 + bounds.getWidth(), bounds.getHeight() / 2);
    }

    public void debugBounds(ShapeRenderer renderer) {
        renderer.setColor(Color.MAGENTA);
        renderer.rect(bounds.x, bounds.y, bounds.width, bounds.height);
    }
}

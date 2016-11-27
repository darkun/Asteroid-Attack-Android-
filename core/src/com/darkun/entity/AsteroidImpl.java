package com.darkun.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import lombok.ToString;

import static com.darkun.Utils.textureToRegions;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 04.11.16
 */
@ToString(exclude = "animation")
public class AsteroidImpl implements Asteroid {
    public static final String LOG_TAG = AsteroidImpl.class.getSimpleName().toUpperCase();
    protected static float FRAME_DURATION = 0.1f;

    @Getter
    private boolean active = false;
    @Getter
    public Vector2 position;
    @Getter
    private Circle bounds;

    protected Animation animation;
    private float stateTime = 0f;

    public AsteroidImpl(Texture texture, int srcWidth, int srcHeight) {
        bounds = new Circle(new Vector2(), srcWidth / 2);
        Array<TextureRegion> regions = textureToRegions(texture, srcWidth, srcHeight);
        if (MathUtils.randomBoolean())
            regions.reverse();

        animation = new Animation(FRAME_DURATION, regions);
    }

    public void start(Vector2 position) {
        active = true;
        this.position = position;
        bounds.set(calculateCenter(bounds.radius), bounds.radius);
        stateTime = 0f;
        Gdx.app.debug(LOG_TAG, "Started - " + this.toString());
    }

    @Override
    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        // TODO need to update
        position.y--;
        bounds.set(calculateCenter(bounds.radius), bounds.radius);
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        batch.draw(frame, position.x, position.y);
    }

    public void debugBounds(ShapeRenderer renderer) {
        renderer.setColor(Color.MAGENTA);
        renderer.circle(bounds.x, bounds.y, bounds.radius);
    }

    @Override
    public void reset() {
        active = false;
        Gdx.app.debug(LOG_TAG, "Removed - " + this.toString());
    }

    public boolean contains (Vector2 point) {
        return bounds.contains(point);
    }

    public Vector2 calculateCenter(float radius) {
         return new Vector2(position.x + radius, position.y + radius);
    }
}

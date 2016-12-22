package com.darkun.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;
import lombok.Getter;
import lombok.ToString;

import static com.darkun.ResourceLoader.EXPLOSION;
import static com.darkun.ResourceLoader.EXPLOSION_SND;
import static com.darkun.Utils.textureToRegions;

/**
 * @author Dmitry Kartsev <dek.alpha@mail.ru>
 * @since 26.11.2016.
 */
@ToString(exclude = "texture")
public class ExplodeImpl implements Explode {
    public static final String LOG_TAG = ExplodeImpl.class.getSimpleName().toUpperCase();
    private static final float FRAME_DURATION = 0.1f;
    public static final float EXPLODE_DAMPING = 0.01f;
    protected Animation animation;
    @Getter
    private Vector2 position;
    @Getter
    private boolean active = false;
    private Sound sound;
    private Texture texture;
    private Circle bounds;
    private float stateTime = 0f;
    private int MAX_FRAMES = 73;
    private int frames = 0;
    private float explodeSpeed = 0f;
    private float traectory;

    public ExplodeImpl(AssetManager assets, int srcWidth, int srcHeight) {
        this.texture = assets.get(EXPLOSION, Texture.class);
        this.sound = assets.get(EXPLOSION_SND, Sound.class);
        position = new Vector2(0, 0);
        bounds = new Circle(new Vector2(), srcWidth / 2);
        Array<TextureRegion> regions = textureToRegions(texture, srcWidth, srcHeight);

        animation = new Animation(FRAME_DURATION, regions);
    }

    @Override
    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        bounds.set(calculateCenter(bounds.radius), bounds.radius);
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        position.x += traectory;
        batch.draw(frame, position.x, position.y);
        frames++;
        if (frames >= MAX_FRAMES) {
            reset();
        }
        position.y -= explodeSpeed;
        if(explodeSpeed > 0) explodeSpeed -= EXPLODE_DAMPING;
    }

    @Override
    public void reset() {
        active = false;
        frames = 0;
        Gdx.app.debug(LOG_TAG, "Removed - " + this.toString());
    }

    @Override
    public void start(Vector2 position, float asteroidSpeed, float asteroidTraectory) {
        active = true;
        this.position = position;
        this.explodeSpeed = asteroidSpeed;
        this.traectory = asteroidTraectory;
        bounds.set(calculateCenter(bounds.radius), bounds.radius);
        stateTime = 0f;
        sound.play();
        Gdx.app.debug(LOG_TAG, "Started - " + this.toString());
    }

    public void debugBounds(ShapeRenderer renderer) {
        renderer.setColor(Color.MAGENTA);
        renderer.circle(bounds.x, bounds.y, bounds.radius);
    }

    public Vector2 calculateCenter(float radius) {
        return new Vector2(position.x + radius, position.y + radius);
    }
}

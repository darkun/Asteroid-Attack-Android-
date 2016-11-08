package com.darkun.objects.asteroid;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Array;

import static com.darkun.AsteroidAttack.SCREEN_HEIGHT;
import static com.darkun.AsteroidAttack.SCREEN_WIDTH;
import static com.darkun.objects.Utils.textureToRegions;

/**
 * @author Gavrilov E. <mr.jerik@gmail.com>
 * @since 04.11.16
 */
public class AsteroidImpl implements Asteroid {
    protected static float FRAME_DURATION = 0.1f;

    private Vector2 position;
    protected Animation animation;
    private float stateTime = 0f;

    public AsteroidImpl(Texture texture, int srcWidth, int srcHeight) {
        calculatePosition(srcWidth);
        Gdx.app.debug(this.getClass().getSimpleName(), this.toString());

        Array<TextureRegion> regions = textureToRegions(texture, srcWidth, srcHeight);
        if (MathUtils.randomBoolean())
            regions.reverse();

        animation = new Animation(FRAME_DURATION, regions);
    }

    @Override
    public void draw(Batch batch) {
        stateTime += Gdx.graphics.getDeltaTime();
        // TODO need to update
        position.y--;
        TextureRegion frame = animation.getKeyFrame(stateTime, true);
        batch.draw(frame, position.x, position.y);
    }

    @Override
    public void reset() {
    }

    private void calculatePosition(int textureWidth) {
        float maxWidth = Integer.valueOf(SCREEN_WIDTH - textureWidth).floatValue();
        position = new Vector2(MathUtils.random(maxWidth), SCREEN_HEIGHT);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("AsteroidImpl{");
        sb.append("position=").append(position);
        sb.append(", animation=").append(animation);
        sb.append(", stateTime=").append(stateTime);
        sb.append('}');
        return sb.toString();
    }
}
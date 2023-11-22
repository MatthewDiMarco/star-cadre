package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.GameObject;

public class ParallaxHandler implements GameObject {

    public static final float MAX_HORIZONTAL_SPEED = 28f;
    public static final float MIN_HORIZONTAL_SPEED = -MAX_HORIZONTAL_SPEED;
    public static final float MAX_VERTICAL_SPEED = 48f;
    public static final float MIN_VERTICAL_SPEED = 24f;

    private final float[][] backgroundOffsets = { {0, 0}, {0, 0}, {0, 0} }; // [image][x/y toggle]
    private final TextureRegion[] scrollingBackgrounds;
    private final float worldWidth;
    private final float worldHeight;
    public float verticalSpeed;
    public float horizontalSpeed;
    public float scrollSpeedOffset;

    public ParallaxHandler(
        Texture[] backgrounds,
        float worldWidth,
        float worldHeight) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.verticalSpeed = MIN_VERTICAL_SPEED;
        this.horizontalSpeed = 0f;
        this.scrollSpeedOffset = 0f;

        int bgWidth = backgrounds[0].getWidth()*3;
        int bgHeight = backgrounds[0].getHeight()*2;

        scrollingBackgrounds = new TextureRegion[backgrounds.length];
        for (int ii = 0; ii < backgrounds.length; ii++) {
            backgrounds[ii].setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            scrollingBackgrounds[ii] = new TextureRegion(
                backgrounds[ii], 0, 0, bgWidth, bgHeight);
        }
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        accelerateHorizontal(-(Math.signum(horizontalSpeed) / 5f));
        scrollSpeedOffset = Math.min(gameHandler.getWaveNumber() * 2, MAX_VERTICAL_SPEED);
        int base = backgroundOffsets.length * 2;
        for (int ii = 0; ii < backgroundOffsets.length; ii++) {
            backgroundOffsets[ii][0] += deltaTime * (verticalSpeed + scrollSpeedOffset) / base;
            backgroundOffsets[ii][1] += deltaTime * horizontalSpeed / base;
            base = base / 2;
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        for (int ii = 0; ii < backgroundOffsets.length; ii++) {
            if (backgroundOffsets[ii][0] > worldHeight) {
                backgroundOffsets[ii][0] = 0;
            }
            if (backgroundOffsets[ii][1] > worldWidth * 2 || backgroundOffsets[ii][1] < 0) {
                backgroundOffsets[ii][1] = worldWidth;
            }
            batch.draw(
                scrollingBackgrounds[ii],
                -backgroundOffsets[ii][1],
                -backgroundOffsets[ii][0],
                worldWidth * 3,
                worldHeight * 2);
        }
        batch.flush();
    }

    public void accelerateHorizontal(float magnitude) {
        horizontalSpeed = Math.min(Math.max(
            horizontalSpeed + magnitude, MIN_HORIZONTAL_SPEED), MAX_HORIZONTAL_SPEED);
    }
}

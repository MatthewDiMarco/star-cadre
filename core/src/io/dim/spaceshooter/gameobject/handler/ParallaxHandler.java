package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.GameObject;

public class ParallaxHandler implements GameObject {

    public static final float MIN_SCROLL_SPEED = 12f;
    public static final float MAX_SCROLL_SPEED = 96f;

    private final float[][] backgroundOffsets = { {0, 0}, {0, 0}, {0, 0} }; // [image][x/y toggle]
    private final TextureRegion[] scrollingBackgrounds;
    private final float worldWidth;
    private final float worldHeight;
    private final boolean verticalRepeat;
    private final boolean horizontalRepeat;
    private float scrollSpeed;

    public ParallaxHandler(
        Texture[] backgrounds,
        float worldWidth,
        float worldHeight,
        boolean verticalRepeat,
        boolean horizontalRepeat) {
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        this.verticalRepeat = verticalRepeat;
        this.horizontalRepeat = horizontalRepeat;

        int bgWidth = horizontalRepeat ? backgrounds[0].getWidth()*2 : backgrounds[0].getWidth();
        int bgHeight = verticalRepeat ? backgrounds[0].getHeight()*2 : backgrounds[0].getHeight();

        scrollingBackgrounds = new TextureRegion[backgrounds.length];
        for (int ii = 0; ii < backgrounds.length; ii++) {
            backgrounds[ii].setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
            scrollingBackgrounds[ii] = new TextureRegion(
                backgrounds[ii], 0, 0, bgWidth, bgHeight);
        }

        this.scrollSpeed = 0f;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        scrollSpeed = Math.min(gameHandler.getWaveNumber() + MIN_SCROLL_SPEED, MAX_SCROLL_SPEED);
        int base = backgroundOffsets.length * 2;
        for (int ii = 0; ii < backgroundOffsets.length; ii++) {
            backgroundOffsets[ii][0] += deltaTime * scrollSpeed / base;
            backgroundOffsets[ii][1] += deltaTime * scrollSpeed / base;
            base = base / 2;
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        for (int ii = 0; ii < backgroundOffsets.length; ii++) {
            if (verticalRepeat && backgroundOffsets[ii][0] > worldHeight) {
                backgroundOffsets[ii][0] = 0;
            }
            if (horizontalRepeat && backgroundOffsets[ii][1] > worldWidth) {
                backgroundOffsets[ii][1] = 0;
            }
            batch.draw(
                scrollingBackgrounds[ii],
                horizontalRepeat ? -backgroundOffsets[ii][1] : 0,
                verticalRepeat ? -backgroundOffsets[ii][0] : 0,
                horizontalRepeat ? worldWidth * 2 : worldWidth,
                verticalRepeat ? worldHeight * 2 : worldHeight);
        }
    }
}

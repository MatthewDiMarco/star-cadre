package io.dim.spaceshooter.handler;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.ApplicationObject;

public class ParallaxHandler implements ApplicationObject {

    public static final float MAX_SCROLL_SPEED = 32f;

    private final float[][] backgroundOffsets = { {0, 0}, {0, 0}, {0, 0} }; // [image][x/y toggle]
    private final TextureRegion[] scrollingBackgrounds;
    private final float worldWidth;
    private final float worldHeight;
    private final boolean verticalRepeat;
    private final boolean horizontalRepeat;

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
    }

    // TODO allow for toggling speed of x/y scroll
    @Override
    public void update(float deltaTime) {
        int base = backgroundOffsets.length * 2;
        for (int ii = 0; ii < backgroundOffsets.length; ii++) {
            backgroundOffsets[ii][0] += deltaTime * MAX_SCROLL_SPEED / base;
            backgroundOffsets[ii][1] += deltaTime * MAX_SCROLL_SPEED / base;
            base = base / 2;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
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

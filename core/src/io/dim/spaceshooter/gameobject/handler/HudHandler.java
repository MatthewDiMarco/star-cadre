package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Align;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.util.MathUtils;
import java.util.Locale;

public class HudHandler implements GameObject {

    public static final float SHAKE_DURATION = 0.25f;

    private final float hudColLeft, hudColRight, hudRowTop;
    private final BitmapFont font;
    private final TextureRegion shipIcon;
    private final int iconWidth, iconHeight;

    private int scoreDisplay, playerHp;
    private float timerShake = SHAKE_DURATION;

    public HudHandler(
        FreeTypeFontGenerator fontGenerator,
        Rectangle hudBoundary,
        TextureRegion shipIcon) {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);
        this.font = fontGenerator.generateFont(fontParameter);
        this.font.getData().setScale(0.08f);

        float hudMargin = font.getCapHeight() / 2;
        this.hudColLeft = hudMargin;
        this.hudColRight = hudBoundary.width - hudMargin;
        this.hudRowTop = hudBoundary.height - hudMargin;

        this.shipIcon = shipIcon;
        this.iconWidth = 5;
        this.iconHeight = 5;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        playerHp = gameHandler.playerRef.hp;
        timerShake = Math.min(SHAKE_DURATION, timerShake + deltaTime);
        if (gameHandler.playerRef.timerLastHit == 0 && playerHp > 0) {
            timerShake = 0f;
        }
        if (scoreDisplay < gameHandler.score) {
            scoreDisplay++;
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        boolean shake = timerShake - SHAKE_DURATION < 0;
        float xShake = shake ? MathUtils.random.nextFloat() / 2 : 0; // TODO put in step
        float yShake = shake ? MathUtils.random.nextFloat() / 2 : 0;

        font.draw(batch, String.format(Locale.getDefault(), "$ %d", scoreDisplay),
            hudColLeft + xShake, hudRowTop + yShake, 0,
            Align.left, false);

        font.draw(batch, String.format(Locale.getDefault(), "%d ", playerHp),
            (hudColRight - iconWidth) + xShake,
            hudRowTop + yShake, 0,
            Align.right, false);

        Color cc = batch.getColor();
        batch.setColor(cc.r, cc.g, cc.b, 0.5f);
        batch.draw(shipIcon,
            (hudColRight - iconWidth) + xShake,
            (hudRowTop - iconHeight) + yShake,
            iconWidth, iconHeight);
        batch.setColor(cc.r, cc.g, cc.b, 1f);
    }
}

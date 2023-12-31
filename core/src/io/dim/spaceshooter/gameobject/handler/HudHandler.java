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
import io.dim.spaceshooter.helper.MathUtils;
import java.util.Locale;

public class HudHandler implements GameObject {

    public static final float SHAKE_DURATION = 0.25f;

    private final float hudColLeft, hudColRight, hudRowTop, hudRowSecond;
    private final int iconWidth, iconHeight;
    private final BitmapFont font;
    private final TextureRegion shipIcon;

    private int scoreDisplay, highscoreDisplay, playerHp;
    private float xShake, yShake;
    private float timerShake = SHAKE_DURATION;

    public HudHandler(
        FreeTypeFontGenerator fontGenerator,
        TextureRegion shipIcon,
        Rectangle hudBoundary) {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);
        this.font = fontGenerator.generateFont(fontParameter);
        this.font.setUseIntegerPositions(false);
        this.font.getData().setScale(0.08f);
        this.shipIcon = shipIcon;

        float hudMargin = font.getCapHeight() / 4;
        this.hudColLeft = hudMargin;
        this.hudColRight = hudBoundary.width - hudMargin;
        this.hudRowTop = hudBoundary.height - hudMargin;
        this.hudRowSecond = hudBoundary.height - font.getCapHeight() - hudMargin * 2;
        this.iconWidth = 5;
        this.iconHeight = 5;

        this.xShake = 0f;
        this.yShake = 0f;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        playerHp = gameHandler.playerRef.hp;
        timerShake = Math.min(SHAKE_DURATION, timerShake + deltaTime);

        boolean shake = timerShake - SHAKE_DURATION < 0;
        xShake = shake ? MathUtils.random.nextFloat() / 2 : 0;
        yShake = shake ? MathUtils.random.nextFloat() / 2 : 0;

        if (gameHandler.playerRef.timerLastHit == 0 && playerHp > 0) {
            timerShake = 0f;
        }

        if (scoreDisplay < gameHandler.score) {
            scoreDisplay++;
        }
        if (highscoreDisplay < gameHandler.score) {
            highscoreDisplay++;
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        font.setColor(Color.GOLD);
        font.draw(batch, String.format(Locale.getDefault(), "$ %d", highscoreDisplay),
            hudColLeft + xShake, hudRowTop + yShake, 0,
            Align.left, false);

        font.setColor(Color.WHITE);
        font.draw(batch, String.format(Locale.getDefault(), "$ %d", scoreDisplay),
            hudColLeft + xShake, hudRowSecond + yShake, 0,
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

    public void setScores(int high) {
        scoreDisplay = 0;
        highscoreDisplay = high;
    }
}

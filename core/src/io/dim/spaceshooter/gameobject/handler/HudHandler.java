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
import java.util.Locale;

public class HudHandler implements GameObject {

    private BitmapFont font;
    private int scoreDisplay, scoreActual, playerHp;
    private float hudVerticalMargin, hudLeftX, hudRightX, hudRow1Y, hudRow2Y, hudSectionWidth;
    private TextureRegion shipIcon;

    public HudHandler(
        FreeTypeFontGenerator fontGenerator,
        Rectangle hudBoundary,
        TextureRegion shipIcon) {
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        this.shipIcon = shipIcon;
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);
        font = fontGenerator.generateFont(fontParameter);
        font.getData().setScale(0.08f);
        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = hudBoundary.width * 2 / 3 - hudLeftX;
        hudRow1Y = hudBoundary.height - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = hudBoundary.width / 3;

        scoreDisplay = 0;
        scoreActual = 0;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        scoreActual = gameHandler.score;
        playerHp = gameHandler.playerRef.hp;
        if (scoreDisplay < scoreActual) {
            scoreDisplay++;
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%d", scoreDisplay),
            hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);

        Color cc = batch.getColor();
        batch.setColor(cc.r, cc.g, cc.b, 0.25f);
        batch.draw(shipIcon, hudRightX + 20, hudRow1Y - 5, 4, 4);
        batch.setColor(cc.r, cc.g, cc.b, 1f);

        font.draw(batch, String.format(Locale.getDefault(), "%d", playerHp),
            hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }
}

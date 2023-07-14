package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class Explosion {

    private Animation<TextureRegion> explosionAnimation;
    private float explosionTimer;
    private Rectangle boundingBox;

    public Explosion(Texture texture, Rectangle boundingBox, float totalAnimationTime) {
        this.boundingBox = boundingBox; // TODO do we really want to do all this for every explosion?
        TextureRegion[][] textureRegion2D = TextureRegion.split(texture, 64, 64);
        TextureRegion[] textureRegion1D = new TextureRegion[16];
        int index = 0;
        for (int ii = 0; ii < 4; ii++) {
            for (int jj = 0; jj < 4; jj++) {
                 textureRegion1D[index] = textureRegion2D[ii][jj];
                 index++;
            }
        }

        explosionAnimation = new Animation<TextureRegion>(totalAnimationTime / 16, textureRegion1D);
        explosionTimer = 0;
    }

    public void update(float deltaTime) {
        explosionTimer += deltaTime;
    }

    public void draw(SpriteBatch batch) {
        batch.draw(
            explosionAnimation.getKeyFrame(explosionTimer),
            boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
    }

    public boolean isFinished() {
        return explosionAnimation.isAnimationFinished(explosionTimer);
    }

}

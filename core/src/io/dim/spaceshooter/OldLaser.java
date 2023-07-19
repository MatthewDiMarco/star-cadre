package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public class OldLaser {
    float movementSpeed;
    Rectangle boundingBox;
    TextureRegion textureRegion;

    public OldLaser(float xPos, float yPos, float width, float height,
        float movementSpeed, TextureRegion textureRegion) {
        this.movementSpeed = movementSpeed;
        this.boundingBox = new Rectangle(xPos, yPos, width, height);
        this.textureRegion = textureRegion;
    }

    public void draw(Batch batch) {
        batch.draw(textureRegion,
            boundingBox.x - boundingBox.width / 2,
            boundingBox.y, boundingBox.width, boundingBox.height);
    }
}

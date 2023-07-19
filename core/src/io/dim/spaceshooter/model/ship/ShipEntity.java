package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.model.Entity;

public abstract class ShipEntity extends Entity {

    // TODO more fields to come
    public float movementSpeed;
    public TextureRegion shipTexture;

    public ShipEntity(float xOrigin, float yOrigin, float width, float height, float movementSpeed,
        TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height);
        this.movementSpeed = movementSpeed;
        this.shipTexture = shipTexture;
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
            shipTexture,
            boundingBox.x,
            boundingBox.y,
            boundingBox.width,
            boundingBox.height);
    }
}

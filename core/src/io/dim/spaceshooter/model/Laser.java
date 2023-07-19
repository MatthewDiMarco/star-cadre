package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Laser extends Entity {

    public int direction;
    protected final TextureRegion laserTexture;

    public Laser(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int direction, TextureRegion laserTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.direction = direction;
        this.laserTexture = laserTexture;
    }

    @Override
    public void step(World world, float deltaTime) {
        boundingBox.y += (movementSpeed * deltaTime) * Math.signum(direction);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
            laserTexture,
            boundingBox.x, boundingBox.y,
            boundingBox.width, boundingBox.height);
    }
}

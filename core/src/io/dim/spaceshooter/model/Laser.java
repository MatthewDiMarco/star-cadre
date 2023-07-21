package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Laser extends Entity {

    public int direction; // TODO: direction vector
    public int strength;

    protected final TextureRegion laserTexture;

    public Laser(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int direction, int strength, TextureRegion laserTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.direction = direction;
        this.strength = strength;
        this.laserTexture = laserTexture;
    }

    @Override
    public void step(World world, float deltaTime) {
        hitBox.y += (movementSpeed * deltaTime) * Math.signum(direction);
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
            laserTexture,
            hitBox.x, hitBox.y,
            hitBox.width, hitBox.height);
    }
}

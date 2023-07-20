package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.model.Entity;
import io.dim.spaceshooter.model.World;

public abstract class ShipEntity extends Entity {

    public float fireRate;
    protected float timeSinceLastShot;
    protected final TextureRegion shipTexture;

    public ShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, float fireRate, TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.shipTexture = shipTexture;
        this.fireRate = fireRate;
        this.timeSinceLastShot = 0;
    }

    @Override
    public void step(World world, float deltaTime) {
        this.timeSinceLastShot = Math.min(fireRate, timeSinceLastShot + deltaTime);
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

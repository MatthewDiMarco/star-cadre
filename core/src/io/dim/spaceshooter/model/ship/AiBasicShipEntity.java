package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.model.World;
import io.dim.spaceshooter.util.EntityUtils;

public class AiBasicShipEntity extends ShipEntity {

    public float directionChangeFrequency;
    protected Vector2 travelPoint;
    protected float timeSinceLastDirectionChange;

    public AiBasicShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int hp, float fireRate, float directionChangeFrequency, TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp, fireRate, shipTexture);
        this.directionChangeFrequency = directionChangeFrequency;
        this.travelPoint = new Vector2(xOrigin, yOrigin);
        this.timeSinceLastDirectionChange = 0;
    }

    @Override
    public void step(World world, float deltaTime) {
        super.step(world, deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            EntityUtils.randomizePoint(travelPoint,
                world.width/5, (int)(world.height/1.5f),
                (int)(world.width/1.25f), (int)(world.height/1.1f));
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }

        float[] boundaryDistances = EntityUtils
            .calcBoundaryDistances(this.hitBox, world.width, world.height);
        boundaryDistances[2] = (float)world.height/2 - hitBox.y; // half screen

        fireLaser(world);
        this.translate(
            travelPoint,
            movementSpeed * deltaTime,
            1f,
            boundaryDistances);
    }

    protected void fireLaser(World world) {
        if (timeSinceLastShot - fireRate >= 0) {
            world.alienLasers.add(world.entityFactory.createAlienLaser(
                hitBox.x + hitBox.width * 0.5f,
                hitBox.y + hitBox.height * 0.1f));
            timeSinceLastShot = 0;
        }
    }
}

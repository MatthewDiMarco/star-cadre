package io.dim.spaceshooter.entity.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.handler.EntityHandler;
import io.dim.spaceshooter.util.EntityUtils;

public class AiBasicShipEntity extends ShipEntity {

    public float directionChangeFrequency;
    protected Vector2 travelPoint;
    protected float timeSinceLastDirectionChange;

    public AiBasicShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int hp, float invulnerabilityTime, float fireRate,
        TextureRegion shipTexture, float directionChangeFrequency) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp, invulnerabilityTime, fireRate,
            shipTexture);
        this.directionChangeFrequency = directionChangeFrequency;
        this.travelPoint = new Vector2(xOrigin, yOrigin);
        this.timeSinceLastDirectionChange = 0;
    }

    @Override
    public void onStep(EntityHandler entityHandler, float deltaTime) {
        super.onStep(entityHandler, deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            EntityUtils.randomizePoint(travelPoint,
                (int)(entityHandler.boundary.width/5),
                (int)(entityHandler.boundary.height/1.5f),
                (int)(entityHandler.boundary.width/1.25f),
                (int)(entityHandler.boundary.height/1.1f));
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }

        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox,
            entityHandler.boundary.width,
            entityHandler.boundary.height);
        boundaryDistances[2] = entityHandler.boundary.height/2 - hitBox.y; // half screen

        fireLaser(entityHandler);
        this.translate(
            travelPoint,
            movementSpeed * deltaTime,
            1f,
            boundaryDistances);
    }

    protected void fireLaser(EntityHandler entityHandler) {
        if (timeSinceLastShot - fireRate >= 0) {
            entityHandler.lasers.add(entityHandler.factory.createAlienLaser(
                hitBox.x + hitBox.width * 0.5f,
                hitBox.y + hitBox.height * 0.1f));
            timeSinceLastShot = 0;
        }
    }
}

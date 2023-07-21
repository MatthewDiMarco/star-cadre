package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.model.EntityManager;
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
    public void step(EntityManager entityManager, float deltaTime) {
        super.step(entityManager, deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            EntityUtils.randomizePoint(travelPoint,
                (int)(entityManager.boundary.width/5),
                (int)(entityManager.boundary.height/1.5f),
                (int)(entityManager.boundary.width/1.25f),
                (int)(entityManager.boundary.height/1.1f));
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }

        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox,
            entityManager.boundary.width,
            entityManager.boundary.height);
        boundaryDistances[2] = entityManager.boundary.height/2 - hitBox.y; // half screen

        fireLaser(entityManager);
        this.translate(
            travelPoint,
            movementSpeed * deltaTime,
            1f,
            boundaryDistances);
    }

    protected void fireLaser(EntityManager entityManager) {
        if (timeSinceLastShot - fireRate >= 0) {
            entityManager.lasers.add(entityManager.factory.createAlienLaser(
                hitBox.x + hitBox.width * 0.5f,
                hitBox.y + hitBox.height * 0.1f));
            timeSinceLastShot = 0;
        }
    }
}

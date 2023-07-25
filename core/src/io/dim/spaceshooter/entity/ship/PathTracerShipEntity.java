package io.dim.spaceshooter.entity.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.handler.EntityHandler;
import io.dim.spaceshooter.util.EntityUtils;

public class PathTracerShipEntity extends ShipEntity {
    public final Vector2[] path;
    public int currPointIdx = 0;

    public PathTracerShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int hp, float invulnerabilityTime, float fireRate,
        TextureRegion shipTexture, Vector2[] path) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp, invulnerabilityTime, fireRate,
            shipTexture);
        this.path = path;
    }

    @Override
    public void onStep(EntityHandler entityHandler, float deltaTime) {
        super.onStep(entityHandler, deltaTime);

        fireLaser(entityHandler);

        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox, entityHandler.boundary.width, entityHandler.boundary.height);

        Vector2 entityCentrePoint = getCenterPoint();
        float distanceToCurrPoint = path[currPointIdx].dst(entityCentrePoint);
        if (Float.compare(distanceToCurrPoint, 0.5f) < 0)
            currPointIdx = (currPointIdx + 1) % path.length;
        this.translate(
            path[currPointIdx],
            movementSpeed * deltaTime,
            0f,
            boundaryDistances);
    }

    @Override
    public void onFireLaser(EntityHandler entityHandler) {
        entityHandler.lasers.add(entityHandler.factory.createAlienLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height * 0.1f));
    }
}

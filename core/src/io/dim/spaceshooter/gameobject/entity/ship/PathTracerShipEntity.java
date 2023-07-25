package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.util.EntityUtils;

public class PathTracerShipEntity extends ShipEntity {

    public final float xOrigin;
    public final float yOrigin;
    public final Vector2[] path;
    public int currPointIdx = 0;

    public PathTracerShipEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed, int hp,
        float firingCooldownDuration,
        float invulnerabilityDuration,
        TextureRegion shipTexture,
        Vector2[] path) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp,
            firingCooldownDuration, invulnerabilityDuration, shipTexture);
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.path = path;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        super.onStep(gameHandler, deltaTime);

        fireLaser(gameHandler);

        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox, gameHandler.boundary.width, gameHandler.boundary.height);

        Vector2 entityCentrePoint = getCenterPoint(); // TODO creating a new vector every time??
        Vector2 targetPoint = new Vector2(
            path[currPointIdx].x + xOrigin,
            path[currPointIdx].y + yOrigin);
        float distanceToCurrPoint = targetPoint.dst(entityCentrePoint);
        if (Float.compare(distanceToCurrPoint, 1f) < 0)
            currPointIdx = (currPointIdx + 1) % path.length;
        this.translate(
            targetPoint,
            movementSpeed * deltaTime,
            0f,
            boundaryDistances);
    }

    @Override
    public void onFireLaser(GameHandler gameHandler) {
        gameHandler.lasers.add(gameHandler.factory.createAlienLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height * 0.1f));
    }
}

package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class PathTracerShipEntity extends ShipEntity {

    public final float xOrigin;
    public final float yOrigin;
    public int currPointIdx = 0;
    public final Vector2[] path;

    public PathTracerShipEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed, int hp,
        float laserCooldownDuration,
        int laserStrength, int laserPerShot,
        float laserBarrelWidth,
        float laserMovementSpeed,
        float invulnerabilityDuration,
        TextureRegion shipTexture,
        Vector2[] path) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp,
            laserCooldownDuration, laserStrength, laserPerShot,
            laserBarrelWidth, laserMovementSpeed, invulnerabilityDuration, shipTexture);
        this.xOrigin = xOrigin;
        this.yOrigin = yOrigin;
        this.path = path;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        super.onStep(gameHandler, deltaTime);
        fireLaser(gameHandler);

        // path traversal
        Vector2 entityCentrePoint = getCenterPoint(); // TODO creating a new vector every time??
        Vector2 targetPoint = new Vector2(
            path[currPointIdx].x, path[currPointIdx].y);
        if (Math.abs(targetPoint.x - entityCentrePoint.x) < 1 &&
            Math.abs(targetPoint.y - entityCentrePoint.y) < 1) {
            currPointIdx = (currPointIdx + 1) % path.length;
        }

        // movement
        this.translate(targetPoint, movementSpeed * deltaTime, 0f);
        if (hitBox.y < -20) {
            disposable = true;
        }

        // collision detection
        if (gameHandler.playerRef.intersects(this)) {
            gameHandler.playerRef.hit(1);
        }
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        super.onDestroy(gameHandler);
        gameHandler.rollRandomPickup(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2);
    }

    @Override
    public LaserEntity getBaseLaser(GameHandler gameHandler) {
        return gameHandler.factory.createAlienLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height * 1.1f);
    }
}

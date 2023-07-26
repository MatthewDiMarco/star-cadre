package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.util.EntityUtils;
import java.util.Arrays;

public class PathTracerShipEntity extends ShipEntity {

    public final float xOrigin;
    public final float yOrigin;
    public int currPointIdx = 0;
    public final Vector2[] path;

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

        // path traversal
        Vector2 entityCentrePoint = getCenterPoint(); // TODO creating a new vector every time??
        Vector2 targetPoint = new Vector2(
            path[currPointIdx].x + xOrigin, path[currPointIdx].y + yOrigin);
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
    public void onFireLaser(GameHandler gameHandler) {
        gameHandler.lasers.add(gameHandler.factory.createAlienLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height * 0.1f));
    }
}

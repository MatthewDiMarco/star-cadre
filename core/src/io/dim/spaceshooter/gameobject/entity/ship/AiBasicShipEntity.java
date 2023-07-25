package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.util.EntityUtils;

public class AiBasicShipEntity extends ShipEntity {

    public float directionChangeFrequency;
    protected Vector2 travelPoint;
    protected float timeSinceLastDirectionChange;

    public AiBasicShipEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed, int hp,
        float firingCooldownDuration,
        float invulnerabilityDuration,
        TextureRegion shipTexture,
        float directionChangeFrequency) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp,
            firingCooldownDuration, invulnerabilityDuration, shipTexture);
        this.directionChangeFrequency = directionChangeFrequency;
        this.travelPoint = new Vector2(xOrigin, yOrigin);
        this.timeSinceLastDirectionChange = 0;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        super.onStep(gameHandler, deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            EntityUtils.randomizePoint(travelPoint,
                (int)(gameHandler.boundary.width/5),
                (int)(gameHandler.boundary.height/1.5f),
                (int)(gameHandler.boundary.width/1.25f),
                (int)(gameHandler.boundary.height/1.1f));
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }

        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox,
            gameHandler.boundary.width,
            gameHandler.boundary.height);
        boundaryDistances[2] = gameHandler.boundary.height/2 - hitBox.y; // half screen

        fireLaser(gameHandler);
        this.translate(
            travelPoint,
            movementSpeed * deltaTime,
            1f,
            boundaryDistances);
    }

    @Override
    public void onFireLaser(GameHandler gameHandler) {
        gameHandler.lasers.add(gameHandler.factory.createAlienLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height * 0.1f));
    }
}

package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.SpaceShooterGame;
import io.dim.spaceshooter.model.World;

public class AiBasicShipEntity extends ShipEntity {

    public final static double PI_TIMES_TWO = 6.283185; // 2*PI

    public float directionChangeFrequency;
    protected Vector2 travelPoint;
    protected float timeSinceLastDirectionChange;

    public AiBasicShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, float fireRate, float directionChangeFrequency, TextureRegion shipTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, fireRate, shipTexture);
        this.directionChangeFrequency = directionChangeFrequency;
        this.travelPoint = new Vector2(xOrigin, yOrigin);
        this.timeSinceLastDirectionChange = 0;
    }

    @Override
    public void step(World world, float deltaTime) {
        super.step(world, deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeTravelPoint(world.width, world.height);
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }

        float[] limits = findScreenLimits(world.width, world.height);
        float upLimit = limits[0];
        float rightLimit = limits[1];
        float downLimit = (float)world.height/2 - boundingBox.y; // half screen
        float leftLimit = limits[3];

        Vector2 shipCentre = new Vector2(
            this.boundingBox.x + this.boundingBox.width / 2,
            this.boundingBox.y + this.boundingBox.height / 2);

        float distanceToPoint = travelPoint.dst(shipCentre);

        final float MOVEMENT_THRESHOLD = 1f;
        if (distanceToPoint > MOVEMENT_THRESHOLD) { // TODO this is duped (player class)
            float xTouchDiff = travelPoint.x - shipCentre.x;
            float yTouchDiff = travelPoint.y - shipCentre.y;
            float xMove = xTouchDiff / distanceToPoint * movementSpeed * deltaTime;
            float yMove = yTouchDiff / distanceToPoint * movementSpeed * deltaTime;

            this.translate(
                Math.min(Math.max(xMove, leftLimit), rightLimit),
                Math.min(Math.max(yMove, downLimit), upLimit));
        }

        fireLaser(world);
    }

    protected void fireLaser(World world) {
        if (timeSinceLastShot - fireRate >= 0) {
            world.alienLasers.add(world.entityFactory.createAlienLaser(
                boundingBox.x + boundingBox.width * 0.5f,
                boundingBox.y + boundingBox.height * 0.1f));
            timeSinceLastShot = 0;
        }
    }

    private void randomizeTravelPoint(int worldWidth, int worldHeight) {
        travelPoint.x = SpaceShooterGame.random.nextInt(
            (int)(worldWidth/1.25f) - worldWidth/5) + (float)worldWidth/5;
        travelPoint.y = SpaceShooterGame.random.nextInt(
            (int)(worldHeight/1.1f) - (int)(worldHeight/1.5f)) + (float)worldHeight/1.5f;
    }
}

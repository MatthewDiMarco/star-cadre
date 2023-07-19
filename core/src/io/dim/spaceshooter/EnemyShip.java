package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

public class EnemyShip extends Ship { // TODO extension bad

    private final double PI_TIMES_TWO = 6.283185; // 2*PI
    Vector2 directionVector;
    float timeSinceLastDirectionChange = 0;
    float directionChangeFrequency = 0.75f;

    public EnemyShip(float movementSpeed, int shield, float xCenter, float yCenter, float width,
        float height, float laserWidth, float laserHeight, float laserMovementSpeed,
        float timeBetweenShots, TextureRegion shipTexture,
        TextureRegion shieldTexture,
        TextureRegion laserTexture) {
        super(movementSpeed, shield, xCenter, yCenter, width, height, laserWidth, laserHeight,
            laserMovementSpeed, timeBetweenShots, shipTexture, shieldTexture, laserTexture);
        directionVector = new Vector2(0, -1);
    }

    public Vector2 getDirectionVector() {
        return directionVector;
    }

    private void randomizeDirectionVector() {
        double bearing = SpaceShooterGame.random.nextDouble() * PI_TIMES_TWO;
        directionVector.x = (float)Math.sin(bearing);
        directionVector.y = (float)Math.cos(bearing);
    }

    @Override
    public void update(float deltaTime) {
        super.update(deltaTime);
        timeSinceLastDirectionChange += deltaTime;
        if (timeSinceLastDirectionChange > directionChangeFrequency) {
            randomizeDirectionVector();
            timeSinceLastDirectionChange -= directionChangeFrequency;
        }
    }

    @Override
    public OldLaser[] fireLasers() {
        OldLaser[] lasers = new OldLaser[2];
        lasers[0] = new OldLaser(
            boundingBox.x + boundingBox.width * 0.18f,
            boundingBox.y - laserHeight,
            laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        lasers[1] = new OldLaser(boundingBox.x + boundingBox.width * 0.82f,
            boundingBox.y - laserHeight,
            laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        timeSinceLastShot = 0;
        return lasers;
    }

    @Override
    public void draw(Batch batch) {
        batch.draw(shipTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shieldHealth > 0) {
            batch.draw(shieldTexture, boundingBox.x,
                boundingBox.y - boundingBox.height * 0.35f,
                boundingBox.width, boundingBox.height);
        }
    }
}

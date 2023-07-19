package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class PlayerShip extends Ship { // TODO extension bad

    int lives;

    public PlayerShip(float movementSpeed, int shield, float xCenter, float yCenter, float width,
        float height, float laserWidth, float laserHeight, float laserMovementSpeed,
        float timeBetweenShots, TextureRegion shipTexture,
        TextureRegion shieldTexture,
        TextureRegion laserTexture) {
        super(movementSpeed, shield, xCenter, yCenter, width, height, laserWidth, laserHeight,
            laserMovementSpeed, timeBetweenShots, shipTexture, shieldTexture, laserTexture);
        lives = 3;
    }

    @Override
    public OldLaser[] fireLasers() {
        OldLaser[] lasers = new OldLaser[2];
        lasers[0] = new OldLaser(
            boundingBox.x + boundingBox.width * 0.07f,
            boundingBox.y + boundingBox.height * 0.45f,
            laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        lasers[1] = new OldLaser(
            boundingBox.x + boundingBox.width * 0.93f,
            boundingBox.y + boundingBox.height * 0.45f,
            laserWidth, laserHeight, laserMovementSpeed, laserTexture);
        timeSinceLastShot = 0;
        return lasers;
    }
}

package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class Ship { // abstract = disgusting TODO fix
    float movementSpeed;
    int shieldHealth;
    Rectangle boundingBox;
    float laserWidth, laserHeight;
    float laserMovementSpeed;
    float timeBetweenShots; // TODO effectively, the fire rate
    float timeSinceLastShot = 0;
    TextureRegion shipTexture, shieldTexture, laserTexture;

    public Ship(float movementSpeed, int shield, float xCenter, float yCenter, float width, float height,
        float laserWidth, float laserHeight, float laserMovementSpeed, float timeBetweenShots,
        TextureRegion shipTexture, TextureRegion shieldTexture, TextureRegion laserTexture) {
        this.movementSpeed = movementSpeed;
        this.shieldHealth = shield;
        this.boundingBox = new Rectangle(xCenter - width / 2, yCenter - height / 2, width, height);
        this.laserWidth = laserWidth;
        this.laserHeight = laserHeight;
        this.laserMovementSpeed = laserMovementSpeed;
        this.timeBetweenShots = timeBetweenShots;
        this.shipTexture = shipTexture;
        this.shieldTexture = shieldTexture;
        this.laserTexture = laserTexture;
    }

    public void update(float deltaTime) {
        timeSinceLastShot += deltaTime;
    }

    public boolean canFireLaser() {
        return timeSinceLastShot - timeBetweenShots >= 0;
    }

    public abstract OldLaser[] fireLasers(); // gross!

    public boolean intersects(Rectangle otherRectangle) {
        return boundingBox.overlaps(otherRectangle);
    }

    public boolean hitAndCheckDestroyed(OldLaser laser) {
        if (shieldHealth > 0) {
            shieldHealth--;
            return false;
        }
        return true;
    }

    public void translate(float xChange, float yChange) {
        boundingBox.setPosition(boundingBox.x + xChange, boundingBox.y + yChange);
    }

    public void draw(Batch batch) {
        batch.draw(shipTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        if (shieldHealth > 0) {
            batch.draw(shieldTexture, boundingBox.x, boundingBox.y, boundingBox.width, boundingBox.height);
        }
    }
}

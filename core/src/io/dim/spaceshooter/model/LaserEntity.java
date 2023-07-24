package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.EntityManager;
import io.dim.spaceshooter.model.ship.ShipEntity;

public class LaserEntity extends Entity {

    public enum LaserTarget { PLAYER, ALIEN }

    public int direction; // TODO: direction vector
    public int strength;
    public LaserTarget laserTarget;

    protected final TextureRegion laserTexture;

    public LaserEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int direction, int strength,
        LaserTarget target, TextureRegion laserTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.direction = direction;
        this.strength = strength;
        this.laserTarget = target;
        this.laserTexture = laserTexture;
    }

    @Override
    public void step(EntityManager entityManager, float deltaTime) {
        hitBox.y += (movementSpeed * deltaTime) * Math.signum(direction);

        if (hitBox.y > entityManager.boundary.height || hitBox.y < -10) {
            disposable = true;
        }

        if (laserTarget == LaserTarget.ALIEN) {
            for (ShipEntity ship : entityManager.ships) {
                if (!ship.invulnerable &&
                    ship != entityManager.playerRef &&
                    ship.intersects(this)) {
                    ship.hit(this);
                    entityManager.particleManager.createLaserBlueEffect(
                        hitBox.x + hitBox.width / 2,
                        hitBox.y + hitBox.height);
                    disposable = true;
                }
            }
        } else {
            if (!entityManager.playerRef.invulnerable &&
                entityManager.playerRef.intersects(this)) {
                entityManager.playerRef.hit(this);
                entityManager.particleManager.createLaserRedEffect(
                    hitBox.x + hitBox.width / 2,
                    hitBox.y);
                disposable = true;
            }
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(
            laserTexture,
            hitBox.x, hitBox.y,
            hitBox.width, hitBox.height);
    }
}

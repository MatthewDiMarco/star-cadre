package io.dim.spaceshooter.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.handler.EntityHandler;
import io.dim.spaceshooter.entity.ship.ShipEntity;
import io.dim.spaceshooter.handler.ParticleHandler;

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
    public void onStep(EntityHandler entityHandler, float deltaTime) {
        hitBox.y += (movementSpeed * deltaTime) * Math.signum(direction);

        if (hitBox.y > entityHandler.boundary.height || hitBox.y < -10) {
            disposable = true;
        }

        if (laserTarget == LaserTarget.ALIEN) {
            for (ShipEntity ship : entityHandler.ships) {
                if (!ship.invulnerable &&
                    ship != entityHandler.playerRef &&
                    ship.intersects(this)) {
                    ship.hit(this);
                    disposable = true;
                }
            }
        } else {
            if (entityHandler.playerRef.intersects(this)) {
                entityHandler.playerRef.hit(this);
            }
        }
    }

    @Override
    public void onDeath(EntityHandler entityHandler, ParticleHandler particleHandler) {
        float xx = hitBox.x + hitBox.width / 2;
        float yy = direction > 0 ? hitBox.y + hitBox.height : hitBox.y;

        if (laserTarget == LaserTarget.ALIEN) {
            particleHandler.createLaserBlueEffect(xx, yy);
        } else {
            particleHandler.createLaserRedEffect(xx, yy);
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        batch.draw(
            laserTexture,
            hitBox.x, hitBox.y,
            hitBox.width, hitBox.height);
    }
}

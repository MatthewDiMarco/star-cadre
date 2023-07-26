package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;

public class LaserEntity extends Entity {

    public enum LaserTarget { PLAYER, ALIEN }

    public int direction; // TODO: direction vector
    public int strength;
    public LaserTarget laserTarget;

    protected final TextureRegion laserTexture;

    public LaserEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed,
        int direction, int strength,
        LaserTarget target, TextureRegion laserTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.direction = direction;
        this.strength = strength;
        this.laserTarget = target;
        this.laserTexture = laserTexture;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        // movement
        hitBox.y += (movementSpeed * deltaTime) * Math.signum(direction);
        if (hitBox.y < -10 || hitBox.y > gameHandler.boundary.height) {
            disposable = true;
        }

        // collision detection
        if (laserTarget == LaserTarget.ALIEN) {
            for (ShipEntity ship : gameHandler.ships) {
                if (!ship.invulnerabilityEnabled &&
                    ship != gameHandler.playerRef &&
                    ship.intersects(this)) {
                    ship.hit(this.strength);
                    disposable = true;
                }
            }
        } else {
            if (gameHandler.playerRef.intersects(this)) {
                gameHandler.playerRef.hit(this.strength);
                disposable = true;
            }
        }
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        float xx = hitBox.x + hitBox.width / 2;
        float yy = direction > 0 ? hitBox.y + hitBox.height : hitBox.y;

        if (laserTarget == LaserTarget.ALIEN) {
            gameHandler.particleHandler.createLaserBlueEffect(xx, yy);
        } else {
            gameHandler.particleHandler.createLaserRedEffect(xx, yy);
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

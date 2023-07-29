package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;

public class LaserEntity extends Entity {

    public enum LaserTarget { PLAYER, ALIEN }

    public float launchSpeedOffset;
    public int strength;
    public Vector2 direction;
    public LaserTarget laserTarget;

    protected final TextureRegion laserTexture;

    public LaserEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed,
        int direction, int strength, float horizontalOffset,
        LaserTarget target, TextureRegion laserTexture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.launchSpeedOffset = movementSpeed * 3;
        this.strength = strength;
        this.direction = new Vector2(horizontalOffset, Math.signum(direction));
        this.laserTarget = target;
        this.laserTexture = laserTexture;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        // movement
        hitBox.x += ((movementSpeed + launchSpeedOffset) * deltaTime) * direction.x;
        hitBox.y += ((movementSpeed + launchSpeedOffset) * deltaTime) * direction.y;
        if (launchSpeedOffset > 0.1f) launchSpeedOffset = launchSpeedOffset / 2;
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
                    gameHandler.score += 15;
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
        float yy = direction.y > 0 ? hitBox.y + hitBox.height : hitBox.y;

        if (laserTarget == LaserTarget.ALIEN) {
            gameHandler.particleHandler.createLaserBlueEffect(xx, yy);
        } else {
            gameHandler.particleHandler.createLaserRedEffect(xx, yy);
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        float rotation = direction.angleDeg() +
            (Math.signum(direction.y) > 0 ? -90 : 90);
        batch.draw(
            laserTexture,
            hitBox.x, hitBox.y,
            0, 0,
            hitBox.width,
            hitBox.height,
            1, 1,
            rotation);
    }
}

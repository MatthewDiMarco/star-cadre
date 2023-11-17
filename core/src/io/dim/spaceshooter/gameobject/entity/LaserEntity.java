package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.helper.Assets;

public class LaserEntity extends Entity {

    public enum LaserTarget { PLAYER, ALIEN }

    public float launchSpeedOffset;
    public int strength;
    public boolean armourPiercing;
    public Vector2 direction;
    public LaserTarget laserTarget;
    public TextureRegion laserTexture;

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        // movement
        hitBox.x += ((movementSpeed + launchSpeedOffset) * deltaTime) * direction.x;
        hitBox.y += ((movementSpeed + launchSpeedOffset) * deltaTime) * direction.y;
        if (launchSpeedOffset > 0.1f) launchSpeedOffset = launchSpeedOffset / 2;
        if (hitBox.y < -5 || hitBox.y > gameHandler.boundary.height) {
            disposable = true;
        }

        // collision detection
        if (laserTarget == LaserTarget.ALIEN) {
            for (ShipEntity ship : gameHandler.ships) {
                if (!ship.invulnerabilityEnabled &&
                    ship != gameHandler.playerRef &&
                    ship.hitBox.y <= gameHandler.boundary.height - 5 &&
                    ship.intersects(this)) {
                    ship.hit(this.strength);
                    if (ship.hp <= 0) gameHandler.score += 5 * ship.hpMax;
                    if (!armourPiercing) disposable = true;
                }
            }
        } else {
            if (!gameHandler.playerRef.invulnerabilityEnabled &&
                gameHandler.playerRef.intersects(this) &&
                gameHandler.playerRef.hp > 0) {
                gameHandler.playerRef.hit(this.strength);
                if (!armourPiercing) disposable = true;
            }
        }
    }

    @Override
    public void onCreate(
        float xOrigin, float yOrigin, float width, float height, Assets assets) {
        TextureRegion texture = assets.textureAtlas.findRegion("laserBlue16");
        this.disposable = false;
        this.hitBox = new Rectangle(xOrigin, yOrigin, width, height);
        this.movementSpeed = 25f;
        this.launchSpeedOffset = movementSpeed * 3;
        this.strength = 1;
        this.armourPiercing = false;
        this.direction = new Vector2(0f, 1);
        this.laserTarget = LaserTarget.ALIEN;
        this.laserTexture = texture;
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        float xx = hitBox.x + hitBox.width / 2;
        float yy = direction.y > 0 ? hitBox.y + hitBox.height : hitBox.y;

        if (laserTarget == LaserTarget.ALIEN) {
            gameHandler.particleHandler.createLaserSmokeBlueEffect(xx, yy);
        } else {
            gameHandler.particleHandler.createLaserSmokeRedEffect(xx, yy);
        }

        gameHandler.factory.freeLaser(this);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        float rotation = direction.angleDeg() - 90;
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

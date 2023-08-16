package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.factory.EntityFactory.LaserType;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.helper.Assets;
import io.dim.spaceshooter.helper.MathUtils;
import java.util.Locale;

public class EnemyShipEntity extends ShipEntity {

    public int currPointIdx = 0;
    public boolean mirrorPath;
    public Vector2[] path;

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        super.onStep(gameHandler, deltaTime);
        fireLaser(gameHandler);

        // path traversal
        Vector2 entityCentrePoint = getCenterPoint(); // TODO creating a new vector every time??
        Vector2 targetPoint = new Vector2(
            path[currPointIdx].x + (mirrorPath ? (gameHandler.boundary.width / 2 - path[currPointIdx].x) * 2 : 0),
            path[currPointIdx].y);
        if (Math.abs(targetPoint.x - entityCentrePoint.x) < 1 &&
            Math.abs(targetPoint.y - entityCentrePoint.y) < 1) {
            currPointIdx = (currPointIdx + 1) % path.length;
        }

        // movement
        this.translate(targetPoint, movementSpeed * deltaTime, 0f);
        if (hitBox.y < -20) {
            disposable = true;
        }

        // collision detection
        if (gameHandler.playerRef.intersects(this)) {
            gameHandler.playerRef.hit(1);
        }
    }

    @Override
    public void onCreate(
        float xOrigin, float yOrigin, float width, float height, Assets assets) {
        this.hitBox = new Rectangle(
            xOrigin - width / 2, yOrigin - height / 2, width, height);
        this.movementSpeed = 60f;
        this.hp = 1;
        this.hpMax = hp;
        this.lasersEnabled = false;
        this.laserArmourPiercing = false;
        this.laserStrength = 0;
        this.laserPerShot = 0;
        this.laserBarrelWidth = 0f;
        this.laserMovementSpeed = 0f;
        this.laserCooldownDuration = 0f;
        this.invulnerabilityEnabled = false;
        this.invulnerabilityDuration = 0f;
        this.timerLastLaser = 0f;
        this.timerLastHit = 0f;
        this.alpha = 1f;
        this.shipTexture = assets.textureAtlas.findRegion(
            String.format(
                Locale.getDefault(),
                "enemyRed%d",
                MathUtils.random.nextInt(4 - 1) + 1));
        this.laserSound = assets.laserSound2;
        this.explosionSound = assets.explosionSound1;
        this.hurtSound = assets.hurtSound1;
        this.mirrorPath = false;
        this.path = null;
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        super.onDestroy(gameHandler);
        gameHandler.rollRandomPickup(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2);
    }

    @Override
    public LaserEntity getBaseLaser(GameHandler gameHandler) {
        return gameHandler.factory.createLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height * 1.1f,
            LaserType.ENEMY);
    }
}

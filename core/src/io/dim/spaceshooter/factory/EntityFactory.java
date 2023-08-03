package io.dim.spaceshooter.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity.LaserTarget;
import io.dim.spaceshooter.gameobject.entity.PickupEntity;
import io.dim.spaceshooter.gameobject.entity.ship.EnemyShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity.TouchScreen;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.helper.PathAtlas;

public class EntityFactory {

    public enum LaserType { PLAYER, ENEMY }
    public enum PickupType { SNIPER, SHOTGUN, MINIGUN }
    public enum EnemyType { SNAKE, INVADER }

    private final TextureAtlas textureAtlas;
    private final PathAtlas pathAtlas;

    public EntityFactory(TextureAtlas textureAtlas, PathAtlas pathAtlas) {
        this.textureAtlas = textureAtlas;
        this.pathAtlas = pathAtlas;
    }

    public PlayerShipEntity createPlayer(float xOrigin, float yOrigin, TouchScreen touchScreen) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 8, 8, 48f, 3, 1f,
            textureAtlas.findRegion("playerShip3_blue"), touchScreen);
    }

    public ShipEntity createEnemy(
        float xOrigin, float yOrigin, boolean mirrorPath, EnemyType enemyType) {
        switch (enemyType) {
            case SNAKE:
                return new EnemyShipEntity(
                    xOrigin, yOrigin, 6, 6, 82f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, textureAtlas.findRegion("enemyRed1"),
                    pathAtlas.paths.get(0), mirrorPath);
            case INVADER:
                return new EnemyShipEntity(
                    xOrigin, yOrigin, 6, 6, 48f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, textureAtlas.findRegion("enemyRed2"),
                    pathAtlas.paths.get(1), mirrorPath);
            default:
                throw new RuntimeException("Error creating entity");
        }
    }

    public PickupEntity createPickup(
        float xOrigin, float yOrigin, PickupType pickupType) {
        float speed = 32f;
        TextureRegion pickupTexture = textureAtlas.findRegion("powerupYellow_bolt");
        switch (pickupType) {
            case SNIPER:
                return new PickupEntity(xOrigin, yOrigin,
                    (float) pickupTexture.getRegionWidth() / 8,
                    (float) pickupTexture.getRegionHeight() / 8,
                    speed, pickupTexture,
                    playerShip -> {
                        playerShip.laserPerShot = 3;
                        playerShip.laserBarrelWidth = 0.05f;
                        playerShip.laserMovementSpeed = 350f;
                        playerShip.laserCooldownDuration = 0.35f;
                        playerShip.laserArmourPiercing = true;
                    });
            case SHOTGUN:
                return new PickupEntity(xOrigin, yOrigin,
                    (float)pickupTexture.getRegionWidth()/8,
                    (float)pickupTexture.getRegionHeight()/8,
                    speed, pickupTexture,
                    playerShip -> {
                        playerShip.laserPerShot = 4;
                        playerShip.laserBarrelWidth = 0.5f;
                        playerShip.laserCooldownDuration = 0.5f;
                    });
            case MINIGUN:
                return new PickupEntity(xOrigin, yOrigin,
                    (float)pickupTexture.getRegionWidth()/8,
                    (float)pickupTexture.getRegionHeight()/8,
                    speed, pickupTexture,
                    playerShip -> {
                        playerShip.laserMovementSpeed = 300f;
                        playerShip.laserCooldownDuration = 0.1f;
                    });
            default:
                throw new RuntimeException("Error creating entity");
        }
    }

    public LaserEntity createLaser(
        float xOrigin, float yOrigin, LaserType laserType) {
        TextureRegion blueLaserTexture = textureAtlas.findRegion("laserBlue16");
        TextureRegion redLaserTexture = textureAtlas.findRegion("laserRed06");
        switch (laserType) {
            case PLAYER:
                return new LaserEntity(xOrigin, yOrigin,
                    (float)(blueLaserTexture.getRegionWidth()/8),
                    (float)(blueLaserTexture.getRegionHeight()/8),
                    0f, 1, 0, 0f,
                    LaserTarget.ALIEN, blueLaserTexture);
            case ENEMY:
                return new LaserEntity(xOrigin, yOrigin,
                    (float)(redLaserTexture.getRegionWidth()/8),
                    (float)(redLaserTexture.getRegionHeight()/8),
                    102f, -1, 1, 0f,
                    LaserTarget.PLAYER, redLaserTexture);
            default:
                throw new RuntimeException("Error creating entity");
        }
    }
}

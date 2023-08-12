package io.dim.spaceshooter.factory;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.AsteroidEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity.LaserTarget;
import io.dim.spaceshooter.gameobject.entity.PickupEntity;
import io.dim.spaceshooter.gameobject.entity.ship.EnemyShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity.TouchScreen;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.helper.MathUtils;
import io.dim.spaceshooter.helper.PathAtlas;
import java.util.Locale;

public class EntityFactory {

    public enum LaserType { PLAYER, ENEMY }
    public enum PickupType { MAGNUM, SHOTGUN, MINIGUN, BOOMSTICK }
    public enum EnemyType { SNAKE, INVADER, DRAGON, TANK }

    private final TextureAtlas textureAtlas;
    private final PathAtlas pathAtlas;
    private final Sound laserSound1;
    private final Sound laserSound2;
    private final Sound explosionSound1;

    public EntityFactory(TextureAtlas textureAtlas, PathAtlas pathAtlas,
        Sound laserSound1, Sound laserSound2, Sound explosionSound1) {
        this.textureAtlas = textureAtlas;
        this.pathAtlas = pathAtlas;
        this.laserSound1 = laserSound1;
        this.laserSound2 = laserSound2;
        this.explosionSound1 = explosionSound1;
    }

    public PlayerShipEntity createPlayer(float xOrigin, float yOrigin, TouchScreen touchScreen) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 7, 7, 52f, 3, 1f,
            textureAtlas.findRegion("playerShip3_blue"),
            laserSound1, explosionSound1, touchScreen);
    }

    public ShipEntity createEnemy(
        float xOrigin, float yOrigin, boolean mirrorPath, EnemyType enemyType) {
        switch (enemyType) {
            case SNAKE:
                return new EnemyShipEntity(
                    xOrigin, yOrigin, 5, 5, 60f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, textureAtlas.findRegion("enemyRed1"),
                    laserSound2, explosionSound1, pathAtlas.paths.get(0), mirrorPath);
            case INVADER:
                return new EnemyShipEntity(
                    xOrigin, yOrigin, 6, 6, 36f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, textureAtlas.findRegion("enemyRed2"),
                    laserSound2, explosionSound1, pathAtlas.paths.get(1), mirrorPath);
            case DRAGON:
                return new EnemyShipEntity(
                    xOrigin, yOrigin, 6, 6, 48f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, textureAtlas.findRegion("enemyRed3"),
                    laserSound2, explosionSound1, pathAtlas.paths.get(2), mirrorPath);
            case TANK:
                return new EnemyShipEntity(
                    xOrigin, yOrigin, 10, 10, 24f, 5,
                    1f, 1, 2,
                    0.5f, 50f,
                    0.1f, textureAtlas.findRegion("enemyRed4"),
                    laserSound2, explosionSound1, pathAtlas.paths.get(3), mirrorPath);
            default:
                throw new RuntimeException("Error creating entity");
        }
    }

    public PickupEntity createPickup(
        float xOrigin, float yOrigin, PickupType pickupType) {
        float speed = 32f;
        TextureRegion pickupTexture = textureAtlas.findRegion("powerupYellow_bolt");
        switch (pickupType) {
            case MAGNUM:
                return new PickupEntity(xOrigin, yOrigin,
                    (float) pickupTexture.getRegionWidth() / 8,
                    (float) pickupTexture.getRegionHeight() / 8,
                    speed, pickupTexture,
                    playerShip -> {
                        playerShip.laserPerShot = 3;
                        playerShip.laserBarrelWidth = 0.05f;
                        playerShip.laserMovementSpeed = 250f;
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
            case BOOMSTICK:
                return new PickupEntity(xOrigin, yOrigin,
                    (float)pickupTexture.getRegionWidth()/8,
                    (float)pickupTexture.getRegionHeight()/8,
                    speed, pickupTexture,
                    playerShip -> {
                        playerShip.laserPerShot = 8;
                        playerShip.laserBarrelWidth = 1f;
                        playerShip.laserCooldownDuration = 0.75f;
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
                    25f, 1, 0, 0f,
                    LaserTarget.ALIEN, blueLaserTexture);
            case ENEMY:
                return new LaserEntity(xOrigin, yOrigin,
                    (float)(redLaserTexture.getRegionWidth()/6),
                    (float)(redLaserTexture.getRegionHeight()/6),
                    102f, -1, 1, 0f,
                    LaserTarget.PLAYER, redLaserTexture);
            default:
                throw new RuntimeException("Error creating entity");
        }
    }

    public AsteroidEntity createAsteroid(float xOrigin, float yOrigin) {
        final float minSize = 6f;
        final float maxSize = 8f;
        final float asteroidSize = MathUtils.random.nextInt((int)(maxSize - minSize)) + minSize;
        return new AsteroidEntity(
            xOrigin, yOrigin, asteroidSize, asteroidSize, 48f,
            textureAtlas.findRegion(String.format(Locale.getDefault(),
                "meteorBrown_big%d", MathUtils.random.nextInt(4 - 1) + 1)));
    }
}

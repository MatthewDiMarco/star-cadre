package io.dim.spaceshooter.factory;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Pool;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity.LaserTarget;
import io.dim.spaceshooter.gameobject.entity.PickupEntity;
import io.dim.spaceshooter.gameobject.entity.ship.EnemyShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity.TouchScreen;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.helper.Assets;
import io.dim.spaceshooter.helper.Paths;
import io.dim.spaceshooter.helper.Paths.PathIndex;

public class EntityFactory {

    public enum LaserType { PLAYER, ENEMY }
    public enum PickupType { MAGNUM, SHOTGUN, MINIGUN, BOOMSTICK }
    public enum EnemyType { SNAKE, INVADER, TANK }

    private final Paths paths;
    private final Assets assets;
    private final Pool<EnemyShipEntity> enemyShipPool;
    private final Pool<LaserEntity> laserPool;
    private final Pool<PickupEntity> pickupPool;

    public EntityFactory(Paths paths, Assets assets) {
        this.paths = paths;
        this.assets = assets;
        this.enemyShipPool = new Pool<EnemyShipEntity>() {
            @Override
            protected EnemyShipEntity newObject() { return new EnemyShipEntity(); }
        };
        this.laserPool = new Pool<LaserEntity>() {
            @Override
            protected LaserEntity newObject() { return new LaserEntity(); }
        };
        this.pickupPool = new Pool<PickupEntity>() {
            @Override
            protected PickupEntity newObject() { return new PickupEntity(); }
        };
    }

    public PlayerShipEntity createPlayer(float xOrigin, float yOrigin, TouchScreen touchScreen) {
        PlayerShipEntity playerShip = new PlayerShipEntity();
        playerShip.onCreate(xOrigin, yOrigin, 7, 7, assets);
        playerShip.touchScreen = touchScreen;
        return playerShip;
    }

    public ShipEntity createEnemy(
        float xOrigin, float yOrigin, boolean mirrorPath, EnemyType enemyType) {
        TextureRegion altTexture;
        EnemyShipEntity enemyShip = enemyShipPool.obtain();
        switch (enemyType) {
            case SNAKE:
                enemyShip.onCreate(xOrigin, yOrigin, 6, 6, assets);
                enemyShip.mirrorPath = mirrorPath;
                enemyShip.path = paths.array[PathIndex.SNAKE.ordinal()];
                enemyShip.movementSpeed = 64f;
                return enemyShip;
            case INVADER:
                enemyShip.onCreate(xOrigin, yOrigin, 6, 6, assets);
                enemyShip.mirrorPath = mirrorPath;
                enemyShip.path = paths.array[PathIndex.INVADER.ordinal()];
                enemyShip.movementSpeed = 48f;
                return enemyShip;
            case TANK:
                altTexture = assets.textureAtlas.findRegion("enemyRed4");
                enemyShip.onCreate(xOrigin, yOrigin, 10, 10, assets);
                enemyShip.mirrorPath = mirrorPath;
                enemyShip.path = paths.array[PathIndex.TANK.ordinal()];
                enemyShip.hp = 10;
                enemyShip.hpMax = enemyShip.hp;
                enemyShip.movementSpeed = 24f;
                enemyShip.lasersEnabled = true;
                enemyShip.laserPerShot = 2;
                enemyShip.laserBarrelWidth = 0.5f;
                enemyShip.laserStrength = 1;
                enemyShip.laserMovementSpeed = 50f;
                enemyShip.laserCooldownDuration = 1f;
                enemyShip.shipTexture = altTexture;
                return enemyShip;
            default:
                throw new RuntimeException("Error creating entity");
        }
    }

    public PickupEntity createPickup(
        float xOrigin, float yOrigin, PickupType pickupType) {
        PickupEntity pickup = pickupPool.obtain();
        pickup.onCreate(xOrigin, yOrigin, 4, 4, assets);
        switch (pickupType) {
            case MAGNUM:
                pickup.pickupMutator = playerShip -> {
                    playerShip.laserPerShot = 3;
                    playerShip.laserBarrelWidth = 0.05f;
                    playerShip.laserMovementSpeed = 250f;
                    playerShip.laserArmourPiercing = true;
                };
                return pickup;
            case SHOTGUN:
                pickup.pickupMutator = playerShip -> {
                    playerShip.laserPerShot = 4;
                    playerShip.laserBarrelWidth = 0.5f;
                    playerShip.laserCooldownDuration = 0.5f;
                };
                return pickup;
            case MINIGUN:
                pickup.pickupMutator = playerShip -> {
                    playerShip.laserMovementSpeed = 300f;
                    playerShip.laserCooldownDuration = 0.1f;
                };
                return pickup;
            case BOOMSTICK:
                pickup.pickupMutator = playerShip -> {
                    playerShip.laserPerShot = 8;
                    playerShip.laserBarrelWidth = 1f;
                    playerShip.laserCooldownDuration = 0.75f;
                };
                return pickup;
            default:
                throw new RuntimeException("Error creating entity");
        }
    }

    public LaserEntity createLaser(
        float xOrigin, float yOrigin, LaserType laserType) {
        TextureRegion laserTexture;
        LaserEntity laser = laserPool.obtain();
        switch (laserType) {
            case PLAYER:
                laserTexture = assets.textureAtlas.findRegion("laserBlue16");
                laser.onCreate(xOrigin, yOrigin,
                    (float)(laserTexture.getRegionWidth()/8),
                    (float)(laserTexture.getRegionHeight()/8),
                    assets);
                return laser;
            case ENEMY:
                laserTexture = assets.textureAtlas.findRegion("laserRed06");
                laser.onCreate(xOrigin, yOrigin,
                    (float)(laserTexture.getRegionWidth()/6),
                    (float)(laserTexture.getRegionHeight()/6),
                    assets);
                laser.direction = new Vector2(0f, -1);
                laser.laserTarget = LaserTarget.PLAYER;
                laser.laserTexture = laserTexture;
                return laser;
            default:
                throw new RuntimeException("Error creating entity");
        }
    }
}

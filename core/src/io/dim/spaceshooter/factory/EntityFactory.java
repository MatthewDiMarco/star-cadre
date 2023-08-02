package io.dim.spaceshooter.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity.LaserTarget;
import io.dim.spaceshooter.gameobject.entity.pickup.MinigunPickup;
import io.dim.spaceshooter.gameobject.entity.pickup.LaserPickup;
import io.dim.spaceshooter.gameobject.entity.pickup.Pickup;
import io.dim.spaceshooter.gameobject.entity.pickup.ShotgunPickup;
import io.dim.spaceshooter.gameobject.entity.ship.PathTracerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.util.MathUtils;

public class EntityFactory {

    public enum AlienType { SNAKE, INVADER }
    public enum PickupType { LASER, SHOTGUN, MINIGUN }

    public static final Vector2[] PATH_BLUEPRINT_SNAKE = {
        new Vector2(-0.2f, 1.1f),
        new Vector2(-0.2f, 0.9f),
        new Vector2(0.9f, 0.5f),
        new Vector2(0.1f, 0.05f),
        new Vector2(1.1f, 0.05f),
        new Vector2(1.1f, -1.0f),
    };

    public static final Vector2[] PATH_BLUEPRINT_INVADER = {
        new Vector2(0.1f, 0.9f),
        new Vector2(0.4f, 0.5f),
        new Vector2(0.1f, 0.5f),
        new Vector2(0.4f, 0.9f),
    };

    private final TextureRegion playerTexture;
    private final TextureRegion alienBasic1Texture;
    private final TextureRegion alienBasic2Texture;
    private final TextureRegion blueLaserTexture;
    private final TextureRegion alienLaserTexture;
    private final TextureRegion pickupTexture;

    private final Vector2[] pathInvaderLeft, pathInvaderRight;
    private final Vector2[] pathSnakeLeft, pathSnakeRight;

    public EntityFactory(TextureAtlas atlas, Rectangle entityBoundary) {
        this.playerTexture = atlas.findRegion("playerShip3_blue");
        this.alienBasic1Texture = atlas.findRegion("enemyRed1");
        this.alienBasic2Texture = atlas.findRegion("enemyRed2");
        this.blueLaserTexture = atlas.findRegion("laserBlue16");
        this.alienLaserTexture = atlas.findRegion("laserRed06");
        this.pickupTexture = atlas.findRegion("powerupYellow_bolt");

        this.pathInvaderLeft = MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_INVADER, entityBoundary.width, entityBoundary.height, false);
        this.pathInvaderRight = MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_INVADER, entityBoundary.width, entityBoundary.height, true);

        this.pathSnakeLeft = MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_SNAKE, entityBoundary.width, entityBoundary.height, false);
        this.pathSnakeRight = MathUtils.calcControlPointsFromBlueprint(
            PATH_BLUEPRINT_SNAKE, entityBoundary.width, entityBoundary.height, true);
    }

    public PlayerShipEntity createPlayer(
        float xOrigin,
        float yOrigin,
        Viewport viewportRef) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 8, 8, 32, 3,
            0.5f, 1, 1,
            0.15f, 100f,
            0.25f, playerTexture, viewportRef);
    }

    public PathTracerShipEntity createAlien(
        float xOrigin,
        float yOrigin,
        boolean mirror,
        AlienType alienType) {
        PathTracerShipEntity alien;
        switch (alienType) {
            case SNAKE:
                alien = new PathTracerShipEntity(
                    xOrigin, yOrigin, 6, 6, 52f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, alienBasic1Texture,
                    mirror ? pathSnakeRight : pathSnakeLeft);
                break;
            case INVADER:
                alien = new PathTracerShipEntity(
                    xOrigin, yOrigin, 6, 6, 48f, 1,
                    0f, 0, 0,
                    0f, 0f,
                    0f, alienBasic2Texture,
                    mirror ? pathInvaderRight : pathInvaderLeft);
                break;
            default:
                throw new RuntimeException("No alien available: " + alienType);
        }
        return alien;
    }

    public Pickup createPickup(
        float xOrigin,
        float yOrigin,
        PickupType pickupType) {
        Pickup pickup;
        final float pickupMovementSpeed = 48f;
        switch (pickupType) {
            case LASER:
                pickup = new LaserPickup(xOrigin, yOrigin,
                    (float)pickupTexture.getRegionWidth()/8,
                    (float)pickupTexture.getRegionHeight()/8,
                    pickupMovementSpeed, pickupTexture);
                break;
            case SHOTGUN:
                pickup = new ShotgunPickup(xOrigin, yOrigin,
                    (float)pickupTexture.getRegionWidth()/8,
                    (float)pickupTexture.getRegionHeight()/8,
                    pickupMovementSpeed, pickupTexture);
                break;
            case MINIGUN:
                pickup = new MinigunPickup(xOrigin, yOrigin,
                    (float)pickupTexture.getRegionWidth()/8,
                    (float)pickupTexture.getRegionHeight()/8,
                    pickupMovementSpeed, pickupTexture);
                break;
            default:
                throw new RuntimeException("No pickup available: " + pickupType);
        }
        return pickup;
    }

    public LaserEntity createBlankPlayerLaser(
        float xOrigin,
        float yOrigin) {
        return new LaserEntity(xOrigin, yOrigin,
            (float)(blueLaserTexture.getRegionWidth()/8),
            (float)(blueLaserTexture.getRegionHeight()/8),
            0f, 1, 0, 0f,
            LaserTarget.ALIEN, blueLaserTexture);
    }

    public LaserEntity createAlienLaser(
        float xOrigin,
        float yOrigin) {
        return new LaserEntity(xOrigin, yOrigin,
            (float)alienLaserTexture.getRegionWidth()/8,
            (float)alienLaserTexture.getRegionHeight()/8,
            102f, -1, 1, 0f,
            LaserTarget.PLAYER, alienLaserTexture);
    }
}

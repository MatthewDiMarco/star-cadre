package io.dim.spaceshooter.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity.LaserTarget;
import io.dim.spaceshooter.gameobject.entity.pickup.TestPickup;
import io.dim.spaceshooter.gameobject.entity.ship.PathTracerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.util.MathUtil;

public class EntityFactory {

    public static final float SPEED_SNAKE = 32f;

    public static final Vector2[] PATH_BLUEPRINT_SNAKE = {
        new Vector2(0, 0),
        new Vector2(10, -20),
        new Vector2(-10, -40),
        new Vector2(10, -60),
        new Vector2(-10, -80),
    };

    private final TextureRegion playerTexture;
    private final TextureRegion alienBasic1Texture;
    private final TextureRegion alienBasic3Texture;
    private final TextureRegion playerLaserTexture;
    private final TextureRegion alienLaserTexture;
    private final TextureRegion pickupTexture;

    private final Vector2[] pathSnake;

    public EntityFactory(TextureAtlas atlas) {
        this.playerTexture = atlas.findRegion("playerShip3_blue");
        this.alienBasic1Texture = atlas.findRegion("enemyRed1");
        this.alienBasic3Texture = atlas.findRegion("enemyRed2");
        this.playerLaserTexture = atlas.findRegion("laserBlue01");
        this.alienLaserTexture = atlas.findRegion("laserRed06");
        this.alienLaserTexture.flip(false, true);
        this.pickupTexture = atlas.findRegion("powerupYellow_bolt");

        this.pathSnake = MathUtil.calcCatmullRomPath(
            MathUtil.repeatControlPointsVertically(PATH_BLUEPRINT_SNAKE, -80, 1), 100);
    }

    public PlayerShipEntity createPlayer(
        float xOrigin,
        float yOrigin,
        Viewport viewportRef) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 8, 8, 64, 3,
            0.25f, 1, 1, 0.25f, 182f, 0f,
            0.2f, playerTexture, viewportRef);
    }

    public PathTracerShipEntity createAlienSnake(
        float xOrigin,
        float yOrigin) {
        return new PathTracerShipEntity( // TODO simplify with default constructors...
            xOrigin, yOrigin, 6, 6, SPEED_SNAKE, 1,
            0f, 0, 0, 0f, 0f, 0f,
            0.1f, alienBasic1Texture, pathSnake);
    }

    public LaserEntity createPlayerLaser(
        float xOrigin,
        float yOrigin) {
        return new LaserEntity(xOrigin, yOrigin,
            (float)(playerLaserTexture.getRegionWidth()/6),
            (float)(playerLaserTexture.getRegionHeight()/6),
            148f, 1, 1, 0f,
            LaserTarget.ALIEN, playerLaserTexture);
    }

    public LaserEntity createAlienLaser( // TODO needs some work...
        float xOrigin,
        float yOrigin) {
        return new LaserEntity(xOrigin, yOrigin,
            (float)alienLaserTexture.getRegionWidth()/8,
            (float)alienLaserTexture.getRegionHeight()/8,
            102f, -1, 1, 0f,
            LaserTarget.PLAYER, alienLaserTexture);
    }

    public TestPickup createTestPickup(
        float xOrigin,
        float yOrigin) {
        return new TestPickup(xOrigin, yOrigin,
            (float)pickupTexture.getRegionWidth()/8,
            (float)pickupTexture.getRegionHeight()/8,
            32f, pickupTexture);
    }
}

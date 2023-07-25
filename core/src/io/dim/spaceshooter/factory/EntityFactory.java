package io.dim.spaceshooter.factory;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity.LaserTarget;
import io.dim.spaceshooter.gameobject.entity.ship.AiBasicShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PathTracerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.util.MathUtil;

public class EntityFactory {

    public static final Vector2[] PATH_BLUEPRINT_FIG_EIGHT = {
        new Vector2(0, 0),
        new Vector2(-10, -10),
        new Vector2(-20, 0),
        new Vector2(-10, 10),
        new Vector2(10, -10),
        new Vector2(20, 0),
        new Vector2(10, 10),
    };

    private final TextureRegion playerTexture;
    private final TextureRegion alienBasicTexture;
    private final TextureRegion alienBasic2Texture;
    private final TextureRegion playerLaserTexture;
    private final TextureRegion alienLaserTexture;

    private final Vector2[] pathFigEight;

    public EntityFactory(TextureAtlas atlas) {
        this.playerTexture = atlas.findRegion("playerShip3_blue");
        this.alienBasicTexture = atlas.findRegion("enemyRed1");
        this.alienBasic2Texture = atlas.findRegion("enemyRed5");
        this.playerLaserTexture = atlas.findRegion("laserBlue01");
        this.alienLaserTexture = atlas.findRegion("laserRed06");
        this.alienLaserTexture.flip(false, true);
        this.pathFigEight = MathUtil.calcBSplinePath(PATH_BLUEPRINT_FIG_EIGHT, 100);
    }

    public PlayerShipEntity createPlayer(
        float xOrigin,
        float yOrigin,
        Viewport viewportRef) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 8, 8,
            64, 3, 0.15f, 0.5f,
            playerTexture, viewportRef);
    }

    public LaserEntity createPlayerLaser(
        float xOrigin,
        float yOrigin) {
        return new LaserEntity(xOrigin, yOrigin,
            (float)playerLaserTexture.getRegionWidth()/6,
            (float)playerLaserTexture.getRegionHeight()/6,
            182, 1, 1, LaserTarget.ALIEN,
            playerLaserTexture);
    }

    public LaserEntity createAlienLaser(
        float xOrigin,
        float yOrigin) {
        return new LaserEntity(xOrigin, yOrigin,
            (float)alienLaserTexture.getRegionWidth()/8,
            (float)alienLaserTexture.getRegionHeight()/8,
            48, -1, 1, LaserTarget.PLAYER, alienLaserTexture);
    }

    public AiBasicShipEntity createAlienBasic(
        float xOrigin,
        float yOrigin) {
        return new AiBasicShipEntity(
            xOrigin, yOrigin, 10, 10,
            24, 5, 0.75f, 0.1f,
            alienBasic2Texture, 1f);
    }

    public PathTracerShipEntity createAlienPathTracer(
        float xOrigin,
        float yOrigin) {
        return new PathTracerShipEntity(
            xOrigin, yOrigin, 6, 6,
            32, 1, 0f, 0.1f,
            alienBasicTexture, pathFigEight);
    }

}

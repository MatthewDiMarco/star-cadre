package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.model.LaserEntity;
import io.dim.spaceshooter.model.LaserEntity.LaserTarget;
import io.dim.spaceshooter.model.ship.AiBasicShipEntity;
import io.dim.spaceshooter.model.ship.PlayerShipEntity;

public class EntityFactory {

    private final TextureRegion playerTexture;
    private final TextureRegion alienBasicTexture;
    private final TextureRegion playerLaserTexture;
    private final TextureRegion alienLaserTexture;

    public EntityFactory(TextureAtlas atlas) {
        this.playerTexture = atlas.findRegion("playerShip3_blue");
        this.alienBasicTexture = atlas.findRegion("enemyRed1");
        this.playerLaserTexture = atlas.findRegion("laserBlue01");
        this.alienLaserTexture = atlas.findRegion("laserRed06");
        this.alienLaserTexture.flip(false, true);
    }

    public PlayerShipEntity createPlayer(
        float xOrigin,
        float yOrigin,
        Viewport viewportRef) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 10, 10,
            64, 3, 0.5f, 0.15f,
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
            xOrigin, yOrigin, 8, 8,
            24, 5, 0.1f, 0.75f,
            alienBasicTexture, 1f);
    }

}

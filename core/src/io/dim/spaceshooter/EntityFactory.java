package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.model.Laser;
import io.dim.spaceshooter.model.ship.AiBasicShipEntity;
import io.dim.spaceshooter.model.ship.PlayerShipEntity;

public class EntityFactory {

    private final TextureAtlas atlas;
    private TextureRegion playerTexture;
    private TextureRegion alienBasicTexture;
    private TextureRegion playerLaserTexture;
    private TextureRegion alienLaserTexture;

    public EntityFactory(TextureAtlas atlas) {
        this.atlas = atlas;
        this.playerTexture = atlas.findRegion("playerShip3_blue");
        this.alienBasicTexture = atlas.findRegion("enemyRed3");
        this.playerLaserTexture = atlas.findRegion("laserBlue01");
        this.alienLaserTexture = atlas.findRegion("laserRed01");
        this.alienLaserTexture.flip(false, true);
    }

    public PlayerShipEntity createPlayer(
        float xOrigin,
        float yOrigin,
        Viewport viewportRef) {
        return new PlayerShipEntity(
            xOrigin, yOrigin, 10, 10,
            64, 0.15f, playerTexture, viewportRef);
    }

    public Laser createPlayerLaser(
        float xOrigin,
        float yOrigin) {
        return new Laser(xOrigin, yOrigin,
            (float)playerLaserTexture.getRegionWidth()/6,
            (float)playerLaserTexture.getRegionHeight()/6,
            182, 1, playerLaserTexture);
    }

    public Laser createAlienLaser(
        float xOrigin,
        float yOrigin) {
        return new Laser(xOrigin, yOrigin,
            (float)alienLaserTexture.getRegionWidth()/6,
            (float)alienLaserTexture.getRegionHeight()/6,
            64, -1, alienLaserTexture);
    }

    public AiBasicShipEntity createAlienBasic(
        float xOrigin,
        float yOrigin) {
        return new AiBasicShipEntity(
            xOrigin, yOrigin, 8, 8,
            92, 0.5f, 1f, alienBasicTexture);
    }

}

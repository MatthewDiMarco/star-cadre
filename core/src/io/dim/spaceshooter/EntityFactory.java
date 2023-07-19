package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.model.Laser;
import io.dim.spaceshooter.model.ship.PlayerShipEntity;

public class EntityFactory {

    private final TextureAtlas atlas;

    public EntityFactory(TextureAtlas atlas) {
        this.atlas = atlas;
    }

    public PlayerShipEntity createPlayer(
        float xOrigin,
        float yOrigin,
        Viewport viewportRef) {
        TextureRegion playerTexture = atlas.findRegion("playerShip3_blue");
        return new PlayerShipEntity(
            xOrigin, yOrigin, 10, 10,
            64, 0.15f, playerTexture, viewportRef);
    }

    public Laser createPlayerLaser(
        float xOrigin,
        float yOrigin) {
        TextureRegion laserTexture = atlas.findRegion("laserBlue04");
        return new Laser(xOrigin, yOrigin,
            (float)laserTexture.getRegionWidth()/6,
            (float)laserTexture.getRegionHeight()/6,
            182, 1, laserTexture);
    }

}

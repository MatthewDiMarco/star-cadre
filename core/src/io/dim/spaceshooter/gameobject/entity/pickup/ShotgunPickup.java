package io.dim.spaceshooter.gameobject.entity.pickup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class ShotgunPickup extends Pickup {

    public int laserPerShotInc = 2;
    public float laserBarrelWidthInc = 0.2f;

    public ShotgunPickup(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, texture);
    }

    @Override
    public void onPickup(GameHandler gameHandler) {
        ShipEntity player = gameHandler.playerRef;
        player.laserPerShot += laserPerShotInc;
        player.laserBarrelWidth += laserBarrelWidthInc;
        timer = 5f;
        super.onPickup(gameHandler);
    }

    @Override
    public void onUndo(GameHandler gameHandler) {
        ShipEntity player = gameHandler.playerRef;
        player.laserPerShot -= laserPerShotInc;
        player.laserBarrelWidth -= laserBarrelWidthInc;
    }
}

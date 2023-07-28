package io.dim.spaceshooter.gameobject.entity.pickup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class MultiLaserPickup extends Pickup {

    public int laserPerShotInc = 1;

    public MultiLaserPickup(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, texture);
    }

    @Override
    public void onPickup(GameHandler gameHandler) {
        gameHandler.playerRef.laserPerShot += laserPerShotInc;
        super.onPickup(gameHandler);
    }

    @Override
    public void onUndo(GameHandler gameHandler) {
        gameHandler.playerRef.laserPerShot -= laserPerShotInc;
    }
}

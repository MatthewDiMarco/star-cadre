package io.dim.spaceshooter.gameobject.entity.pickup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class FireratePickup extends Pickup {
    // TODO stacking: maybe the rate of increase slows down? (rate delta is a function of current firerate...)

    public float laserCooldownDec = 0.15f;
    public float laserSpeedInc = 100f;

    public FireratePickup(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, texture);
    }

    @Override
    public void onPickup(GameHandler gameHandler) {
        gameHandler.playerRef.laserCooldownDuration -= laserCooldownDec;
        gameHandler.playerRef.laserSpeed += laserSpeedInc;
        super.onPickup(gameHandler);
    }

    @Override
    public void onUndo(GameHandler gameHandler) {
        gameHandler.playerRef.laserCooldownDuration += laserCooldownDec;
        gameHandler.playerRef.laserSpeed -= laserSpeedInc;
    }
}

package io.dim.spaceshooter.gameobject.entity.pickup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class MinigunPickup extends Pickup {

    public float laserCooldownDec = 0.15f;
    public float laserSpeedInc = 15f;

    public MinigunPickup(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, texture);
    }

    @Override
    public void onPickup(GameHandler gameHandler) {
        ShipEntity player = gameHandler.playerRef;
        player.laserCooldownDuration -= laserCooldownDec;
        player.laserMovementSpeed += laserSpeedInc;
        super.onPickup(gameHandler);
    }

    @Override
    public void onUndo(GameHandler gameHandler) {
        ShipEntity player = gameHandler.playerRef;
        player.laserCooldownDuration += laserCooldownDec;
        player.laserMovementSpeed -= laserSpeedInc;
    }
}

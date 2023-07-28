package io.dim.spaceshooter.gameobject.entity.pickup;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class TestPickup extends Pickup {

    public TestPickup(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed, texture);
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        if (activated) {
            gameHandler.playerRef.laserPerShot -= 1;
        }
    }

    @Override
    public void onPickup(GameHandler gameHandler) {
        PlayerShipEntity player = gameHandler.playerRef;
        player.laserPerShot += 1;
        gameHandler.particleHandler.createExplosionEffect(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2,
            Math.max(hitBox.width, hitBox.height) / 10);
    }
}

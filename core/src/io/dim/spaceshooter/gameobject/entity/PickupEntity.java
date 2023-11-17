package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.helper.Assets;

public class PickupEntity extends Entity {

    public boolean draw;
    public float timer;
    public PickupMutator pickupMutator;
    public TextureRegion pickupTexture;

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        if (gameHandler.playerRef.pickup == this) {
            // tick
            timer = Math.max(0f, timer - deltaTime);
            if (timer <= 0) {
                disposable = true;
            }
        } else {
            // movement
            hitBox.y -= (movementSpeed * deltaTime);
            if (hitBox.y < -10) {
                disposable = true;
            }

            // collision detection
            if (gameHandler.playerRef.intersects(this)) {
                PlayerShipEntity player = gameHandler.playerRef;
                player.setDefaultLaserState();
                if (player.pickup != null) player.pickup.disposable = true;
                player.pickup = this;

                draw = false;
                pickupMutator.onPickup(gameHandler.playerRef);

                gameHandler.particleHandler.createExplosionEffect(
                    hitBox.x + hitBox.width / 2,
                    hitBox.y + hitBox.height / 2,
                    Math.max(hitBox.width, hitBox.height) / 30);  // temp
            }
        }
    }

    @Override
    public void onCreate(
        float xOrigin, float yOrigin, float width, float height, Assets assets) {
        this.disposable = false;
        this.hitBox = new Rectangle(xOrigin, yOrigin, width, height);
        this.movementSpeed = 32f;
        this.draw = true;
        this.timer = 10f;
        this.pickupMutator = playerShip -> {};
        this.pickupTexture = assets.textureAtlas.findRegion("powerupYellow_bolt");
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        if (gameHandler.playerRef.pickup == this) {
            gameHandler.playerRef.pickup = null;
            gameHandler.playerRef.setDefaultLaserState();
        }
        gameHandler.factory.freePickup(this);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (draw) {
            batch.draw(
                pickupTexture,
                hitBox.x,
                hitBox.y,
                hitBox.width,
                hitBox.height);
        }
    }

    public interface PickupMutator {
        void onPickup(PlayerShipEntity playerShip);
    }
}

package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class PickupEntity extends Entity {

    public static final float DEFAULT_TIMER = 10f;
    public boolean draw;
    public float timer;
    public PickupMutator pickupMutator;

    protected TextureRegion texture;

    public PickupEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture, PickupMutator pickupMutator) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.draw = true;
        this.timer = DEFAULT_TIMER;
        this.pickupMutator = pickupMutator;
        this.texture = texture;
    }

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
    public void onDestroy(GameHandler gameHandler) {
        if (gameHandler.playerRef.pickup == this) {
            gameHandler.playerRef.pickup = null;
            gameHandler.playerRef.setDefaultLaserState();
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (draw) {
            batch.draw(
                texture,
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

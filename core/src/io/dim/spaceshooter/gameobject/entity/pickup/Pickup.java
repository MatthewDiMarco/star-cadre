package io.dim.spaceshooter.gameobject.entity.pickup;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.Entity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public abstract class Pickup extends Entity {

    public static final float DEFAULT_TIMER = 10f;
    public boolean activated;
    public float timer;

    protected TextureRegion texture;

    public Pickup(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.activated = false;
        this.timer = DEFAULT_TIMER;
        this.texture = texture;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        if (activated) {
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
                activated = true;
                onPickup(gameHandler);
            }
        }
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        if (activated) {
            onUndo(gameHandler);
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (!activated) {
            batch.draw(
                texture,
                hitBox.x,
                hitBox.y,
                hitBox.width,
                hitBox.height);
        }
    }

    public void onPickup(GameHandler gameHandler) {
        gameHandler.particleHandler.createExplosionEffect(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2,
            Math.max(hitBox.width, hitBox.height) / 30);  // temp
    }

    public abstract void onUndo(GameHandler gameHandler);
}

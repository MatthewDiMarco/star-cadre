package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class AsteroidEntity extends Entity {

    protected float rotation;
    protected TextureRegion texture;

    public AsteroidEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, TextureRegion texture) {
        super(xOrigin, yOrigin, width, height, movementSpeed);
        this.rotation = 0f;
        this.texture = texture;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        // movement
        rotation += 1f;
        hitBox.y -= movementSpeed * deltaTime;
        if (hitBox.y < -20) {
            disposable = true;
        }

        // collision detection
        if (!gameHandler.playerRef.invulnerabilityEnabled &&
            gameHandler.playerRef.intersects(this) &&
            gameHandler.playerRef.hp > 0) {
            gameHandler.playerRef.hit(1);
            disposable = true;
        }
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        gameHandler.particleHandler.createDebrisEffect(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2);
        gameHandler.particleHandler.createExplosionEffect(
            hitBox.x + hitBox.width / 2,
            hitBox.y + hitBox.height / 2,
            Math.max(hitBox.width, hitBox.height) / 20);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        batch.draw(
            texture,
            hitBox.x, hitBox.y,
            hitBox.width / 2,
            hitBox.height / 2,
            hitBox.width,
            hitBox.height,
            1, 1,
            rotation);
    }
}

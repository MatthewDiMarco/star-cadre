package io.dim.spaceshooter.entity.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.handler.EntityHandler;
import io.dim.spaceshooter.util.EntityUtils;

public class PlayerShipEntity extends ShipEntity {

    protected final Viewport viewportRef;

    public PlayerShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, int hp, float invulnerabilityTime, float fireRate,
        TextureRegion shipTexture, Viewport viewportRef) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp, invulnerabilityTime, fireRate,
            shipTexture);
        this.viewportRef = viewportRef;
    }

    @Override
    public void onStep(EntityHandler entityHandler, float deltaTime) {
        super.onStep(entityHandler, deltaTime);
        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox,
            entityHandler.boundary.width,
            entityHandler.boundary.height);

        // TODO movement speed would feel better with acceleration
        if (Gdx.input.isKeyPressed(Input.Keys.UP) ||
            Gdx.input.isKeyPressed(Input.Keys.W)) {
            this.translate(
                0f,
                movementSpeed * deltaTime,
                boundaryDistances);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.D)) {
            this.translate(
                movementSpeed * deltaTime,
                0,
                boundaryDistances);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
            Gdx.input.isKeyPressed(Input.Keys.S)) {
            this.translate(
                0f,
                -movementSpeed * deltaTime,
                boundaryDistances);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.A)) {
            this.translate(
                -movementSpeed * deltaTime,
                0f,
                boundaryDistances);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            fireLaser(entityHandler);
        }

        if (Gdx.input.isTouched()) {
            Vector2 touchPoint = viewportRef.unproject( // TODO replace w/ Lambda
                new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            fireLaser(entityHandler);
            this.translate(
                touchPoint,
                movementSpeed * deltaTime,
                0.5f,
                boundaryDistances);
        }
    }

    protected void fireLaser(EntityHandler entityHandler) {
        if (timeSinceLastShot - fireRate >= 0) {
            entityHandler.lasers.add(entityHandler.factory.createPlayerLaser(
                hitBox.x + hitBox.width * 0.5f,
                hitBox.y + hitBox.height * 1.1f));
            timeSinceLastShot = 0;
        }
    }
}

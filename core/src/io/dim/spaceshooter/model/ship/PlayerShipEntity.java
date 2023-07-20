package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.model.World;

public class PlayerShipEntity extends ShipEntity {

    protected final Viewport viewportRef;

    public PlayerShipEntity(float xOrigin, float yOrigin, float width, float height,
        float movementSpeed, float fireRate, TextureRegion shipTexture, Viewport viewportRef) {
        super(xOrigin, yOrigin, width, height, movementSpeed, fireRate, shipTexture);
        this.viewportRef = viewportRef;
    }

    @Override
    public void step(World world, float deltaTime) {
        super.step(world, deltaTime);
        float[] limits = findScreenLimits(world.width, world.height);
        float upLimit = limits[0];
        float rightLimit = limits[1];
        float downLimit = limits[2];
        float leftLimit = limits[3];

        // TODO movement speed would feel better with acceleration
        if ((Gdx.input.isKeyPressed(Input.Keys.RIGHT) ||
            Gdx.input.isKeyPressed(Input.Keys.D)) && rightLimit > 0) {
            this.translate(Math.min(
                movementSpeed * deltaTime,
                rightLimit), 0f);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.UP) ||
            Gdx.input.isKeyPressed(Input.Keys.W)) && upLimit > 0) {
            this.translate(0f, Math.min(
                movementSpeed * deltaTime,
                upLimit));
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.LEFT) ||
            Gdx.input.isKeyPressed(Input.Keys.A)) && leftLimit < 0) {
            this.translate(Math.max(
                -movementSpeed * deltaTime,
                leftLimit), 0f);
        }

        if ((Gdx.input.isKeyPressed(Input.Keys.DOWN) ||
            Gdx.input.isKeyPressed(Input.Keys.S)) && downLimit < 0) {
            this.translate(0f, Math.max(
                -movementSpeed * deltaTime,
                downLimit));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            fireLaser(world);
        }

        if (Gdx.input.isTouched()) {
            fireLaser(world);

            Vector2 touchPoint = viewportRef.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            Vector2 playerShipCentre = new Vector2(
                this.boundingBox.x + this.boundingBox.width / 2,
                this.boundingBox.y + this.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            final float MOVEMENT_THRESHOLD = 0.5f;
            if (touchDistance > MOVEMENT_THRESHOLD) {
                float xTouchDiff = touchPoint.x - playerShipCentre.x;
                float yTouchDiff = touchPoint.y - playerShipCentre.y;
                float xMove = xTouchDiff / touchDistance * movementSpeed * deltaTime;
                float yMove = yTouchDiff / touchDistance * movementSpeed * deltaTime;

                this.translate(
                    Math.min(Math.max(xMove, leftLimit), rightLimit),
                    Math.min(Math.max(yMove, downLimit), upLimit));
            }
        }
    }

    protected void fireLaser(World world) {
        if (timeSinceLastShot - fireRate >= 0) {
            world.playerLasers.add(world.entityFactory.createPlayerLaser(
                boundingBox.x + boundingBox.width * 0.5f,
                boundingBox.y + boundingBox.height * 1.1f));
            timeSinceLastShot = 0;
        }
    }
}

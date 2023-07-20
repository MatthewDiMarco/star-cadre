package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.model.World;
import io.dim.spaceshooter.util.EntityUtils;

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
        float[] boundaryDistances = EntityUtils
            .calcBoundaryDistances(this.boundingBox, world.width, world.height);

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
            fireLaser(world);
        }

        if (Gdx.input.isTouched()) {
            Vector2 touchPoint = viewportRef.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            fireLaser(world);
            this.translate(
                touchPoint,
                movementSpeed * deltaTime,
                0.5f,
                boundaryDistances);
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

package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.util.EntityUtils;

public class PlayerShipEntity extends ShipEntity {

    protected final Viewport viewportRef;

    public PlayerShipEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed, int hp,
        float laserCooldownDuration,
        int laserStrength, int laserPerShot,
        float laserArcLength,
        float laserSpeed, float laserScatter,
        float invulnerabilityDuration,
        TextureRegion shipTexture,
        Viewport viewportRef) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp,
            laserCooldownDuration, laserStrength, laserPerShot, laserArcLength, laserSpeed, laserScatter,
            invulnerabilityDuration, shipTexture);
        this.viewportRef = viewportRef;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        super.onStep(gameHandler, deltaTime);
        float[] boundaryDistances = EntityUtils.calcBoundaryDistances(
            this.hitBox,
            gameHandler.boundary.width,
            gameHandler.boundary.height);

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
            fireLaser(gameHandler);
        }

        if (Gdx.input.isTouched()) {
            fireLaser(gameHandler);
            Vector2 touchPoint = viewportRef.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            this.translate(
                touchPoint,
                movementSpeed * deltaTime,
                2f);
        }
    }

    @Override
    public void onFireLaser(GameHandler gameHandler) { // TODO gonna need to move this up (ShipEntity)
        float arcRegionLength = laserArcLength / laserPerShot;
        for (int ii = 0; ii < laserPerShot; ii++) {
            float offset = (ii*arcRegionLength + arcRegionLength/2) - laserArcLength/2;
            LaserEntity laser = gameHandler.factory.createPlayerLaser(
                hitBox.x + hitBox.width * 0.5f,
                hitBox.y + hitBox.height * 1.1f);
            laser.strength = laserStrength;
            laser.movementSpeed = laserSpeed;
            laser.direction.x = offset;
            gameHandler.lasers.add(laser);
        }
    }
}

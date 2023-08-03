package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.factory.EntityFactory.LaserType;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.PickupEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;

public class PlayerShipEntity extends ShipEntity {

    public PickupEntity pickup;

    private TouchScreen touchScreen;

    public PlayerShipEntity(float xOrigin, float yOrigin,
        float width, float height,
        float movementSpeed, int hp,
        float invulnerabilityDuration,
        TextureRegion shipTexture, TouchScreen touchScreen) {
        super(xOrigin, yOrigin, width, height, movementSpeed, hp,
            0f, 0, 0,
            0f, 0f,
            invulnerabilityDuration, shipTexture);
        this.setDefaultLaserState();
        this.pickup = null;
        this.touchScreen = touchScreen;
    }

    public void setDefaultLaserState() {
        this.lasersEnabled = true;
        this.laserArmourPiercing = false;
        this.laserStrength = 1;
        this.laserPerShot = 1;
        this.laserBarrelWidth = 0f;
        this.laserMovementSpeed = 148f;
        this.laserCooldownDuration = 0.25f;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        super.onStep(gameHandler, deltaTime);

        float[] boundaryDistances = new float[] {
            gameHandler.boundary.height/3 - hitBox.y - hitBox.height, // up
            gameHandler.boundary.width - hitBox.x - hitBox.width, // right
            -hitBox.y, // down
            -hitBox.x // left
        };

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

        if (Gdx.input.isTouched() && gameHandler.playerRef.hp > 0) {
            float speed = gameHandler.playerRef.movementSpeed * deltaTime;
            Vector2 touchPoint = touchScreen.getTouchPoint();
            gameHandler.playerRef.fireLaser(gameHandler);
            gameHandler.playerRef.translate(touchPoint, speed, 2f, boundaryDistances);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.SPACE)) {
            fireLaser(gameHandler);
        }
    }

    @Override
    public LaserEntity getBaseLaser(GameHandler gameHandler) {
        return gameHandler.factory.createLaser(
            hitBox.x + hitBox.width * 0.5f,
            hitBox.y + hitBox.height,
            LaserType.PLAYER);
    }

    public interface TouchScreen {
        Vector2 getTouchPoint();
    }
}

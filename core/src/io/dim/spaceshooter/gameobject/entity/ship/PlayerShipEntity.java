package io.dim.spaceshooter.gameobject.entity.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.factory.EntityFactory.LaserType;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.PickupEntity;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.helper.Assets;

public class PlayerShipEntity extends ShipEntity {

    public PickupEntity pickup;
    public TouchScreen touchScreen;

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
    public void onCreate(
        float xOrigin, float yOrigin, float width, float height, Assets assets) {
        this.hitBox = new Rectangle(
            xOrigin - width / 2, yOrigin - height / 2, width, height);
        this.disposable = false;
        this.movementSpeed = 48f;
        this.hp = 3;
        this.hpMax = hp;
        this.invulnerabilityEnabled = false;
        this.invulnerabilityDuration = 1f;
        this.timerLastLaser = 0;
        this.timerLastHit = invulnerabilityDuration;
        this.alpha = 1f;
        this.shipTexture = assets.textureAtlas.findRegion("playerShip3_blue");
        this.laserSound = assets.laserSound1;
        this.explosionSound = assets.explosionSound1;
        this.hurtSound = assets.hurtSound1;
        this.pickup = null;
        this.setDefaultLaserState();
    }

    @Override
    public void onDestroy(GameHandler gameHandler) {
        super.onDestroy(gameHandler);
        gameHandler.gameIsOver = true;
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

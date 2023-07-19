package io.dim.spaceshooter.model.ship;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.model.World;

public class PlayerShipEntity extends ShipEntity {

    private final Viewport viewportRef;

    public PlayerShipEntity(float xOrigin, float yOrigin, float width, float height, float movementSpeed,
        TextureRegion shipTexture, Viewport viewportRef) {
        super(xOrigin, yOrigin, width, height, movementSpeed, shipTexture);
        this.viewportRef = viewportRef;
    }

    @Override
    public void tick(World world, float deltaTime) {
        float leftLimit = -this.boundingBox.x;
        float downLimit = -this.boundingBox.y;
        float rightLimit = world.width - this.boundingBox.x - this.boundingBox.width;
        float upLimit = world.height - this.boundingBox.y - this.boundingBox.height;

        // TODO movement speed would feel better with acceleration
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            this.translate(Math.min(
                movementSpeed * deltaTime,
                rightLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            this.translate(0f, Math.min(
                movementSpeed * deltaTime,
                upLimit));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            this.translate(Math.max(
                -movementSpeed * deltaTime,
                leftLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            this.translate(0f, Math.max(
                -movementSpeed * deltaTime,
                downLimit));
        }

        if (Gdx.input.isTouched()) {
            Vector2 touchPoint = viewportRef.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            Vector2 playerShipCentre = new Vector2(
                this.boundingBox.x + this.boundingBox.width / 2,
                this.boundingBox.y + this.boundingBox.height / 2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            float MOVEMENT_THRESHOLD = 0.5f;
            if (touchDistance > MOVEMENT_THRESHOLD) {
                float xTouchDiff = touchPoint.x - playerShipCentre.x;
                float yTouchDiff = touchPoint.y - playerShipCentre.y;
                float xMove = xTouchDiff / touchDistance * movementSpeed * deltaTime;
                float yMove = yTouchDiff / touchDistance * movementSpeed * deltaTime;

                // TODO probably easiest to pack screen collision code into reusable function
                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                this.translate(xMove, yMove);
            }
        }
    }
}

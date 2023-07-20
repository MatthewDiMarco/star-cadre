package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

public abstract class Entity {

    public Rectangle boundingBox;
    public float movementSpeed;

    public Entity(float xOrigin, float yOrigin, float width, float height, float movementSpeed) {
        this.movementSpeed = movementSpeed;
        this.boundingBox = new Rectangle(
            xOrigin - width / 2,
            yOrigin - height / 2,
            width, height);
    }
    public void translate(float xAmount, float yAmount) {
        boundingBox.setPosition(boundingBox.x + xAmount, boundingBox.y + yAmount);
    }

    public void translate(float xAmount, float yAmount, float[] boundaryDistances) {
        this.translate(
            Math.min(Math.max(xAmount, boundaryDistances[3]), boundaryDistances[1]),
            Math.min(Math.max(yAmount, boundaryDistances[2]), boundaryDistances[0]));
    }

    public void translate(
        Vector2 point,
        float speed,
        float proximityThreshold,
        float[] boundaryDistances) {
        Vector2 entityCentrePoint = new Vector2(
            this.boundingBox.x + this.boundingBox.width / 2,
            this.boundingBox.y + this.boundingBox.height / 2);

        float distanceToPoint = point.dst(entityCentrePoint);

        if (distanceToPoint > proximityThreshold) {
            float xTouchDiff = point.x - entityCentrePoint.x;
            float yTouchDiff = point.y - entityCentrePoint.y;
            float xMove = xTouchDiff / distanceToPoint * speed;
            float yMove = yTouchDiff / distanceToPoint * speed;

            this.translate(xMove, yMove, boundaryDistances);
        }
    }

    public abstract void step(World world, float deltaTime);
    public abstract void draw(SpriteBatch batch);
}

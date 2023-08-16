package io.dim.spaceshooter.gameobject.entity;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.helper.Assets;

public abstract class Entity implements GameObject {

    public Rectangle hitBox;
    public float movementSpeed;
    public boolean disposable = false;

    public boolean intersects(Entity entity) {
        return hitBox.overlaps(entity.hitBox);
    }

    public Vector2 getCenterPoint() {
        return new Vector2(
            this.hitBox.x + this.hitBox.width / 2,
            this.hitBox.y + this.hitBox.height / 2);
    }

    public void translate(float xAmount, float yAmount) {
        hitBox.setPosition(hitBox.x + xAmount, hitBox.y + yAmount);
    }

    public void translate(float xAmount, float yAmount, float[] boundaryDistances) {
        this.translate(
            Math.min(Math.max(xAmount, boundaryDistances[3]), boundaryDistances[1]),
            Math.min(Math.max(yAmount, boundaryDistances[2]), boundaryDistances[0]));
    }

    public void translate(Vector2 point, float speed, float proximityThreshold) {
        Vector2 entityCentrePoint = getCenterPoint();
        float distanceToPoint = point.dst(entityCentrePoint);
        if (distanceToPoint > proximityThreshold) {
            float xPointDiff = point.x - entityCentrePoint.x;
            float yPointDiff = point.y - entityCentrePoint.y;
            float xMove = xPointDiff / distanceToPoint * speed;
            float yMove = yPointDiff / distanceToPoint * speed;
            this.translate(xMove, yMove);
        }
    }

    public void translate(
        Vector2 point, float speed, float proximityThreshold, float[] boundaryDistances) {
        Vector2 entityCentrePoint = getCenterPoint();
        float distanceToPoint = point.dst(entityCentrePoint);
        if (distanceToPoint > proximityThreshold) {
            float xPointDiff = point.x - entityCentrePoint.x;
            float yPointDiff = point.y - entityCentrePoint.y;
            float xMove = xPointDiff / distanceToPoint * speed;
            float yMove = yPointDiff / distanceToPoint * speed;
            this.translate(xMove, yMove, boundaryDistances);
        }
    }

    public abstract void onCreate(
        float xOrigin, float yOrigin, float width, float height, Assets assets);

    public abstract void onDestroy(GameHandler gameHandler);
}

package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class Entity {

    public Rectangle boundingBox;

    public Entity(float xOrigin, float yOrigin, float width, float height) {
        this.boundingBox = new Rectangle(
            xOrigin - width / 2,
            yOrigin - height / 2,
            width,
            height);
    }

    public void translate(float xAmount, float yAmount) {
        boundingBox.setPosition(boundingBox.x + xAmount, boundingBox.y + yAmount);
    }

    public abstract void tick(World world, float deltaTime);
    public abstract void draw(SpriteBatch batch);
}

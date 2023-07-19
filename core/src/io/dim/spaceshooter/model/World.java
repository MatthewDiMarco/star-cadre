package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import java.util.ArrayList;
import java.util.List;

public class World {

    public final int width;
    public final int height;

    public List<Entity> entities;

    public World(int width, int height) {
        this.width = width;
        this.height = height;
        this.entities = new ArrayList<>();
    }

    public void tickEntities(float deltaTime) {
        for (Entity ee : entities) {
            ee.tick(this, deltaTime);
        }
    }

    public void drawEntities(SpriteBatch batch) {
        for (Entity ee : entities) {
            ee.draw(batch);
        }
    }
}

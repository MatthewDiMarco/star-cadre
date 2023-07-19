package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.EntityFactory;
import io.dim.spaceshooter.model.ship.PlayerShipEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class World {

    public final int width;
    public final int height;

    public final EntityFactory entityFactory;

    public PlayerShipEntity player;
    public List<Laser> playerLasers;

    public World(int width, int height, EntityFactory entityFactory) {
        this.width = width;
        this.height = height;
        this.entityFactory = entityFactory;
        this.playerLasers = new ArrayList<>();
    }

    public void updateEntities(float deltaTime) {
        player.step(this, deltaTime);
        ListIterator<Laser> iterator = playerLasers.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.step(this, deltaTime);
            if (laser.boundingBox.y > height) {
                iterator.remove();
            }
        }
    }

    public void drawEntities(SpriteBatch batch) {
        player.draw(batch);
        for (Laser laser : playerLasers) {
            laser.draw(batch);
        }
    }
}

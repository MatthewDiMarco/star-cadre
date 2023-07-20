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
    public List<Laser> playerLasers; // TODO replace with libGDX collections
    public List<Laser> alienLasers;
    public List<Entity> hostiles; // TODO maybe combine all these lists into a map?

    public World(int width, int height, EntityFactory entityFactory) {
        this.width = width;
        this.height = height;
        this.entityFactory = entityFactory;
        this.playerLasers = new ArrayList<>();
        this.alienLasers = new ArrayList<>();
        this.hostiles = new ArrayList<>();
    }

    public void updateEntities(float deltaTime) {
        player.step(this, deltaTime);

        ListIterator<Laser> laserIterator;
        laserIterator = playerLasers.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            laser.step(this, deltaTime);
            if (laser.boundingBox.y > height) {
                laserIterator.remove();
            }
        }
        laserIterator = alienLasers.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            laser.step(this, deltaTime);
            if (laser.boundingBox.y < -10) {
                laserIterator.remove();
            }
        }

        ListIterator<Entity> hostileIterator;
        hostileIterator = hostiles.listIterator();
        while (hostileIterator.hasNext()) {
            Entity hostile = hostileIterator.next();
            hostile.step(this, deltaTime);
        }
    }

    public void drawEntities(SpriteBatch batch) {
        player.draw(batch);
        for (Laser laser : playerLasers) {
            laser.draw(batch);
        }
        for (Laser laser : alienLasers) {
            laser.draw(batch);
        }
        for (Entity hostile : hostiles) {
            hostile.draw(batch);
        }
    }
}

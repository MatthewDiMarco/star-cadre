package io.dim.spaceshooter;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.dim.spaceshooter.model.Entity;
import io.dim.spaceshooter.model.LaserEntity;
import io.dim.spaceshooter.model.ship.PlayerShipEntity;
import io.dim.spaceshooter.model.ship.ShipEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EntityManager {

    public final Rectangle boundary;
    public final EntityFactory factory;
    public PlayerShipEntity playerRef;
    public List<ShipEntity> ships;
    public List<LaserEntity> lasers; // TODO replace with libGDX collections

    public EntityManager(int width, int height, EntityFactory factory) {
        boundary = new Rectangle(0, 0, width, height);
        this.factory = factory;
        this.ships = new ArrayList<>();
        this.lasers = new ArrayList<>();

    }

    public void updateEntities(float deltaTime) {
        stepAll((List<Entity>)(List<?>)ships, deltaTime);
        stepAll((List<Entity>)(List<?>)lasers, deltaTime);
    }

    public void drawEntities(SpriteBatch batch) {
        for (Entity ship : ships) ship.draw(batch);
        for (LaserEntity laser : lasers) laser.draw(batch);
    }

    private <T> void stepAll(List<Entity> entities, float deltaTime) {
        ListIterator<Entity> laserIterator = entities.listIterator();
        while (laserIterator.hasNext()) {
            Entity laser = laserIterator.next();
            if (laser.disposable) {
                laserIterator.remove();
            } else {
                laser.step(this, deltaTime);
            }
        }
    }
}

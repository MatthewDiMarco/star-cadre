package io.dim.spaceshooter.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.entity.Entity;
import io.dim.spaceshooter.entity.LaserEntity;
import io.dim.spaceshooter.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.entity.ship.ShipEntity;
import io.dim.spaceshooter.ApplicationObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class EntityHandler implements ApplicationObject {

    public final Rectangle boundary;
    public final EntityFactory factory;
    public ParticleHandler particleHandler;
    public PlayerShipEntity playerRef;
    public List<ShipEntity> ships;
    public List<LaserEntity> lasers; // TODO replace with libGDX collections

    public EntityHandler(int width, int height, EntityFactory factory,
        ParticleHandler particleHandler) {
        boundary = new Rectangle(0, 0, width, height);
        this.factory = factory;
        this.particleHandler = particleHandler;
        this.ships = new ArrayList<>();
        this.lasers = new ArrayList<>();

    }

    @Override
    public void update(float deltaTime) {
        stepAll((List<Entity>)(List<?>)ships, deltaTime);
        stepAll((List<Entity>)(List<?>)lasers, deltaTime);
        particleHandler.update(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        for (Entity ship : ships) ship.onDraw(batch);
        for (LaserEntity laser : lasers) laser.onDraw(batch);
        particleHandler.render(batch);
    }

    private void stepAll(List<Entity> entities, float deltaTime) { // TODO destroy all entities out of bounds
        ListIterator<Entity> iterator = entities.listIterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity.disposable) {
                entity.onDestroy(this, particleHandler); // TODO avoid particles if off screen
                iterator.remove();
            } else {
                entity.onStep(this, deltaTime);
            }
        }
    }
}

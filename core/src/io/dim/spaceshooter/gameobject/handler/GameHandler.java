package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.gameobject.entity.Entity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.gameobject.GameObject;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GameHandler implements GameObject {

    public final Rectangle boundary;
    public final EntityFactory factory;

    public final ParticleHandler particleHandler;
    private final ParallaxHandler parallaxHandler;

    public PlayerShipEntity playerRef;
    public List<ShipEntity> ships;
    public List<LaserEntity> lasers; // TODO replace with libGDX collections

    public GameHandler(int width, int height,
        EntityFactory factory,
        ParticleHandler particleHandler,
        ParallaxHandler parallaxHandler) {
        boundary = new Rectangle(0, 0, width, height);
        this.factory = factory;
        this.particleHandler = particleHandler;
        this.parallaxHandler = parallaxHandler;
        this.ships = new ArrayList<>();
        this.lasers = new ArrayList<>();

    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        parallaxHandler.onStep(gameHandler, deltaTime);
        stepAll((List<Entity>)(List<?>)ships, deltaTime);
        stepAll((List<Entity>)(List<?>)lasers, deltaTime);
        particleHandler.onStep(gameHandler, deltaTime);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        parallaxHandler.onDraw(batch);
        for (ShipEntity ship : ships) ship.onDraw(batch);
        for (LaserEntity laser : lasers) laser.onDraw(batch);
        particleHandler.onDraw(batch);
    }

    private void stepAll(List<Entity> entities, float deltaTime) {
        ListIterator<Entity> iterator = entities.listIterator();
        while (iterator.hasNext()) {
            Entity entity = iterator.next();
            if (entity.disposable) {
                entity.onDestroy(this);
                iterator.remove();
            } else {
                entity.onStep(this, deltaTime);
            }
        }
    }
}

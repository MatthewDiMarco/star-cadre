package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.factory.EntityFactory.PickupType;
import io.dim.spaceshooter.gameobject.entity.Entity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.pickup.Pickup;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.util.MathUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class GameHandler implements GameObject {

    public final Rectangle boundary;
    public final EntityFactory factory;

    public final ParticleHandler particleHandler;
    private final ParallaxHandler parallaxHandler;
    private final SpawnHandler spawnHandler;
    private final HudHandler hudHandler;

    public int score = 0;
    public PlayerShipEntity playerRef;
    public List<ShipEntity> ships;
    public List<LaserEntity> lasers; // TODO replace with libGDX collections
    public List<Pickup> pickups;

    public GameHandler(int width, int height,
        EntityFactory factory,
        ParticleHandler particleHandler,
        ParallaxHandler parallaxHandler,
        SpawnHandler spawnHandler,
        HudHandler hudHandler) {
        boundary = new Rectangle(0, 0, width, height);
        this.factory = factory;
        this.particleHandler = particleHandler;
        this.parallaxHandler = parallaxHandler;
        this.spawnHandler = spawnHandler;
        this.hudHandler = hudHandler;
        this.ships = new ArrayList<>();
        this.lasers = new ArrayList<>();
        this.pickups = new ArrayList<>();
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        parallaxHandler.onStep(gameHandler, deltaTime);
        stepAll((List<Entity>)(List<?>)ships, deltaTime);
        stepAll((List<Entity>)(List<?>)lasers, deltaTime);
        stepAll((List<Entity>)(List<?>)pickups, deltaTime);
        particleHandler.onStep(gameHandler, deltaTime);
        spawnHandler.onStep(gameHandler, deltaTime);
        hudHandler.onStep(gameHandler, deltaTime);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        parallaxHandler.onDraw(batch);
        for (ShipEntity ship : ships) ship.onDraw(batch);
        for (LaserEntity laser : lasers) laser.onDraw(batch);
        for (Pickup pickup : pickups) pickup.onDraw(batch);
        particleHandler.onDraw(batch);
        hudHandler.onDraw(batch);
    }

    public void rollRandomPickup(float xPos, float yPos) {
        int type = MathUtils.random.nextInt(50);
        if (type < 3) {
            Pickup pickup = factory.createPickup(xPos, yPos, PickupType.values()[type]);
            pickups.add(pickup);
        }
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

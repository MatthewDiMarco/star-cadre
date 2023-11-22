package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Array.ArrayIterator;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.factory.EntityFactory.PickupType;
import io.dim.spaceshooter.gameobject.entity.Entity;
import io.dim.spaceshooter.gameobject.entity.LaserEntity;
import io.dim.spaceshooter.gameobject.entity.PickupEntity;
import io.dim.spaceshooter.gameobject.entity.ship.PlayerShipEntity;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.helper.MathUtils;

public class GameHandler implements GameObject {

    public final Rectangle boundary;
    public final EntityFactory factory;

    public final ParallaxHandler parallaxHandler;
    public final ParticleHandler particleHandler;
    private final HudHandler hudHandler;
    private final SpawnHandler spawnHandler;

    public int score;
    public int highscore;
    public PlayerShipEntity playerRef;
    public Array<ShipEntity> ships = new Array<>();
    public Array<LaserEntity> lasers = new Array<>();;
    public Array<PickupEntity> pickups = new Array<>();;
    public boolean gameIsOver = false;

    public GameHandler(int width, int height,
        EntityFactory factory,
        ParallaxHandler parallaxHandler,
        ParticleHandler particleHandler,
        HudHandler hudHandler,
        SpawnHandler spawnHandler) {
        boundary = new Rectangle(0, 0, width, height);
        this.factory = factory;
        this.parallaxHandler = parallaxHandler;
        this.particleHandler = particleHandler;
        this.hudHandler = hudHandler;
        this.spawnHandler = spawnHandler;
        this.score = 0;
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        highscore = prefs.getInteger("highscore");
        prefs.flush();
        hudHandler.setScores(highscore);
    }

    public int getWaveNumber() {
        return spawnHandler.waveNumber;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        parallaxHandler.onStep(gameHandler, deltaTime);
        stepAll((Array<Entity>)(Array<?>)ships, deltaTime);
        stepAll((Array<Entity>)(Array<?>)lasers, deltaTime);
        stepAll((Array<Entity>)(Array<?>) pickups, deltaTime);
        particleHandler.onStep(gameHandler, deltaTime);
        hudHandler.onStep(gameHandler, deltaTime);
        spawnHandler.onStep(gameHandler, deltaTime);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        for (ShipEntity ship : ships) ship.onDraw(batch);
        for (LaserEntity laser : lasers) laser.onDraw(batch);
        for (PickupEntity pickupEntity : pickups) pickupEntity.onDraw(batch);
        particleHandler.onDraw(batch);
        hudHandler.onDraw(batch);
    }

    public void rollRandomPickup(float xPos, float yPos) {
        if (spawnHandler.timerLastPickup <= 0) {
            int type = MathUtils.random.nextInt(50);
            if (type < 4) {
                PickupEntity pickupEntity = factory.createPickup(xPos, yPos, PickupType.values()[type]);
                pickups.add(pickupEntity);
                spawnHandler.timerLastPickup = SpawnHandler.DURATION_BETWEEN_PICKUPS;
            }
        }
    }

    public void updateHighscore() {
        Preferences prefs = Gdx.app.getPreferences("My Preferences");
        prefs.putInteger("highscore", score);
        prefs.flush();
    }

    private void stepAll(Array<Entity> entities, float deltaTime) {
        ArrayIterator<Entity> iterator = entities.iterator();
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

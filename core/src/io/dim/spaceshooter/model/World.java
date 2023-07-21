package io.dim.spaceshooter.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.EntityFactory;
import io.dim.spaceshooter.model.ship.PlayerShipEntity;
import io.dim.spaceshooter.model.ship.ShipEntity;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class World {

    public final int width;
    public final int height;

    public final EntityFactory entityFactory;

    public PlayerShipEntity player;
    public List<Laser> playerLasers; // TODO replace with libGDX collections
    public List<ShipEntity> aliens; // TODO maybe combine all these lists into a map?
    public List<Laser> alienLasers;
    public List<Entity> asteroids;

    public World(int width, int height, EntityFactory entityFactory) {
        this.width = width;
        this.height = height;
        this.entityFactory = entityFactory;
        this.playerLasers = new ArrayList<>();
        this.aliens = new ArrayList<>();
        this.alienLasers = new ArrayList<>();
        this.asteroids = new ArrayList<>();

    }

    public void updateEntities(float deltaTime) {
        updateLasers(deltaTime);
        updateShips(deltaTime);
    }

    public void drawEntities(SpriteBatch batch) {
        player.draw(batch);
        for (Laser laser : playerLasers) {
            laser.draw(batch);
        }
        for (Laser laser : alienLasers) {
            laser.draw(batch);
        }
        for (Entity alien : aliens) {
            alien.draw(batch);
        }
    }

    private void updateLasers(float deltaTime) {
        ListIterator<Laser> laserIterator;

        laserIterator = playerLasers.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            laser.step(this, deltaTime);
            if (laser.hitBox.y > height) {
                laserIterator.remove();
            }
            ListIterator<ShipEntity> alienIterator = aliens.listIterator();
            while (alienIterator.hasNext()) {
                ShipEntity alien = alienIterator.next();
                if (laser.intersects(alien)) {
                    alien.hit(laser);
                    laserIterator.remove();
                }
            }
        }

        laserIterator = alienLasers.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            laser.step(this, deltaTime);
            if (laser.hitBox.y < -10) {
                laserIterator.remove();
            }
            if (laser.intersects(player) && !player.invulnerable && player.functional) {
                player.hit(laser);
                laserIterator.remove();
            }
        }
    }

    private void updateShips(float deltaTime) {
        player.step(this, deltaTime);

        ListIterator<ShipEntity> alienIterator;
        alienIterator = aliens.listIterator();
        while (alienIterator.hasNext()) {
            ShipEntity alien = alienIterator.next();
            alien.step(this, deltaTime);
            if (!alien.functional) {
                alienIterator.remove();
            }
        }
    }
}

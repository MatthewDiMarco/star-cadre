package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.factory.EntityFactory.AlienType;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.util.MathUtils;
import java.util.Arrays;
import java.util.Stack;

public class SpawnHandler implements GameObject {

    public static final float DURATION_BETWEEN_SWARMS = 15f;

    public Stack<Job> spawnJobs;

    protected float timerLastSwarm;

    public SpawnHandler() {
        this.spawnJobs = new Stack<>();
        this.timerLastSwarm = DURATION_BETWEEN_SWARMS;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        timerLastSwarm = Math.min(DURATION_BETWEEN_SWARMS, timerLastSwarm + deltaTime);
        if (spawnJobs.empty() && gameHandler.ships.size() == 1) {
            int type = MathUtils.random.nextInt(2);
            generateSwarm(gameHandler, AlienType.values()[type], 5, false, true);
            timerLastSwarm = 0f;
        }

        if (!spawnJobs.empty()) {
            Job nextJob = spawnJobs.peek();
            nextJob.timeToSpawn = nextJob.timeToSpawn - deltaTime;
            if (nextJob.timeToSpawn <= 0) {
                gameHandler.ships.addAll(Arrays.asList(nextJob.aliens));
                spawnJobs.pop();
            }
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {}

    private void generateSwarm(
        GameHandler gameHandler, AlienType alienType, int length, boolean mirror, boolean twin) {
        for (int ii = 0; ii < length; ii++) {
            ShipEntity[] aliens = new ShipEntity[twin ? 2 : 1];
            aliens[0] = gameHandler.factory.createAlien(
                gameHandler.boundary.width / 2,
                gameHandler.boundary.height + 10,
                mirror, alienType);
            if (twin) {
                aliens[1] = gameHandler.factory.createAlien(
                    gameHandler.boundary.width / 2,
                    gameHandler.boundary.height + 10,
                    !mirror, alienType);
            }
            spawnJobs.push(new Job(aliens, 0.15f)); // TODO use yOffset to seperate, not spawn time
        }
    }

    static class Job {

        public ShipEntity[] aliens;
        public float timeToSpawn;

        public Job(ShipEntity[] entity, float timeToSpawn) {
            this.aliens = entity;
            this.timeToSpawn = timeToSpawn;
        }
    }
}

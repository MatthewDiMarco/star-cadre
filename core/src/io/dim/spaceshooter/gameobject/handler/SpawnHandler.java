package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.factory.EntityFactory.EnemyType;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.helper.MathUtils;
import java.util.Arrays;
import java.util.Stack;

public class SpawnHandler implements GameObject {

    public static final float DURATION_BETWEEN_PICKUPS = 10f;
    public static final float DURATION_BETWEEN_ASTEROIDS = 10f;
    public static final float MIN_DELAY_BETWEEN_SWARMS = 0.25f;
    public static final int MIN_SWARM_LENGTH = 2;
    public static final int MAX_SWARM_LENGTH = 6;

    public Stack<Job> spawnJobs;

    protected int waveNumber;
    protected float timerLastPickup;
    protected float timerLastAsteroid;

    public SpawnHandler() {
        this.spawnJobs = new Stack<>();
        this.waveNumber = 0;
        this.timerLastPickup = DURATION_BETWEEN_PICKUPS;
        this.timerLastAsteroid = DURATION_BETWEEN_ASTEROIDS;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        timerLastPickup = Math.min(
            DURATION_BETWEEN_PICKUPS, timerLastPickup - deltaTime);
        timerLastAsteroid = Math.min(
            DURATION_BETWEEN_ASTEROIDS, timerLastAsteroid - deltaTime);

        if (spawnJobs.empty() && gameHandler.ships.size() == 1) {
            generateWave(gameHandler);
        }

        if (!spawnJobs.empty()) {
            Job nextJob = spawnJobs.peek();
            nextJob.timeToSpawn = nextJob.timeToSpawn - deltaTime;
            if (nextJob.timeToSpawn <= 0) {
                gameHandler.ships.addAll(Arrays.asList(nextJob.aliens));
                spawnJobs.pop();
            }
        }

        if (timerLastAsteroid <= 0) {
            float xx = gameHandler.boundary.width / 2;
            float yy = gameHandler.boundary.height + 5;
            float numAsteroids = MathUtils.random.nextInt(3 - 1) + 1;
            for (int ii = 0; ii < numAsteroids; ii++) {
                gameHandler.asteroids.add(gameHandler.factory.createAsteroid(
                    xx + MathUtils.random.nextInt(60) - 30,
                    yy + MathUtils.random.nextInt(40 - 10 * ii) + 10 * ii));
            }
            timerLastAsteroid = DURATION_BETWEEN_ASTEROIDS;
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {}

    private void generateWave(GameHandler gameHandler) {
        waveNumber++;
        if (waveNumber == 1) {
            // intro wave
            generateSwarm(gameHandler, EnemyType.SNAKE, 5f, 2);
            generateSwarm(gameHandler, EnemyType.DRAGON, 0f, 2);
            generateSwarm(gameHandler, EnemyType.INVADER, 3f, 2);
            generateSwarm(gameHandler, EnemyType.INVADER, 0f, 2);
        } else if (waveNumber % 10 == 0) {
            // boss
            generateSwarm(gameHandler, EnemyType.TANK, 0f, 1);
        } else {
            // random spawns
            final int maxSwarmLength = Math.min(MAX_SWARM_LENGTH, waveNumber + MIN_SWARM_LENGTH);
            int swarmLength = MathUtils.random.nextInt(
                maxSwarmLength - MIN_SWARM_LENGTH) + MIN_SWARM_LENGTH;
            int numSwarms = MathUtils.random.nextInt(4 - 2) + 2;
            for (int ii = 0; ii < numSwarms; ii++) {
                int type = MathUtils.random.nextInt(3);
                generateSwarm(gameHandler, EnemyType.values()[type],
                    Math.max(MIN_DELAY_BETWEEN_SWARMS, 2f / waveNumber), swarmLength);
            }
        }
    }

    private void generateSwarm(
        GameHandler gameHandler, EnemyType alienType, float timeOffset, int length) {
        for (int ii = 0; ii < length; ii++) {
            ShipEntity[] aliens = new ShipEntity[2];
            aliens[0] = gameHandler.factory.createEnemy(
                gameHandler.boundary.width / 2,
                gameHandler.boundary.height + 5,
                true, alienType);
            aliens[1] = gameHandler.factory.createEnemy(
                gameHandler.boundary.width / 2,
                gameHandler.boundary.height + 5,
                false, alienType);
            spawnJobs.push(new Job(aliens,
                0.2f + (ii == length-1 ? timeOffset : 0f)));
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

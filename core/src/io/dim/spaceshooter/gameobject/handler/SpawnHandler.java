package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.util.EntityUtils;
import java.util.Arrays;
import java.util.Stack;

public class SpawnHandler implements GameObject {

    public static final float DURATION_BETWEEN_SWARMS = 2.5f;

    public Stack<Job> spawnJobs;

    protected float timerLastSwarm;

    public SpawnHandler() {
        this.spawnJobs = new Stack<>();
        this.timerLastSwarm = DURATION_BETWEEN_SWARMS;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        timerLastSwarm = Math.min(DURATION_BETWEEN_SWARMS, timerLastSwarm + deltaTime);
        if (timerLastSwarm - DURATION_BETWEEN_SWARMS >= 0) {
            float randomXPos = gameHandler.boundary.width / 2 +
                (EntityUtils.random.nextInt(40) - 20);
            generateSnakeJob(gameHandler, randomXPos,
                EntityUtils.random.nextInt(10 - 3) + 3);
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

    private void generateSnakeJob(GameHandler gameHandler, float xPos, int length) {
        for (int ii = 0; ii < length; ii++) {
            ShipEntity aliens = gameHandler.factory.createAlienSnake(
                xPos, gameHandler.boundary.height + 10);
            spawnJobs.push(new Job(new ShipEntity[]{aliens}, 0.15f));
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

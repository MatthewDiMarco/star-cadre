package io.dim.spaceshooter.gameobject.handler;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import io.dim.spaceshooter.factory.EntityFactory.EnemyType;
import io.dim.spaceshooter.gameobject.GameObject;
import io.dim.spaceshooter.gameobject.entity.ship.ShipEntity;
import io.dim.spaceshooter.helper.MathUtils;
import java.util.Arrays;
import java.util.Stack;

/**
 * Ideas on spawn algo:
 * <p>
 * A "Node" represents the sub-spawner for an enemy type.
 * Nodes have a min-max length / speed, a weight (0f to 1f) and "activeFrom" representing the (game)
 * time from which this node will be able to do spawning.
 * <p>
 * A spawn cycle is like a mini wave. On every cycle, a die will be rolled to decide which, out of
 * the active nodes, will spawn. The node's weight will be a factor in this outcome.
 * <p>
 * The following global spawn properties will be treated as a function of game time elapsed:
 * - swarm speed factor
 * - swarm length factor
 * - spawn cycle frequency
 */

public class SpawnHandler implements GameObject {

    public static final float DURATION_BETWEEN_PICKUPS = 10f;

    public Stack<Job> spawnJobs;

    protected float timerLastPickup;

    public SpawnHandler() {
        this.spawnJobs = new Stack<>();
        this.timerLastPickup = 0f;
    }

    @Override
    public void onStep(GameHandler gameHandler, float deltaTime) {
        timerLastPickup = Math.min(DURATION_BETWEEN_PICKUPS, timerLastPickup - deltaTime);

        if (spawnJobs.empty() && gameHandler.ships.size() == 1) {
            int type = MathUtils.random.nextInt(3);
            generateSwarm(gameHandler, EnemyType.values()[type], 10);
//            generateSwarm(gameHandler, EnemyType.TANK, 1);
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
        GameHandler gameHandler, EnemyType alienType, int length) {
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
            spawnJobs.push(new Job(aliens, 0.15f));
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

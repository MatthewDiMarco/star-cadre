package io.dim.spaceshooter;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;

public class GameScreen implements Screen { // TODO big refactor: modular architecture

    private final int WORLD_WIDTH = 72;
    private final int WORLD_HEIGHT = 128;
    private final float MOVEMENT_THRESHOLD = 0.5f;
    private Camera camera;
    private Viewport viewport;
    private SpriteBatch batch;
    private TextureAtlas textureAtlas;
    private TextureRegion[] backgrounds;
    private TextureRegion // terrible...
        playerShipRegion,
        playerShieldRegion,
        enemyShipRegion,
        enemyShieldRegion,
        playerLaserRegion,
        enemyLaserRegion;
    private Texture explosionTexture;
    private float[] backgroundOffsets = { 0, 0, 0, 0 };
    private float backgroundMaxScrollSpeed;
    private float timeBetweenEnemySpawns = 3f;
    private float enemySpawnTimer = 0;
    private PlayerShip playerShip;
    private List<EnemyShip> enemyShips;
    private List<Laser> playerLasers; // TODO update to GDX collections
    private List<Laser> enemyLasers;
    private List<Explosion> explosionList; // TODO put explosion png into atlas
    private int score = 0;
    BitmapFont font;
    float hudVerticalMargin, hudLeftX, hudRightX, hudCentreX, hudRow1Y, hudRow2Y, hudSectionWidth;

    public GameScreen() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(WORLD_WIDTH, WORLD_HEIGHT, camera);

        textureAtlas = new TextureAtlas("images.atlas");

        backgrounds = new TextureRegion[4];
        backgrounds[0] = textureAtlas.findRegion("Starscape00");
        backgrounds[1] = textureAtlas.findRegion("Starscape01");
        backgrounds[2] = textureAtlas.findRegion("Starscape02");
        backgrounds[3] = textureAtlas.findRegion("Starscape03");
        backgroundMaxScrollSpeed = (float)(WORLD_HEIGHT) / 4;

        playerShipRegion = textureAtlas.findRegion("playerShip2_blue");
        enemyShipRegion = textureAtlas.findRegion("enemyRed3");
        playerShieldRegion = textureAtlas.findRegion("shield2");
        enemyShieldRegion = textureAtlas.findRegion("shield1");
        enemyShieldRegion.flip(false, true);
        playerLaserRegion = textureAtlas.findRegion("laserBlue03");
        enemyLaserRegion = textureAtlas.findRegion("laserRed03");
        explosionTexture = new Texture("explosion.png");

        playerShip = new PlayerShip(64, 3,
            WORLD_WIDTH / 2, WORLD_HEIGHT / 4, 10, 10,
            0.4f, 4, 102, 0.5f,
            playerShipRegion, playerShieldRegion, playerLaserRegion);

        enemyShips = new LinkedList<>();
        playerLasers = new LinkedList<>();
        enemyLasers = new LinkedList<>();
        explosionList = new LinkedList<>();

        batch = new SpriteBatch();

        prepareHUD();
    }

    private void prepareHUD() {
        FreeTypeFontGenerator fontGenerator = new FreeTypeFontGenerator(
            Gdx.files.internal("EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);

        font = fontGenerator.generateFont(fontParameter);

        font.getData().setScale(0.08f);

        hudVerticalMargin = font.getCapHeight() / 2;
        hudLeftX = hudVerticalMargin;
        hudRightX = WORLD_WIDTH * 2 / 3 - hudLeftX;
        hudCentreX = WORLD_WIDTH / 3;
        hudRow1Y = WORLD_HEIGHT - hudVerticalMargin;
        hudRow2Y = hudRow1Y - hudVerticalMargin - font.getCapHeight();
        hudSectionWidth = WORLD_WIDTH / 3;
    }

    @Override
    public void render(float deltaTime) {
        batch.begin();
        renderBackground(deltaTime);
        detectInput(deltaTime);
        playerShip.update(deltaTime);
        spawnEnemyShips(deltaTime);
        ListIterator<EnemyShip> enemyShipListIterator = enemyShips.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            moveEnemy(enemyShip, deltaTime);
            enemyShip.update(deltaTime);
            enemyShip.draw(batch);
        }
        playerShip.draw(batch);
        renderLasers(deltaTime);
        detectCollisions();
        renderExplosions(deltaTime);

        updateAndRenderHUD();

        batch.end();
    }

    private void updateAndRenderHUD() {
        font.draw(batch, "Score", hudLeftX, hudRow1Y, hudSectionWidth, Align.left, false);
        font.draw(batch, "Shield", hudCentreX, hudRow1Y, hudSectionWidth, Align.center, false);
        font.draw(batch, "Lives", hudRightX, hudRow1Y, hudSectionWidth, Align.right, false);

        font.draw(batch, String.format(Locale.getDefault(), "%06d", score),
            hudLeftX, hudRow2Y, hudSectionWidth, Align.left, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.shieldHealth),
            hudCentreX, hudRow2Y, hudSectionWidth, Align.center, false);
        font.draw(batch, String.format(Locale.getDefault(), "%02d", playerShip.lives),
            hudRightX, hudRow2Y, hudSectionWidth, Align.right, false);
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
        batch.setProjectionMatrix(camera.combined);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    @Override
    public void show() {

    }

    private void spawnEnemyShips(float deltaTime) {
        enemySpawnTimer += deltaTime;
        if (enemySpawnTimer > timeBetweenEnemySpawns) {
            enemyShips.add(new EnemyShip(48, 1,
                SpaceShooterGame.random.nextFloat() * (WORLD_WIDTH - 10) + 5,
                WORLD_HEIGHT - 5, 10, 10,
                0.3f, 5, 50, 0.8f,
                enemyShipRegion, enemyShieldRegion, enemyLaserRegion));
            enemySpawnTimer -= timeBetweenEnemySpawns;
        }
    }

    private void detectInput(float deltaTime) {
        float leftLimit, rightLimit, upLimit, downLimit;
        leftLimit = -playerShip.boundingBox.x;
        downLimit = -playerShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - playerShip.boundingBox.x - playerShip.boundingBox.width;
        upLimit = (float)WORLD_HEIGHT/2 - playerShip.boundingBox.y - playerShip.boundingBox.height;

        // TODO movement speed would feel better with acceleration

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) && rightLimit > 0) {
            playerShip.translate(Math.min(
                playerShip.movementSpeed * deltaTime,
                rightLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.UP) && upLimit > 0) {
            playerShip.translate(0f, Math.min(
                playerShip.movementSpeed * deltaTime,
                upLimit));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) && leftLimit < 0) {
            playerShip.translate(Math.max(
                -playerShip.movementSpeed * deltaTime,
                leftLimit), 0f);
        }

        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) && downLimit < 0) {
            playerShip.translate(0f, Math.max(
                -playerShip.movementSpeed * deltaTime,
                downLimit));
        }

        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            Gdx.app.exit();
            System.exit(0);
        }

        if (Gdx.input.isTouched()) {
            Vector2 touchPoint = viewport.unproject(
                new Vector2(Gdx.input.getX(), Gdx.input.getY()));

            Vector2 playerShipCentre = new Vector2(
                playerShip.boundingBox.x + playerShip.boundingBox.width/2,
                playerShip.boundingBox.y + playerShip.boundingBox.height/2);

            float touchDistance = touchPoint.dst(playerShipCentre);

            if (touchDistance > MOVEMENT_THRESHOLD) {
                float xTouchDiff = touchPoint.x - playerShipCentre.x;
                float yTouchDiff = touchPoint.y - playerShipCentre.y;
                float xMove = xTouchDiff / touchDistance * playerShip.movementSpeed * deltaTime;
                float yMove = yTouchDiff / touchDistance * playerShip.movementSpeed * deltaTime;

                // TODO probably easiest to pack screen collision code into reusable function
                if (xMove > 0) xMove = Math.min(xMove, rightLimit);
                else xMove = Math.max(xMove, leftLimit);

                if (yMove > 0) yMove = Math.min(yMove, upLimit);
                else yMove = Math.max(yMove, downLimit);

                playerShip.translate(xMove, yMove);
            }

        }
    }

    private void moveEnemy(EnemyShip enemyShip, float deltaTime) {
        float leftLimit, rightLimit, upLimit, downLimit; // TODO all duped
        leftLimit = -enemyShip.boundingBox.x;
        downLimit = (float)WORLD_HEIGHT/2 - enemyShip.boundingBox.y;
        rightLimit = WORLD_WIDTH - enemyShip.boundingBox.x - enemyShip.boundingBox.width;
        upLimit = WORLD_HEIGHT - enemyShip.boundingBox.y - enemyShip.boundingBox.height;

        float xMove = enemyShip.getDirectionVector().x * enemyShip.movementSpeed * deltaTime;
        float yMove = enemyShip.getDirectionVector().y * enemyShip.movementSpeed * deltaTime;

        // TODO probably easiest to pack screen collision code into reusable function
        if (xMove > 0) xMove = Math.min(xMove, rightLimit);
        else xMove = Math.max(xMove, leftLimit);

        if (yMove > 0) yMove = Math.min(yMove, upLimit);
        else yMove = Math.max(yMove, downLimit);

        enemyShip.translate(xMove, yMove);
    }

    private void detectCollisions() {
        // for each player laser, check it intersects an enemy ship
        ListIterator<Laser> laserIterator = playerLasers.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            ListIterator<EnemyShip> enemyShipListIterator = enemyShips.listIterator();
            while (enemyShipListIterator.hasNext()) {
                EnemyShip enemyShip = enemyShipListIterator.next();
                if (enemyShip.intersects(laser.boundingBox)) {
                    if (enemyShip.hitAndCheckDestroyed(laser)) {
                        explosionList.add(new Explosion(
                            explosionTexture, new Rectangle(
                                enemyShip.boundingBox.x,
                                enemyShip.boundingBox.y,
                                enemyShip.boundingBox.width,
                                enemyShip.boundingBox.height), 0.5f));
                        enemyShipListIterator.remove();
                    }
                    laserIterator.remove();
                    break; // TODO fucking diabolical
                }
            }
        }

        // for each enemy laser, check it intersects the player ship
        laserIterator = enemyLasers.listIterator();
        while (laserIterator.hasNext()) {
            Laser laser = laserIterator.next();
            if (playerShip.intersects(laser.boundingBox)) {
                playerShip.hitAndCheckDestroyed(laser);
                laserIterator.remove();
            }
        }
    }

    private void renderExplosions(float deltaTime) {
        ListIterator<Explosion> explosionListIterator = explosionList.listIterator();
        while (explosionListIterator.hasNext()) {
            Explosion explosion = explosionListIterator.next();
            explosion.update(deltaTime);
            if (explosion.isFinished()) {
                explosionListIterator.remove();
            } else {
                explosion.draw(batch);
            }
        }
    }

    private void renderLasers(float deltaTime) {
        if (playerShip.canFireLaser()) {
            Laser[] lasers = playerShip.fireLasers();
            for (Laser laser: lasers) {
                playerLasers.add(laser);
            }
        }
        ListIterator<EnemyShip> enemyShipListIterator = enemyShips.listIterator();
        while (enemyShipListIterator.hasNext()) {
            EnemyShip enemyShip = enemyShipListIterator.next();
            if (enemyShip.canFireLaser()) {
                Laser[] lasers = enemyShip.fireLasers();
                enemyLasers.addAll(Arrays.asList(lasers));
            }
        }
        ListIterator<Laser> iterator = playerLasers.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y += laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y > WORLD_HEIGHT) {
                iterator.remove();
            }
        }
        iterator = enemyLasers.listIterator();
        while (iterator.hasNext()) {
            Laser laser = iterator.next();
            laser.draw(batch);
            laser.boundingBox.y -= laser.movementSpeed * deltaTime;
            if (laser.boundingBox.y + laser.boundingBox.height < 0) {
                iterator.remove();
            }
        }
    }

    private void renderBackground(float deltaTime) {
        backgroundOffsets[0] += deltaTime * backgroundMaxScrollSpeed / 8;
        backgroundOffsets[1] += deltaTime * backgroundMaxScrollSpeed / 4;
        backgroundOffsets[2] += deltaTime * backgroundMaxScrollSpeed / 2;
        backgroundOffsets[3] += deltaTime * backgroundMaxScrollSpeed ;

        for (int ii = 0; ii < backgroundOffsets.length; ii++) {
            if (backgroundOffsets[ii] > WORLD_HEIGHT) {
                backgroundOffsets[ii] = 0;
            }
            batch.draw(backgrounds[ii], 0, -backgroundOffsets[ii], WORLD_WIDTH, WORLD_HEIGHT * 2);
        }
    }
}

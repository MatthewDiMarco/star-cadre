package io.dim.spaceshooter.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator.FreeTypeFontParameter;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.gameobject.handler.HudHandler;
import io.dim.spaceshooter.gameobject.handler.ParticleHandler;
import io.dim.spaceshooter.gameobject.handler.ParallaxHandler;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.gameobject.handler.SpawnHandler;
import io.dim.spaceshooter.helper.Assets;
import io.dim.spaceshooter.helper.Paths;
import java.util.Stack;

public class GameState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;
    public static final float BUTTON_WIDTH = 28f;
    public static final float BUTTON_HEIGHT = 13.9f;

    private final Paths paths;
    private final Assets assets = new Assets();
    private boolean gameRunning = true;
    private boolean stepping = true;
    private ParallaxHandler parallaxHandler;
    private GameHandler gameHandler;

    public GameState(
        OrthographicCamera camera,
        Viewport bgViewport,
        Viewport gameViewport,
        Stack<ApplicationState> manager) {
        super(camera, bgViewport, gameViewport, manager);
        paths = new Paths(new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT));
        assets.backgrounds[0] = new Texture("backgrounds/backgrounds-0.png");
        assets.backgrounds[1] = new Texture("backgrounds/backgrounds-1.png");
        assets.backgrounds[2] = new Texture("backgrounds/backgrounds-2.png");
        assets.textureAtlas = new TextureAtlas("textures/textures.atlas");
        assets.restartTexture = new Texture("textures/restart.png");
        assets.fontGenerator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/EdgeOfTheGalaxyRegular-OVEa6.otf"));
        assets.laserSound1 = Gdx.audio.newSound(
            Gdx.files.internal("sounds/laserShoot1.wav"));
        assets.laserSound2 = Gdx.audio.newSound(
            Gdx.files.internal("sounds/laserShoot2.wav"));
        assets.explosionSound1 = Gdx.audio.newSound(
            Gdx.files.internal("sounds/explosion1.wav"));
        assets.hurtSound1 = Gdx.audio.newSound(
            Gdx.files.internal("sounds/hitHurt1.wav"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);
        init();
    }

    @Override
    public void update(final float deltaTime) {
        handleInput(deltaTime);
        parallaxHandler.onStep(gameHandler, deltaTime);
        if (stepping) {
            gameHandler.onStep(gameHandler, deltaTime);
            if (!gameRunning) stepping = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        bgViewport.apply();
        parallaxHandler.onDraw(batch);
        gameViewport.apply();
        gameHandler.onDraw(batch);
        if (gameHandler.gameIsOver) {
            Color cc = batch.getColor();
            batch.setColor(cc.r, cc.g, cc.b, 0.8f);
            batch.draw(assets.restartTexture,
                ((float)(WORLD_WIDTH / 2) - BUTTON_WIDTH / 2),
                ((float)(WORLD_HEIGHT / 2) - BUTTON_HEIGHT / 2),
                BUTTON_WIDTH, BUTTON_HEIGHT);
            batch.setColor(cc.r, cc.g, cc.b, 1f);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        assets.textureAtlas.dispose();
        assets.restartTexture.dispose();
        assets.fontGenerator.dispose();
        assets.laserSound1.dispose();
        assets.laserSound2.dispose();
        assets.explosionSound1.dispose();
        for (Texture bg : assets.backgrounds) {
            bg.dispose();
        }
    }

    private void handleInput(float deltaTime) {
        if (!gameHandler.gameIsOver && Gdx.input.isKeyJustPressed(Keys.P)) {
            gameRunning = !gameRunning;
            if (!stepping) stepping = true;
        }

        if (!gameHandler.gameIsOver && Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            stepping = true;
        }

        if (gameHandler.gameIsOver && Gdx.input.isTouched()) {
            float xButtonStart = (float)(WORLD_WIDTH / 2) - BUTTON_WIDTH / 2;
            float xButtonEnd = (float)(WORLD_WIDTH / 2) + BUTTON_WIDTH / 2;
            float yButtonStart = (float)(WORLD_HEIGHT / 2) - BUTTON_HEIGHT / 2;
            float yButtonEnd = (float)(WORLD_HEIGHT / 2) + BUTTON_HEIGHT / 2;
            Vector2 touchPoint = gameViewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
            if (touchPoint.x > xButtonStart && touchPoint.x < xButtonEnd &&
                touchPoint.y > yButtonStart && touchPoint.y < yButtonEnd) {
                init();
            }
        }

        if (Gdx.input.isKeyJustPressed(Keys.R)) {
            init();
        }
    }

    private void init() {
        parallaxHandler = new ParallaxHandler(assets.backgrounds,
            WORLD_WIDTH, WORLD_HEIGHT, true, false);
        gameHandler = new GameHandler(WORLD_WIDTH, WORLD_HEIGHT,
            new EntityFactory(paths, assets),
            new ParticleHandler(assets.textureAtlas),
            new HudHandler(assets.fontGenerator,
                assets.textureAtlas.findRegion("playerLife3_blue"),
                new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT)),
            new SpawnHandler());

        gameHandler.playerRef = gameHandler.factory.createPlayer(
            (float) WORLD_WIDTH / 2,
            (float) WORLD_HEIGHT / 4,
            () -> gameViewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())));
        gameHandler.ships.add(gameHandler.playerRef);

        gameRunning = true;
        stepping = true;
    }
}

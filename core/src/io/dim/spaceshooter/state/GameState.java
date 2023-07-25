package io.dim.spaceshooter.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.gameobject.handler.ParticleHandler;
import io.dim.spaceshooter.gameobject.handler.ParallaxHandler;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import java.util.Stack;

public class GameState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;

    private final TextureAtlas textureAtlas;
    private final Texture[] backgrounds = new Texture[3];

    private boolean gameRunning = true;
    private boolean stepping = true;
    private GameHandler gameHandler;

    public GameState(
        OrthographicCamera camera,
        Viewport viewport,
        Stack<ApplicationState> manager) {
        super(camera, viewport, manager);
        backgrounds[0] = new Texture("backgrounds/backgrounds-0.png");
        backgrounds[1] = new Texture("backgrounds/backgrounds-1.png");
        backgrounds[2] = new Texture("backgrounds/backgrounds-2.png");
        textureAtlas = new TextureAtlas("textures/textures.atlas");
        init();
    }

    @Override
    public void update(final float deltaTime) {
        handleInput();
        if (stepping) {
            gameHandler.onStep(gameHandler, deltaTime);
            if (!gameRunning) stepping = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        gameHandler.onDraw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        for (Texture bg : backgrounds) {
            bg.dispose();
        }
    }

    private void handleInput() {
        if (Gdx.input.isKeyJustPressed(Keys.P)) {
            gameRunning = !gameRunning;
            if (!stepping) stepping = true;
        }
        if (Gdx.input.isKeyJustPressed(Keys.ENTER)) {
            stepping = true;
        }
        if (Gdx.input.isKeyJustPressed(Keys.R)) {
            init();
        }
    }

    private void init() {
        gameHandler = new GameHandler(
            WORLD_WIDTH, WORLD_HEIGHT,
            new EntityFactory(textureAtlas),
            new ParticleHandler(textureAtlas),
            new ParallaxHandler(backgrounds,
                WORLD_WIDTH, WORLD_HEIGHT,
                true, false));

        gameHandler.playerRef = gameHandler.factory.createPlayer(
                (float)WORLD_WIDTH / 2,
                (float)WORLD_HEIGHT / 4, viewport);
        gameHandler.ships.add(gameHandler.playerRef);

        gameRunning = true;
        stepping = true;

        // temp TODO delete
        gameHandler.ships.add(gameHandler.factory.createAlienBasic(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT + 10));

        gameHandler.ships.add(gameHandler.factory.createAlienPathTracer(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT - 20));
    }
}

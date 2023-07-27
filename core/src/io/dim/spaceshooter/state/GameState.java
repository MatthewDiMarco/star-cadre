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
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.gameobject.handler.HudHandler;
import io.dim.spaceshooter.gameobject.handler.ParticleHandler;
import io.dim.spaceshooter.gameobject.handler.ParallaxHandler;
import io.dim.spaceshooter.gameobject.handler.GameHandler;
import io.dim.spaceshooter.gameobject.handler.SpawnHandler;
import java.util.Stack;

public class GameState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;

    private final TextureAtlas textureAtlas;
    private final Texture[] backgrounds = new Texture[3];
    private final FreeTypeFontGenerator fontGenerator;

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
        fontGenerator = new FreeTypeFontGenerator(
            Gdx.files.internal("fonts/EdgeOfTheGalaxyRegular-OVEa6.otf"));
        FreeTypeFontGenerator.FreeTypeFontParameter fontParameter = new FreeTypeFontParameter();
        fontParameter.size = 72;
        fontParameter.borderWidth = 3.6f;
        fontParameter.color = new Color(1, 1, 1, 0.3f);
        fontParameter.borderColor = new Color(0, 0, 0, 0.3f);
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
        fontGenerator.dispose();
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
                true, false),
            new SpawnHandler(),
            new HudHandler(
                fontGenerator,
                new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT),
                textureAtlas.findRegion("playerLife3_blue")));

        gameHandler.playerRef = gameHandler.factory.createPlayer(
                (float)WORLD_WIDTH / 2,
                (float)WORLD_HEIGHT / 4, viewport);
        gameHandler.ships.add(gameHandler.playerRef);

        gameRunning = true;
        stepping = true;
    }
}

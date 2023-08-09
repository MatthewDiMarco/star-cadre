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
import io.dim.spaceshooter.helper.PathAtlas;
import java.util.Stack;

public class GameState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;
    public static final float BUTTON_WIDTH = 28f;
    public static final float BUTTON_HEIGHT = 13.9f;

    private final TextureAtlas textureAtlas;
    private final Texture restartTexture;
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
        restartTexture = new Texture("textures/restart.png");
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
        handleInput(deltaTime);
        if (stepping) {
            gameHandler.onStep(gameHandler, deltaTime);
            if (!gameRunning) stepping = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        gameHandler.onDraw(batch);
        if (gameHandler.gameIsOver) {
            Color cc = batch.getColor();
            batch.setColor(cc.r, cc.g, cc.b, 0.8f);
            batch.draw(restartTexture,
                ((float)(WORLD_WIDTH / 2) - BUTTON_WIDTH / 2),
                ((float)(WORLD_HEIGHT / 2) - BUTTON_HEIGHT / 2),
                BUTTON_WIDTH, BUTTON_HEIGHT);
            batch.setColor(cc.r, cc.g, cc.b, 1f);
        }
        batch.end();
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        restartTexture.dispose();
        fontGenerator.dispose();
        for (Texture bg : backgrounds) {
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
            Vector2 touchPoint = viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY()));
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
        gameHandler = new GameHandler(
            WORLD_WIDTH, WORLD_HEIGHT,
            new EntityFactory(textureAtlas, new PathAtlas(new Rectangle(0, 0, WORLD_WIDTH, WORLD_HEIGHT))),
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
            (float) WORLD_WIDTH / 2,
            (float) WORLD_HEIGHT / 4,
            () -> viewport.unproject(new Vector2(Gdx.input.getX(), Gdx.input.getY())));

        gameHandler.ships.add(gameHandler.playerRef);

        gameRunning = true;
        stepping = true;
    }
}

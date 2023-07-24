package io.dim.spaceshooter.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.factory.EntityFactory;
import io.dim.spaceshooter.handler.ParticleHandler;
import io.dim.spaceshooter.handler.ParallaxHandler;
import io.dim.spaceshooter.handler.EntityHandler;
import java.util.Stack;

public class PlayState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;

    private TextureAtlas textureAtlas;
    private final Texture[] backgrounds = new Texture[3];


    private ParallaxHandler parallaxHandler;
    private EntityHandler entityHandler;
    private boolean gameRunning = true;
    private boolean stepping = true;

    public PlayState(
        OrthographicCamera camera,
        Viewport viewport,
        Stack<ApplicationState> manager) {
        super(camera, viewport, manager);
        init();
    }

    @Override
    public void update(final float deltaTime) {
        handleInput();
        if (stepping) {
            parallaxHandler.update(deltaTime);
            entityHandler.update(deltaTime);
            if (!gameRunning) stepping = false;
        }
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        parallaxHandler.render(batch);
        entityHandler.render(batch);
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
        backgrounds[0] = new Texture("backgrounds/backgrounds-0.png");
        backgrounds[1] = new Texture("backgrounds/backgrounds-1.png");
        backgrounds[2] = new Texture("backgrounds/backgrounds-2.png");
        parallaxHandler = new ParallaxHandler(
            backgrounds, WORLD_WIDTH, WORLD_HEIGHT,
            true, false);

        textureAtlas = new TextureAtlas("textures/textures.atlas");
        entityHandler = new EntityHandler(
            WORLD_WIDTH, WORLD_HEIGHT,
            new EntityFactory(textureAtlas),
            new ParticleHandler(textureAtlas));
        entityHandler.playerRef = entityHandler.factory.createPlayer(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT / 4, viewport);
        entityHandler.ships.add(entityHandler.playerRef);

        // temp
        entityHandler.ships.add(entityHandler.factory.createAlienBasic(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT + 10));

        gameRunning = true;
        stepping = true;
    }
}

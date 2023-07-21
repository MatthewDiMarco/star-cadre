package io.dim.spaceshooter.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.EntityFactory;
import io.dim.spaceshooter.model.ParallaxBackground;
import io.dim.spaceshooter.model.World;
import java.util.Stack;

public class PlayState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;

    private TextureAtlas textureAtlas;
    private final Texture[] backgrounds = new Texture[3];


    private ParallaxBackground parallaxBackground;
    private World world;

    public PlayState(
        OrthographicCamera camera,
        Viewport viewport,
        Stack<ApplicationState> manager) {
        super(camera, viewport, manager);
        init();
    }

    @Override
    public void handleInput() {
        if (Gdx.input.isKeyPressed(Keys.P)) {
            System.out.println("TODO: PAUSE"); // TODO
        }

        if (Gdx.input.isKeyPressed(Keys.R)) {
            init();
        }
    }

    @Override
    public void update(final float deltaTime) {
        parallaxBackground.scroll(deltaTime);
        world.updateEntities(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        parallaxBackground.draw(batch);
        world.drawEntities(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        for (Texture bg : backgrounds) {
            bg.dispose();
        }
        textureAtlas.dispose();
    }

    private void init() {
        backgrounds[0] = new Texture("backgrounds-0.png");
        backgrounds[1] = new Texture("backgrounds-1.png");
        backgrounds[2] = new Texture("backgrounds-2.png");
        parallaxBackground = new ParallaxBackground(
            backgrounds, WORLD_WIDTH, WORLD_HEIGHT,
            true, false);

        textureAtlas = new TextureAtlas("textures.atlas");
        world = new World(WORLD_WIDTH, WORLD_HEIGHT, new EntityFactory(textureAtlas));
        world.player = world.entityFactory.createPlayer(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT / 4, viewport);

        // temp
        world.aliens.add(world.entityFactory.createAlienBasic(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT + 10));
    }
}

package io.dim.spaceshooter.state;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool.PooledEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.EntityFactory;
import io.dim.spaceshooter.ParticleManager;
import io.dim.spaceshooter.model.ParallaxBackground;
import io.dim.spaceshooter.EntityManager;
import java.util.Stack;

public class PlayState extends ApplicationState {

    public static final int WORLD_WIDTH = 72;
    public static final int WORLD_HEIGHT = 128;

    private TextureAtlas textureAtlas;
    private final Texture[] backgrounds = new Texture[3];


    private ParallaxBackground parallaxBackground;
    private EntityManager entityManager;

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
        entityManager.updateEntities(deltaTime);
    }

    @Override
    public void render(SpriteBatch batch) {
        batch.begin();
        parallaxBackground.draw(batch);
        entityManager.drawEntities(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        textureAtlas.dispose();
        for (Texture bg : backgrounds) {
            bg.dispose();
        }
        // TODO particle manager?
    }

    private void init() {
        backgrounds[0] = new Texture("backgrounds-0.png");
        backgrounds[1] = new Texture("backgrounds-1.png");
        backgrounds[2] = new Texture("backgrounds-2.png");
        parallaxBackground = new ParallaxBackground(
            backgrounds, WORLD_WIDTH, WORLD_HEIGHT,
            true, false);

        textureAtlas = new TextureAtlas("textures.atlas");
        entityManager = new EntityManager(
            WORLD_WIDTH, WORLD_HEIGHT,
            new EntityFactory(textureAtlas),
            new ParticleManager(textureAtlas));
        entityManager.playerRef = entityManager.factory.createPlayer(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT / 4, viewport);
        entityManager.ships.add(entityManager.playerRef);

        // temp
        entityManager.ships.add(entityManager.factory.createAlienBasic(
            (float)WORLD_WIDTH / 2,
            (float)WORLD_HEIGHT + 10));

    }
}

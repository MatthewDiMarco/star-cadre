package io.dim.spaceshooter.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Stack;

public abstract class ApplicationState {

    protected OrthographicCamera camera;
    protected Viewport bgViewport;
    protected Viewport gameViewport;
    protected Stack<ApplicationState> manager;

    public ApplicationState(
        OrthographicCamera camera,
        Viewport bgViewport,
        Viewport gameViewport,
        Stack<ApplicationState> manager) {
        this.camera = camera;
        this.bgViewport = bgViewport;
        this.gameViewport = gameViewport;
        this.manager = manager;

    }

    public abstract void update(final float deltaTime);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();
}

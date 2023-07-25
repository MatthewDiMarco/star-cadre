package io.dim.spaceshooter.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.Viewport;
import java.util.Stack;

public abstract class ApplicationState {

    protected OrthographicCamera camera;
    protected Viewport viewport;
    protected Stack<ApplicationState> manager;

    public ApplicationState(
        OrthographicCamera camera,
        Viewport viewport,
        Stack<ApplicationState> manager) {
        this.camera = camera;
        this.viewport = viewport;
        this.manager = manager;

    }

    public abstract void update(final float deltaTime);

    public abstract void render(SpriteBatch batch);

    public abstract void dispose();
}

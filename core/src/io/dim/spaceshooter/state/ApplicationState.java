package io.dim.spaceshooter.state;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.ApplicationObject;
import java.util.Stack;

public abstract class ApplicationState implements ApplicationObject {

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

    public abstract void dispose();
}

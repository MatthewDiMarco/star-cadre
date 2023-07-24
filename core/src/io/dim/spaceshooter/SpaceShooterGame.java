package io.dim.spaceshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.state.ApplicationState;
import io.dim.spaceshooter.state.PlayState;
import java.util.Stack;

public class SpaceShooterGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport viewport;
	private Stack<ApplicationState> applicationStateManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		viewport = new StretchViewport(PlayState.WORLD_WIDTH, PlayState.WORLD_HEIGHT, camera);

		applicationStateManager = new Stack<>();
		applicationStateManager.push(new PlayState(camera, viewport, applicationStateManager));
	}

	@Override
	public void render() {
		applicationStateManager.peek().update(Gdx.graphics.getDeltaTime());
		applicationStateManager.peek().render(batch);

		if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
			Gdx.app.exit();
			System.exit(0);
		}
	}

	@Override
	public void resize(int width, int height) {
		viewport.update(width, height, true);
		batch.setProjectionMatrix(camera.combined);
	}

	@Override
	public void dispose() {
		batch.dispose();
		for (ApplicationState appState : applicationStateManager) {
			appState.dispose();
		}
	}
}

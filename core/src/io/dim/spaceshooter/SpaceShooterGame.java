package io.dim.spaceshooter;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import io.dim.spaceshooter.state.ApplicationState;
import io.dim.spaceshooter.state.GameState;
import java.util.Stack;

public class SpaceShooterGame extends ApplicationAdapter {

	private SpriteBatch batch;
	private OrthographicCamera camera;
	private Viewport bgViewport;
	private Viewport gameViewport;
	private Stack<ApplicationState> applicationStateManager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		camera = new OrthographicCamera();
		bgViewport = new ExtendViewport(GameState.WORLD_WIDTH, GameState.WORLD_HEIGHT, camera);
		gameViewport = new FitViewport(GameState.WORLD_WIDTH, GameState.WORLD_HEIGHT, camera);
		applicationStateManager = new Stack<>();
		applicationStateManager.push(
			new GameState(camera, bgViewport, gameViewport, applicationStateManager));
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
		bgViewport.update(width, height, true);
		gameViewport.update(width, height, true);
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

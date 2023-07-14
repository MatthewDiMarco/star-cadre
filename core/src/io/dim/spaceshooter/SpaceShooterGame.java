package io.dim.spaceshooter;

import com.badlogic.gdx.Game;
import java.util.Random;

public class SpaceShooterGame extends Game { // TODO refactor w/ custom game state manager

	GameScreen gameScreen;

	static Random random = new Random();

	@Override
	public void create() {
		gameScreen = new GameScreen();
		setScreen(gameScreen);
	}

	@Override
	public void render() {
		super.render();
	}

	@Override
	public void resize(int width, int height) {
		gameScreen.resize(width, height);
	}

	@Override
	public void dispose() {
		gameScreen.dispose();
	}
}

package io.dim.spaceshooter;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import io.dim.spaceshooter.SpaceShooterGame;

// Please note that on macOS your application needs to be started with the -XstartOnFirstThread JVM argument
public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("SpaceShooter");
		config.useVsync(true);
		config.setForegroundFPS(60);
		config.setWindowedMode(360, 640);
		new Lwjgl3Application(new SpaceShooterGame(), config);
	}
}

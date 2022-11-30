package de.cerus.cookieclicker.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import de.cerus.cookieclicker.CookieClickerGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(30);
		config.setTitle("Skittle Clicker");
		config.setWindowedMode(800, 480);
		config.useVsync(true);
		config.setWindowIcon("skittle_icon32.png");
		new Lwjgl3Application(new CookieClickerGame(), config);
	}
}
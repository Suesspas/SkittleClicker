package de.cerus.cookieclicker.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.cerus.cookieclicker.CookieClickerGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 30;
		config.title = "Skittle Clicker";
		config.width = 854;
		config.height = 480;
		config.addIcon("skittle_icon32.png", Files.FileType.Internal);
		config.addIcon("skittle_icon16.png", Files.FileType.Internal);

		new LwjglApplication(new CookieClickerGame(), config);
	}
}
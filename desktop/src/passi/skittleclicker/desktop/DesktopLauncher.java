package passi.skittleclicker.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import passi.skittleclicker.SkittleClickerGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setIdleFPS(30);
		config.setTitle("Skittle Clicker");
		config.setWindowedMode(800, 480);
		config.useVsync(true);
		config.setWindowSizeLimits(800,480,1920,1080);
		config.setWindowIcon("skittle_icon32.png");
		new Lwjgl3Application(new SkittleClickerGame(), config);
	}
}
package passi.skittleclicker.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
//import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import passi.skittleclicker.SkittleClickerGame;

public class DesktopLauncher {

	public static void main (String[] arg) {
//		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
//		config.setIdleFPS(30);
//		config.setTitle("Skittle Clicker");
//		config.setWindowedMode(800, 480);
//		config.useVsync(true);
//		config.setWindowSizeLimits(800,480,1920,1080);
//		config.setWindowIcon("skittle_icon32.png");
//		new Lwjgl3Application(new SkittleClickerGame(), config);
		//TODO for some reason new lwjgl3 does not shut down correctly on exit and does not save progress
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.backgroundFPS = 30;
		config.title = "Skittle Clicker";
		config.width = 854;
		config.height = 480;
		config.addIcon("skittle_icon32.png", Files.FileType.Internal);
		config.addIcon("skittle_icon16.png", Files.FileType.Internal);
		new LwjglApplication(new SkittleClickerGame(), config);
	}
}
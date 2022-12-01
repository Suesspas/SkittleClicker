package passi.skittleclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import passi.skittleclicker.screens.*;
import passi.skittleclicker.util.AppPreferences;
import passi.skittleclicker.util.ContributorUtil;
import passi.skittleclicker.util.FontUtil;

public class SkittleClickerGame extends Game {

    private SpriteBatch batch;

    private BitmapFont font;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int EXIT = 3;
    public final static int ENDGAME = 4;

    public MainMenuScreen menuScreen;
    public GameScreen gameScreen;
    public SettingsScreen settingsScreen;
    public PreferencesScreen preferencesScreen;
    private AppPreferences preferences;

    @Override
    public void create() {
        FontUtil.init();
        ContributorUtil.load();

        batch = new SpriteBatch();
        font = FontUtil.KOMIKA;
        preferences = new AppPreferences();

        setScreen(new MainMenuScreen(this));
    }

    @Override
    public void render() {
        if (screen == null || !(screen instanceof GameScreen)) {
            Gdx.gl.glClearColor(0, 0, 0, 1);
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        }

        super.render();
    }

    @Override
    public void dispose() {
        super.dispose();
        batch.dispose();
        font.dispose();
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public BitmapFont getFont() {
        return font;
    }

    public void deleteSaveData() {
        gameScreen.deleteSaveData();
    }

    public void changeScreen(int screen){
        System.out.println(screen); //TODO changing screens back to mainmenu doesnt work after newgame?
        switch(screen){
            case MENU:
                if(menuScreen == null) {
                    menuScreen = new MainMenuScreen(this);
                }
                this.setScreen(menuScreen);
                break;
            case PREFERENCES:
                if(preferencesScreen == null) {
                    preferencesScreen = new PreferencesScreen(this);
                }
                this.setScreen(preferencesScreen);
                break;
            case APPLICATION:
                if(gameScreen == null) {
                    gameScreen = new GameScreen(this);
                }
                this.setScreen(gameScreen);
                break;
//            case ENDGAME:
//                if(endScreen == null) {
//                    endScreen = new EndScreen(this);
//                }
//                this.setScreen(endScreen);
//                break;
            case EXIT:
                this.setScreen(new ExitScreen(this));
                break;
        }
    }

    public AppPreferences getPreferences() {
        return this.preferences;
    }
}
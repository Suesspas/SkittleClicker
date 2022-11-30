package de.cerus.cookieclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import de.cerus.cookieclicker.screens.GameScreen;
import de.cerus.cookieclicker.screens.MenuScreen;
import de.cerus.cookieclicker.util.ContributorUtil;
import de.cerus.cookieclicker.util.FontUtil;

public class CookieClickerGame extends Game {

    private SpriteBatch batch;

    private BitmapFont font;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int LOOT = 3;
    public final static int ENDGAME = 4;

    GameScreen gameScreen;

    @Override
    public void create() {
        FontUtil.init();
        ContributorUtil.load();

        batch = new SpriteBatch();
        font = FontUtil.KOMIKA;

        setScreen(new MenuScreen(this));
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
    }

    public void changeScreen(int screen){
        System.out.println(screen); //TODO changing screens back to mainmenu doesnt work after newgame?
        switch(screen){
//            case MENU:
//                if(mainMenuScreen == null) {
//                    mainMenuScreen = new MainMenuScreen(this);
//                }
//                this.setScreen(mainMenuScreen);
//                break;
//            case PREFERENCES:
//                if(preferencesScreen == null) {
//                    preferencesScreen = new PreferencesScreen(this);
//                }
//                this.setScreen(preferencesScreen);
//                break;
            case APPLICATION:
                if(gameScreen == null) {
                    gameScreen = new GameScreen(this, true);
                }
                this.setScreen(gameScreen);
                break;
//            case ENDGAME:
//                if(endScreen == null) {
//                    endScreen = new EndScreen(this);
//                }
//                this.setScreen(endScreen);
//                break;
//            case LOOT:
//                if(lootBoxScreen == null) {
//                    lootBoxScreen = new LootBoxScreen(this);
//                }
//                this.setScreen(lootBoxScreen);
//                break;
        }
    }
}
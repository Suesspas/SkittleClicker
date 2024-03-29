package passi.skittleclicker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import passi.skittleclicker.screens.*;
import passi.skittleclicker.util.AppPreferences;
import passi.skittleclicker.util.FontUtil;
import passi.skittleclicker.util.TextureUtil;

import java.util.ArrayList;
import java.util.List;

public class SkittleClickerGame extends Game {

    private SpriteBatch batch;

    private BitmapFont font;

    public final static int MENU = 0;
    public final static int PREFERENCES = 1;
    public final static int APPLICATION = 2;
    public final static int EXIT = 3;
    public final static int ENDGAME = 4;
    public final static int UNLOCKS = 5;

    public MainMenuScreen menuScreen;
    public GameScreen gameScreen;
    public PreferencesScreen preferencesScreen;
    private AppPreferences preferences;
    private TextureRegion[] backgrounds;
    private final List<Music> musicList = new ArrayList<>();

    @Override
    public void create() {
        FontUtil.init();

        batch = new SpriteBatch();
        font = FontUtil.FONT_30;
        preferences = new AppPreferences();
        String musicPath1 = "music/Faithful-Mission.mp3";
        musicList.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath1)));
        String musicPath2 = "music/Ghostrifter-Official-City-Lights.mp3";
        musicList.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath2)));
        String musicPath3 = "music/Morning-Routine-Lofi-Study-Music.mp3";
        musicList.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath3)));
        String musicPath4 = "music/purrple-cat-field-of-fireflies.mp3";
        musicList.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath4)));
        String musicPath5 = "music/When-I-Was-A-Boy.mp3";
        musicList.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath5)));
        String musicPath6 = "music/Where-The-Waves-Take-Us.mp3";
        musicList.add(Gdx.audio.newMusic(Gdx.files.internal(musicPath6)));

        backgrounds = TextureUtil.getTextureRegions( //wood_tavern.jpg
                new Texture("backgrounds/skittles_forest.jpg"), 2, 2);

        //trim edges from overlapping bgs
        backgrounds[0].setRegion(backgrounds[0].getRegionX(), backgrounds[0].getRegionY(),
                backgrounds[0].getRegionWidth()-10, backgrounds[0].getRegionHeight());
        backgrounds[1].setRegion(backgrounds[1].getRegionX()+4, backgrounds[1].getRegionY(),
                backgrounds[1].getRegionWidth(), backgrounds[1].getRegionHeight());
        backgrounds[2].setRegion(backgrounds[2].getRegionX(), backgrounds[2].getRegionY(),
                backgrounds[2].getRegionWidth()-10, backgrounds[2].getRegionHeight());


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
        for (Music m:
             musicList) {
            m.dispose();
        }
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
        System.out.println(screen);
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
            case ENDGAME:
                this.setScreen(new CreditsScreen(this, CreditsScreen.CREDITS));
                break;
            case EXIT:
                this.setScreen(new ExitScreen(this, ExitScreen.EXIT));
                break;
            case UNLOCKS:
                this.setScreen(new UnlocksScreen(this));
                break;
        }
    }
    private int musicChanges = 0;
    public Music getNextBGM(){
        Music nextMusic = musicList.get(musicChanges);
        musicChanges = ++musicChanges % musicList.size();
        return nextMusic;
    }

    public Music getCurrentBGM(){
        System.out.println("get current bgm: number " + ((musicChanges + musicList.size() - 1) % musicList.size()));
        return musicList.get((musicChanges + musicList.size() - 1) % musicList.size());
    }
    public AppPreferences getPreferences() {
        return this.preferences;
    }

    public void updateStageSkin() {
        gameScreen.updateStageSkin();
    }

    public void enableTestMode() {
        gameScreen.enableTestMode();
    }

    public TextureRegion getBackground(int i){
        if (i >= backgrounds.length) {
            System.err.println("Background " + i + "does not exist");
            return null;
        }
        return backgrounds[i];
    }
}
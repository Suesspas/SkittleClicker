package passi.skittleclicker.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.EventListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Slider;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;

public class PreferencesScreen implements Screen {

    final SkittleClickerGame game;
    private Stage stage;
    private Label titleLabel;
    private Label volumeMusicLabel;
    private Label volumeSoundLabel;
    private Label musicOnOffLabel;
    private Label soundOnOffLabel;
    private Label stageSkinLabel;
    private Label enableTestLabel;
    OrthographicCamera camera;
    private TextureRegion background; //= new Texture("backgrounds/Green_Nebula_07-1024x1024.png");

    public PreferencesScreen(final SkittleClickerGame gam) {
        game = gam;

//        camera = new OrthographicCamera();
//        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        /// create stage and set it as input processor
        stage = new Stage(new ScreenViewport());
        background = game.getBackground(0);
    }

    @Override
    public void show() {
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        // Create a table that fills the screen. Everything else will go inside
        // this table.
        Table table = new Table();
        table.setFillParent(true);
        //table.setDebug(true);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_glass/glassy-ui.json"

        // music volume
        final Slider musicSlider = new Slider(0f, 1f, 0.02f, false, skin);
        musicSlider.setValue(game.getPreferences().getMusicVolume());
        musicSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setMusicVolume(musicSlider.getValue());
                game.gameScreen.bgm.setVolume(game.getPreferences().getMusicVolume());
                // updateVolumeLabel();
                return false;
            }
        });

        // sound volume
        final Slider soundSlider = new Slider(0f, 1f, 0.02f, false, skin);
        soundSlider.setValue(game.getPreferences().getSoundVolume());
        soundSlider.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                game.getPreferences().setSoundVolume(soundSlider.getValue());
                //game.gameScreen.pickUpSound.setVolume(1,game.getPreferences().getMusicVolume());
                // updateVolumeLabel();
                return false;
            }
        });

        // music on/off
        final CheckBox musicCheckbox = new CheckBox(null, skin);
        musicCheckbox.setChecked(game.getPreferences().isMusicEnabled());
        musicCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = musicCheckbox.isChecked();
                game.getPreferences().setMusicEnabled(enabled);
                if (enabled)
                    game.gameScreen.bgm.play();
                else
                    game.gameScreen.bgm.pause();
                return false;
            }
        });

        // sound on/off
        final CheckBox soundEffectsCheckbox = new CheckBox(null, skin);
        soundEffectsCheckbox.setChecked(game.getPreferences().isSoundEffectsEnabled());
        soundEffectsCheckbox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                boolean enabled = soundEffectsCheckbox.isChecked();
                game.getPreferences().setSoundEffectsEnabled(enabled);
                return false;
            }
        });

        final CheckBox stageSkinCheckBox = new CheckBox("(requires restart)", skin);
        stageSkinCheckBox.setChecked(game.getPreferences().getStageSkin().equals("iron"));
        stageSkinCheckBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(stageSkinCheckBox.isChecked()) {
                    game.getPreferences().setStageSkin("iron");
                    game.updateStageSkin();
                } else {
                    game.getPreferences().setStageSkin("wood");
                    game.updateStageSkin();
                }
                return false;
            }
        });

        final CheckBox testModeCheckBox = new CheckBox("(gives you loads of skittles for testing)", skin);
        testModeCheckBox.addListener(new EventListener() {
            @Override
            public boolean handle(Event event) {
                if(testModeCheckBox.isChecked()) {
                    game.enableTestMode();
                }
                return false;
            }
        });

        // return to game and main menu screen buttons
        final TextButton backButton = new TextButton("To Game", skin);//"small" //stylename
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.APPLICATION);
            }
        });
        final TextButton menuButton = new TextButton("Main Menu", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.MENU);
            }
        });


        titleLabel = new Label( "Preferences", skin );
        volumeMusicLabel = new Label( "Music Volume", skin );
        volumeSoundLabel = new Label( "Sound Volume", skin );
        musicOnOffLabel = new Label( "Music enabled", skin );
        soundOnOffLabel = new Label( "Sound enabled", skin );
        stageSkinLabel = new Label("Stage Skin", skin);
        enableTestLabel = new Label("Test Mode", skin);

        int padding = 10;
        table.add(titleLabel).colspan(2);
        table.row().pad(padding,0,0,padding);
        table.add(volumeMusicLabel).left();
        table.add(musicSlider);
        table.row().pad(padding,0,0,padding);
        table.add(musicOnOffLabel).left();
        table.add(musicCheckbox);//.left();
        table.row().pad(padding,0,0,padding);
        table.add(volumeSoundLabel).left();
        table.add(soundSlider);
        table.row().pad(padding,0,0,padding);
        table.add(soundOnOffLabel).left();
        table.add(soundEffectsCheckbox);//.left();
        table.row().pad(padding,0,0,padding);
        table.add(stageSkinLabel).left();
        table.add(stageSkinCheckBox);
        table.row().pad(padding,0,0,padding);
        table.add(enableTestLabel).left();
        table.add(testModeCheckBox);
        table.row().pad(10,0,0,10);
        table.add(backButton).colspan(2);
        table.row().pad(10,0,0,10);
        table.add(menuButton).colspan(2);

    }

    @Override
    public void render(float delta) {
//        ScreenUtils.clear(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.getBatch().end();

        /*
        game.batch.begin();
        game.font.draw(game.batch, "Preferences screen", 100, 150);
        game.font.draw(game.batch, "Beep Boop", 100, 100);
        game.batch.end();
         */

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 30f));

        stage.draw();

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.changeScreen(SkittleClickerGame.MENU);
        }

/*
        if (Gdx.input.isTouched()) {
            game.changeScreen(2);
            dispose();
        }

 */
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void hide() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {
    }

    @Override
    public void dispose() {
        stage.dispose();
    }
}


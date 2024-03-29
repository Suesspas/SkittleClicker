package passi.skittleclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.util.ScreenUtil;
import passi.skittleclicker.util.TextureUtil;

public class MainMenuScreen implements Screen {

    private final SkittleClickerGame game;
    private OrthographicCamera camera;
    private Stage stage;
//    long aniTime = Long.MAX_VALUE;
    private ImageButton playGame;
    private ImageButton preferences;
    private ImageButton deleteSaveData;
    private ImageButton exit;
    private ImageButton unlocks;
    private ImageButton credits;
    private final Texture mousieHello;
    private final Texture mousieCheese;
    private final Texture mousieRage;
    private final Texture mousieCry;
    private final Texture mousieSSJ;
    private final Texture mousieShy;
    private static String layoutStyle;
    private final TextureRegion background;//new Texture("backgrounds/Blue_Nebula_02-1024x1024.png");

    public MainMenuScreen(final SkittleClickerGame gam) {
        game = gam;
        this.camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport());
        if (game.gameScreen == null)
            game.gameScreen = new GameScreen(game);
        layoutStyle = game.getPreferences().getStageSkin();
        background = game.getBackground(2);//3

        mousieHello = new Texture("mousieHello86.png");
        mousieCheese = new Texture("mousieCheese86.png");
        mousieRage = new Texture("mousieRage86.png");
        mousieCry = new Texture("mousieCry86.png");
        mousieSSJ = new Texture("mousieSSJ86.png");
        mousieShy = new Texture("mousieShy86.png");
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        // temporary until we have asset manager in


        Skin skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_neutralizer/neutralizer-ui.json"
        ImageButton.ImageButtonStyle imageButtonStyle = TextureUtil.getImageButtonStyle(layoutStyle, 350, 105);

//        ScrollPane scrollPane = new ScrollPane(table);
//        scrollPane.setFillParent(true);
//        stage.addActor(scrollPane);



        //create buttons
        playGame = new ImageButton(imageButtonStyle);
        preferences = new ImageButton(imageButtonStyle);
        deleteSaveData = new ImageButton(imageButtonStyle);
        exit = new ImageButton(imageButtonStyle);
        unlocks = new ImageButton(imageButtonStyle);

        credits = new ImageButton(imageButtonStyle);

        //add buttons to table
        table.add(playGame);
//        table.add(new Image(new Texture("mousieHello86.png")));
        table.row().pad(10, 0, 0, 0);
        table.add(preferences);
        table.row().pad(10, 0, 0, 0);;
        table.add(unlocks);
        table.row().pad(10, 0, 0, 0);;
        table.add(deleteSaveData).fillX().uniformX();
        table.row().pad(10, 0, 0, 0);
        table.add(credits);
        table.row().pad(10, 0, 0, 0);
        table.add(exit);

//        table.row().pad(10, 0, 10, 0);
//        table.add(test).fillX().uniformX();
//        table.row().pad(10, 0, 10, 0);
//        table.add(test).fillX().uniformX();

        // create button listeners
        exit.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.EXIT);
//                Gdx.app.exit();
            }
        });

        unlocks.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.UNLOCKS);
            }
        });

        playGame.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.APPLICATION);
            }
        });

        deleteSaveData.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new ExitScreen(game, ExitScreen.DELETE_DATA));
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.PREFERENCES);
            }
        });
        credits.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.ENDGAME);
            }
        });

    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        game.getBatch().end();
//        game.getBatch().begin();
        //game.batch.draw(background,0,0);
//        game.getFont().draw(game.getBatch(), "Skittle Clicker Game v0.0.1 ", Gdx.graphics.getWidth() - 200, 20);
//        game.getFont().draw(game.getBatch(), ""+delta, Gdx.graphics.getWidth() - 200, 50);
//        game.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

        game.getBatch().begin();
        drawButtonOverlay(playGame, mousieHello, "Play Game");
        drawButtonOverlay(preferences, mousieCheese, "Preferences");
        drawButtonOverlay(deleteSaveData, mousieRage, "Delete Data");
        drawButtonOverlay(credits, mousieShy, "Credits");
        drawButtonOverlay(unlocks, mousieSSJ, "Unlocks");
        drawButtonOverlay(exit, mousieCry, "Exit");
        game.getBatch().end();
//        if (System.currentTimeMillis() - aniTime > 500){
//            stage.clear();
//            if (game.gameScreen != null ){
//                game.gameScreen.dispose();
//                game.gameScreen= null;
//            }
//            game.changeScreen(Claw.APPLICATION);
//        }
        /*
        if (Gdx.input.isTouched()) {
            game.changeScreen(2);
            dispose();
        }
        */
    }

    private void drawButtonOverlay(Button button, Texture overlayTexture, String name) {
        int xPadding = 40;
        Vector2 buttonScreenCoords = ScreenUtil.getScreenCoords(button, camera);
        if (button.isOver()){
            game.getBatch().draw(overlayTexture,
                    buttonScreenCoords.x + button.getWidth() - mousieHello.getWidth() - xPadding + 10,
                    buttonScreenCoords.y -button.getHeight() + 10);
        }
        game.getFont().draw(game.getBatch(), name, buttonScreenCoords.x + xPadding, buttonScreenCoords.y - button.getHeight()/2.5f);
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

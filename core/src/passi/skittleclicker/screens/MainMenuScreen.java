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

    final SkittleClickerGame game;
    OrthographicCamera camera;
    private Stage stage;
    Texture background;
    long aniTime = Long.MAX_VALUE;
    ImageButton playGame;
    ImageButton preferences;
    ImageButton deleteSaveData;
    ImageButton exit;
    ImageButton test;
    Texture mousieHello;
    Texture mousieCheese;
    Texture mousieRage;
    Texture mousieCry;

    public MainMenuScreen(final SkittleClickerGame gam) {
        game = gam;
        this.camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport());
        if (game.gameScreen == null)
            game.gameScreen = new GameScreen(game);
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
        mousieHello = new Texture("mousieHello86.png");
        mousieCheese = new Texture("mousieCheese86.png");
        mousieRage = new Texture("mousieRage86.png");
        mousieCry = new Texture("mousieCry86.png");

        Skin skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_neutralizer/neutralizer-ui.json"
        String imageUpPath = "button_iron.png";
        String imageDownPath = "button_iron_shadow.png";
        String imageMouseOverPath = "button_iron_light.png";
        int width = 350;
        int height= 105;
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(TextureUtil.scaleImage(imageUpPath,  width, height)));
        Drawable drawablePressed = new TextureRegionDrawable(new TextureRegion(TextureUtil.scaleImage(imageDownPath, width, height)));
        Drawable drawableMouseOver = new TextureRegionDrawable(new TextureRegion(TextureUtil.scaleImage(imageMouseOverPath, width, height)));
//        GreyedOutImageButton shopButton = new GreyedOutImageButton(drawable, drawablePressed, shader);
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(null,null,null, drawable, drawablePressed,null);
        imageButtonStyle.imageOver = drawableMouseOver;

//        ScrollPane scrollPane = new ScrollPane(table);
//        scrollPane.setFillParent(true);
//        stage.addActor(scrollPane);

        background = new Texture(Gdx.files.internal("mousiePop2.png"));

        //create buttons
        playGame = new ImageButton(imageButtonStyle);
        preferences = new ImageButton(imageButtonStyle);
        deleteSaveData = new ImageButton(imageButtonStyle);
        exit = new ImageButton(imageButtonStyle);
        test = new ImageButton(imageButtonStyle);

        //add buttons to table
        table.add(playGame);
//        table.add(new Image(new Texture("mousieHello86.png")));
        table.row().pad(10, 0, 10, 0);
        table.add(preferences);
        table.row();
        table.add(deleteSaveData).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(exit);
        table.row().pad(10, 0, 10, 0);
        table.add(test);
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
                game.deleteSaveData();
                System.out.println("Save Data deleted");
            }
        });

        preferences.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.PREFERENCES);
            }
        });
        test.addListener(new ChangeListener() {
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
        drawButtonOverlay(exit, mousieCry, "Exit");
        drawButtonOverlay(test, mousieCheese, "Test");
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
        game.getBatch().draw(overlayTexture, buttonScreenCoords.x + button.getWidth() - mousieHello.getWidth() - xPadding + 10, buttonScreenCoords.y -button.getHeight() + 10);
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

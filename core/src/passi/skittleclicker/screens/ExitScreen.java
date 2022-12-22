package passi.skittleclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;

public class ExitScreen implements Screen {

    private final SkittleClickerGame game;
    private final OrthographicCamera camera;
    private final Stage stage;
    public static final int EXIT = 0;
    public static final int DELETE_DATA = 1;
    private int action;

    public ExitScreen(SkittleClickerGame game, int action) {
        this.game = game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        this.action = action;
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        game.gameScreen.saveProgress();

        Gdx.input.setInputProcessor(stage);
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(true);
        table.setDebug(false);
        stage.addActor(table);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_neutralizer/neutralizer-ui.json"

        Label label = new Label(action == DELETE_DATA ? "Are you sure you want to delete ALL your save data permanently?"
                : "Are you sure you want to exit?" , skin);
        label.setColor(Color.WHITE);

        //create buttons
        TextButton yesButton = new TextButton("Yes", skin);
        TextButton noButton = new TextButton("No", skin);

        //add buttons to table
        table.add(label);
        table.row().pad(10, 0, 10, 0);
        table.add(yesButton).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(noButton).fillX().uniformX();

        // create button listeners

        yesButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                if (action == DELETE_DATA){
                    game.deleteSaveData();
                    stage.clear();
                    game.changeScreen(SkittleClickerGame.MENU);
                } else {
                    Gdx.app.exit();
                }
            }
        });

        noButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                stage.clear();
                game.changeScreen(SkittleClickerGame.MENU);
            }
        });

    }

    @Override
    public void render(float delta) {
        camera.update();
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {}
}
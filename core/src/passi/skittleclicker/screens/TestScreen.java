package passi.skittleclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.util.AutoFocusScrollPane;

public class TestScreen implements Screen {

    final SkittleClickerGame game;
    OrthographicCamera camera;
    private Stage stage;

    public TestScreen(final SkittleClickerGame gam) {
        game = gam;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        stage = new Stage(new ScreenViewport());
        if (game.gameScreen == null)
            game.gameScreen = new GameScreen(game);
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);

        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_neutralizer/neutralizer-ui.json"

        // Create a table that fills the screen. Everything else will go inside this table.
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setDebug(true);

        Table clickerPart = new Table();
        clickerPart.setFillParent(false);
        clickerPart.setDebug(false);
        clickerPart.add(new TextButton("Clicker part", skin));

        Table shopGroupDisplayPart = new Table();
        shopGroupDisplayPart.setFillParent(false);
        shopGroupDisplayPart.setDebug(false);
        shopGroupDisplayPart.add(new TextButton("Shop Group display part", skin));

        Table table = new Table();
        table.setFillParent(false);
        table.setDebug(false);
//        stage.addActor(table);

//        ScrollPane scrollPane = new ScrollPane(table);
//        scrollPane.setFillParent(true);
//        stage.addActor(scrollPane);

        AutoFocusScrollPane autoFocusScrollPane = new AutoFocusScrollPane(table);
//        autoFocusScrollPane.setActor(table);
        autoFocusScrollPane.setFillParent(false);

        rootTable.add(clickerPart).expand();//maxWidth(10).expand();//.width(rootTable.getWidth()/3);
        rootTable.add(shopGroupDisplayPart).expand();
        rootTable.add(autoFocusScrollPane).expand();

        stage.addActor(rootTable);

        //create buttons
        TextButton playGame = new TextButton("Play Game", skin);
        TextButton preferences = new TextButton("Preferences", skin);
        TextButton deleteSaveData = new TextButton("Delete Save Data", skin);
        TextButton exit = new TextButton("Exit", skin);
//        TextButton loot = new TextButton("Loot Boxes", skin);

        //add buttons to table
        table.add(playGame).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(preferences).fillX().uniformX();
        table.row();
        table.add(deleteSaveData).fillX().uniformX();
        table.row().pad(10, 0, 10, 0);
        table.add(exit).fillX().uniformX();
        for (int i = 0; i < 10; i++) {
            table.row().pad(10, 0, 10, 0);
            table.add(new TextButton("Button " + i, skin)).fillX().uniformX();
        }

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

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0.2f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
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

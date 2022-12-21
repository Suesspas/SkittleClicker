package passi.skittleclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.util.AutoFocusScrollPane;
import passi.skittleclicker.util.FontUtil;
import passi.skittleclicker.util.GreyedOutImageButton;
import passi.skittleclicker.util.TextureUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

public class UnlocksScreen implements Screen {

    private final SkittleClickerGame game;
    private OrthographicCamera camera;
    private Stage stage;
    private final TextureRegion background;
    private static String layoutStyle;
    List<GreyedOutImageButton> upgradeButtons;
    boolean noUnlocks = true;

    public UnlocksScreen(final SkittleClickerGame gam) {
        game = gam;
        this.camera = new OrthographicCamera();
        stage = new Stage(new ScreenViewport());
        if (game.gameScreen == null)
            game.gameScreen = new GameScreen(game);
        layoutStyle = game.getPreferences().getStageSkin();
        background = game.getBackground(2);//3
        upgradeButtons = new ArrayList<>();
    }
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        // Create a table that fills the screen. Everything else will go inside this table.
        Table table = new Table();
        table.setFillParent(false);
        table.setDebug(false);
        table.right();

        AutoFocusScrollPane scrollPane = new AutoFocusScrollPane(table);
        scrollPane.setFlickScroll(true); //disables scroll by drag which scrolled horizontally
        scrollPane.setFillParent(true);
        scrollPane.setDebug(false);
        scrollPane.setOverscroll(false, false);
        stage.addActor(scrollPane);

        int buttonsPerRow = 5;
        int index = 0;
        List<GreyedOutImageButton> upButtons = game.gameScreen.getUpgradeButtons();
        for (GreyedOutImageButton button:
                upButtons) {
            GreyedOutImageButton upgradeButton = new GreyedOutImageButton(button);
            upgradeButton.setDisabled(true);
            boolean visible = game.gameScreen.isUpgradeVisible(upButtons.indexOf(button));
            upgradeButton.setVisible(visible);
            noUnlocks = noUnlocks && !visible;
            table.add(upgradeButton).right();
            upgradeButtons.add(upgradeButton);
            index++;
            if (index >= buttonsPerRow){
                table.row().pad(10, 0, 10, 0);
                index = 0;
            }
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        Gdx.gl.glClearColor(0f, 0f, 0f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            dispose();
            game.setScreen(new MainMenuScreen(game));
        }

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);
        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        if (noUnlocks) {
            FontUtil.FONT_60.draw(game.getBatch(), "No unlocks yet :(",
                    camera.viewportWidth/2 - 200, camera.viewportHeight/2);
        }
        game.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

        for (int i = 0; i < upgradeButtons.size(); i++) {

            if(upgradeButtons.get(i).isOver()){
                game.gameScreen.renderToolTip(i + 11, 200, camera.viewportHeight-Gdx.input.getY());
            }
        }
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

/*
 *  Copyright (c) 2018 Cerus
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Cerus
 *
 */

package passi.skittleclicker.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.data.Data;
import passi.skittleclicker.fixes.CustomShapeRenderer;
import passi.skittleclicker.objects.MiniSkittle;
import passi.skittleclicker.objects.Shop;
import passi.skittleclicker.util.AutoFocusScrollPane;
import passi.skittleclicker.util.FontUtil;
import passi.skittleclicker.util.GreyedOutImageButton;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScreen implements Screen {

    private static float SKITTLE_WIDTH = 200;
    private static float SKITTLE_HEIGHT = 200;
    private static float SKITTLE_MAX_WIDTH = 200;
    private static float SKITTLE_MAX_HEIGHT = 200;

    private CustomShapeRenderer shapeRenderer;

    private SkittleClickerGame game;
    private OrthographicCamera camera;
    private Shop shop;
    private DecimalFormat format;

    private Texture skittleTexture;

    private List<Texture> miniSkittleTextures;
    private Texture shopTexture;
    private Texture clickerTexture;

    private Ellipse skittleRepresentation;
    private Rectangle shopRepresentation;
    public Music bgm;

    private double skittlesPerSecond;
    private int clickerAnimationIndex;

    private Queue<MiniSkittle> skittles = new ConcurrentLinkedQueue<>();
    private int amountMiniSkittles;
    private static int MINISKITTLE_WIDTH = 25;
    private static int MINISKITTLE_HEIGHT = 25;
    private static float MINISKITTLE_THRESHOLD = -1;
    private static float MINISKITTLE_SPEED = 0.4f;//0.8f;
    private static float MINISKITTLE_ROTATION_SPEED = 0.25f;
    private final int NUMBER_OF_SHOPGROUPS = 10;

    private float generalRotation = 0;

    private int clicksPerSecond = 0;
    private ScheduledExecutorService service;

    private int autoSaveTimer;

    private Stage stage;
    private final ShaderProgram shader;

    List<ClickListener> clickListeners; //TODO add to all buttons

    public GameScreen(SkittleClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.shop = new Shop();
        this.stage = new Stage(new ScreenViewport());
        this.format = new DecimalFormat("#,###");
        // temporary until we have asset manager in
        Skin skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_neutralizer/neutralizer-ui.json"

        this.shader = new ShaderProgram(Gdx.files.internal("grey.vsh"), Gdx.files.internal("grey.fsh"));

        this.skittleTexture = new Texture(Gdx.files.internal("big_skittle.png"));
//        this.skittleTexture = scaleImage("big_skittle.png", 100, 100);
        this.miniSkittleTextures = new ArrayList<>();
        miniSkittleTextures.add(new Texture(Gdx.files.internal("skittle_red.png")));
        miniSkittleTextures.add(new Texture(Gdx.files.internal("skittle_green.png")));
        miniSkittleTextures.add(new Texture(Gdx.files.internal("skittle_purple.png")));

        this.shopTexture = new Texture(Gdx.files.internal("shop.png"));
        this.clickerTexture = new Texture(Gdx.files.internal("pointer.png"));
//        this.clickerTexture = scaleImage("pointer.png", 3, 3);

        this.skittleRepresentation = new Ellipse();
        this.shopRepresentation = new Rectangle();

        this.shapeRenderer = new CustomShapeRenderer();

        bgm = Gdx.audio.newMusic(Gdx.files.internal("arcade.wav"));
        bgm.setLooping(true);
        bgm.setVolume(game.getPreferences().getMusicVolume());
        if (game.getPreferences().isMusicEnabled()) {
            bgm.play();
        } else {
            bgm.stop();
        }

        this.clickListeners = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_SHOPGROUPS; i++) {
            clickListeners.add(new ClickListener());
        }

        this.autoSaveTimer = 0;

//        if (continueGame) {
        loadDataForShop();
//        }

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveProgress));
    }

    void saveProgress(){
        Data.saveProgress(shop.getSkittles(),
                shop.getClickerNumber(),
                shop.getGrannyNumber(),
                shop.getBakeryNumber(),
                shop.getFactoryNumber());
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        clickerAnimationIndex = -1;
        clicksPerSecond = 0;


        Gdx.input.setInputProcessor(stage);


        // Create a table that fills the screen. Everything else will go inside this table.
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setDebug(true);

        Table clickerTable = new Table();
        clickerTable.setFillParent(false);
        clickerTable.setDebug(false);
//        clickerTable.add(new TextButton("Clicker part", skin));

        Table imageTable = new Table();
        imageTable.setFillParent(false);
        imageTable.setDebug(false);
        for (int i = 0; i < 20; i++) {
            imageTable.add(new Image(new Texture("mousieHello86.png")));
            imageTable.row();
        }

        Table shopTable = new Table();
        shopTable.setFillParent(false);
        shopTable.setDebug(false);


        AutoFocusScrollPane scrollImageTable = new AutoFocusScrollPane(imageTable);
        AutoFocusScrollPane scrollShopTable = new AutoFocusScrollPane(shopTable);
        scrollShopTable.setFillParent(false);

//        rootTable.add(new Image(new Texture("mousieHello86.png"))).expand().fill().maxHeight(100).colspan(3);
//        rootTable.row();
        rootTable.add(clickerTable).expand().uniform();
        rootTable.add(scrollImageTable).expand().uniform();
        rootTable.add(scrollShopTable).expand().uniform().fill();

        stage.addActor(rootTable);

        //create buttons
        ImageButton imageButton1; //TODO load individual image Buttons
        ImageButton imageButton2;
        List<GreyedOutImageButton> shopButtons = new ArrayList<>();

        //add buttons to table
        for (int i = 0; i < NUMBER_OF_SHOPGROUPS; i++) {
            shopTable.row().pad(10, 0, 10, 0);
            shopButtons.add(setupShopButton(clickListeners.get(i), "imageButtonTest.png",
                    "imageButtonTestPressed.png",1000, 100));
            shopTable.add(shopButtons.get(i)).fillX();
            if (i % 2 == 0) shopButtons.get(i).setIsGreyedOut(true);
        }

        service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() / 4);

        scheduleService(service);
    }

    private GreyedOutImageButton setupShopButton(ClickListener clickListener, String imageUpPath, String imageDownPath, int width, int height) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(scaleImage(imageUpPath,  width, height)));
        Drawable drawablePressed = new TextureRegionDrawable(new TextureRegion(scaleImage(imageDownPath, width, height)));
        GreyedOutImageButton shopButton = new GreyedOutImageButton(drawable, drawablePressed, shader);
//        TextTooltip tooltip = new TextTooltip("You can press this", skin);
//        tooltip.setInstant(true);
//        shopButton.addListener(tooltip); The problem with tooltips is they are text only and not modifiable for updated quantities
        //TODO setVisible(false) for not yet unlocked

        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("Pressed ");
            }
        });
        shopButton.addListener(clickListener);
        return shopButton;
    }

    @Override
    public void render(float delta) {
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            service.shutdown();
            game.changeScreen(SkittleClickerGame.MENU);
            saveProgress();
        }


        Gdx.gl.glClearColor(0.21f/* + b*/, 0.53f/* + b*/, 0.70f/* + b*/, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);


        // Render mini skittles and tooltips
        game.getBatch().begin();
        renderSkittles();

        for (int i = 0; i < clickListeners.size(); i++) {
            if(clickListeners.get(i).isOver()){
                renderToolTip(i);
            }
        }
        game.getBatch().end();

        renderClicker();

        // Render shop and info bar
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(Color.LIGHT_GRAY);
//        shapeRenderer.roundedRect(camera.position.x - (camera.viewportWidth / 4f),
//                camera.position.y + (camera.viewportHeight / 2f) - 45,
//                camera.viewportWidth / 2f, 40, 10);
//        shapeRenderer.end();

        // Render everything else
        game.getBatch().begin();

//        shopRepresentation.set(camera.position.x + (camera.viewportWidth / 4f) - 45,
//                camera.position.y + (camera.viewportHeight / 2f) - 42, 35, 35);
//        game.getBatch().draw(shopTexture, shopRepresentation.x, shopRepresentation.y,
//                shopRepresentation.width, shopRepresentation.height);

        skittleRepresentation.set((camera.viewportWidth / 6f) - (SKITTLE_WIDTH / 2f) - (SKITTLE_WIDTH < 200 ? 5 : 0),
                (camera.viewportHeight / 2f) - (SKITTLE_HEIGHT / 2f) - (SKITTLE_HEIGHT < 200 ? 5 : 0), SKITTLE_WIDTH, SKITTLE_HEIGHT);
        game.getBatch().draw(skittleTexture, skittleRepresentation.x, skittleRepresentation.y,
                SKITTLE_WIDTH, SKITTLE_HEIGHT);

        game.getFont().draw(game.getBatch(), "Skittles per second: " + skittlesPerSecond,
                camera.position.x - (camera.viewportWidth / 2f) + 10,
                camera.position.y - (camera.viewportHeight / 2f) + 100);
        game.getFont().draw(game.getBatch(), "Rendered skittles: " + amountMiniSkittles,
                camera.position.x - (camera.viewportWidth / 2f) + 10,
                camera.position.y - (camera.viewportHeight / 2f) + 70);
        game.getFont().draw(game.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(),
                camera.position.x - (camera.viewportWidth / 2f) + 10,
                camera.position.y - (camera.viewportHeight / 2f) + 40);

        game.getFont().draw(game.getBatch(), "Skittles: " + format.format(shop.getSkittles()),
                camera.position.x - (camera.viewportWidth / 2f) + 10,
                camera.viewportHeight - 40);

        game.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

//        shop.render(game, camera);

        // Remove disappeared skittles
        skittles.forEach(miniSkittle -> {
            if (miniSkittle.getY() <= -MINISKITTLE_HEIGHT) {
                skittles.remove(miniSkittle);
                amountMiniSkittles--;
            }
        });

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && skittleRepresentation.contains(getUnprojectedScreenCoords(100))
                && shop.isNotVisible()) {
            SKITTLE_HEIGHT -= 10;
            SKITTLE_WIDTH -= 10;

            shop.click();
            clicksPerSecond++;
            addSkittle();
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && SKITTLE_WIDTH < 200 && SKITTLE_HEIGHT < 200
                && shop.isNotVisible()) {
            SKITTLE_WIDTH = 200;
            SKITTLE_HEIGHT = 200;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && shopRepresentation.contains(getUnprojectedScreenCoords(0))
                && shop.isNotVisible()) {
            shop.setVisible(shop.isNotVisible());
        }
    }

    private void renderToolTip(int i) {
        System.out.println("Mouse over "+i);
    }

    private void loadDataForShop() {
        Object[] objects = Data.loadProgress();
        shop.setupShop(objects);
//        shop.setSkittles(objects[0] instanceof Long ? (Long) objects[0] : (Integer) objects[0]);
//        shop.setClicker(objects[1] instanceof Long ? (Long) objects[1] : (Integer) objects[1]);
//        shop.setGrandmas(objects[2] instanceof Long ? (Long) objects[2] : (Integer) objects[2]);
//        shop.setBakeries(objects[3] instanceof Long ? (Long) objects[3] : (Integer) objects[3]);
//        shop.setFactories(objects[4] instanceof Long ? (Long) objects[4] : (Integer) objects[4]);
    }

    private void scheduleService(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shop.updateSkittles();
            skittlesPerSecond = shop.getSkittlesPerSecond() + clicksPerSecond;
            addSkittle();
            clicksPerSecond = 0;

            autoSaveTimer++;
            if (autoSaveTimer > 60){
                saveProgress();
                autoSaveTimer = 0;
                System.out.println("auto saved");
            }
        }, 1000, 1000, TimeUnit.MILLISECONDS);
    }

    private Vector2 getUnprojectedScreenCoords(float minus) {
        Vector3 screenCoords = new Vector3();
        screenCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(screenCoords);

        return new Vector2(screenCoords.x - minus, screenCoords.y - minus);
    }

    private float getAngle(Vector2 source, Vector2 target) {
        float angle = (float) Math.toDegrees(Math.atan2(target.y - source.y, target.x - source.x));

        if (angle < 0) {
            angle += 360;
        }

        return angle;
    }

    private void renderClicker() {
        if (shop.getClickerNumber() == 0) {
            return;
        }

        generalRotation += 0.1f;
        int row = 0;

        for (int i = 0; i < shop.getClickerNumber(); i++) {
            if (i == 18 || i == 36) {
                row++;
            }

            float rotation = i * 20 + generalRotation;
            if (rotation > 360) {
                rotation = rotation % 360;
            }

            double angle = Math.toRadians(rotation);

            game.getBatch().begin();
            Vector2 center = new Vector2(
                    skittleRepresentation.x + SKITTLE_MAX_WIDTH / 2f,
                    skittleRepresentation.y + SKITTLE_MAX_HEIGHT / 2f
            );

            float x1 = skittleRepresentation.x + SKITTLE_MAX_WIDTH / 2f - center.x;
            float y1 = center.y - SKITTLE_MAX_HEIGHT / 2f - ((30 + (50 * row)) - (clickerAnimationIndex == i ? 10 : 0)) - center.y;

            float x2 = (float) (x1 * Math.cos(angle) - y1 * Math.sin(angle));
            float y2 = (float) (x1 * Math.sin(angle) + y1 * Math.cos(angle));

            Vector2 vec = new Vector2();
            vec.x = x2 + center.x;
            vec.y = y2 + center.y;

            game.getBatch().draw(
                    clickerTexture,
                    vec.x - (clickerTexture.getWidth() / 50f) / 2f,
                    vec.y - (clickerTexture.getHeight() / 50f) / 2f,
                    (clickerTexture.getWidth() / 50f) / 2f,
                    (clickerTexture.getHeight() / 50f) / 2f,
                    clickerTexture.getWidth() / 50f,
                    clickerTexture.getHeight() / 50f,
                    1,
                    1,
                    getAngle(
                            center,
                            new Vector2(
                                    vec.x - (clickerTexture.getWidth() / 50f) / 2f,
                                    vec.y - (clickerTexture.getHeight() / 50f) / 2f
                            )
                    ) + 90,
                    0,
                    0,
                    clickerTexture.getWidth(),
                    clickerTexture.getHeight(),
                    false,
                    false
            );
            game.getBatch().end();
        }
    }

    private void renderSkittles() {
        skittles.forEach(miniSkittle -> {
            Texture miniSkittleTexture;
            switch (miniSkittle.getColor()){
                case RED: miniSkittleTexture = miniSkittleTextures.get(MiniSkittle.Color.RED.ordinal());
                    break;
                case GREEN: miniSkittleTexture = miniSkittleTextures.get(MiniSkittle.Color.GREEN.ordinal());
                    break;
                case PURPLE: miniSkittleTexture = miniSkittleTextures.get(MiniSkittle.Color.PURPLE.ordinal());
                    break;
                default: miniSkittleTexture = skittleTexture;
            }
            game.getBatch().draw(miniSkittleTexture,
                    miniSkittle.getX_relativePos()*camera.viewportWidth/3,
                    miniSkittle.getY(),
                    MINISKITTLE_WIDTH, MINISKITTLE_HEIGHT);
            miniSkittle.setY(miniSkittle.getY() - MINISKITTLE_SPEED);
            miniSkittle.setRotation((miniSkittle.getRotation() + MINISKITTLE_ROTATION_SPEED) % 360.0f);
        });
    }

    private void addSkittle() {
        if (MINISKITTLE_THRESHOLD == -1 || amountMiniSkittles <= MINISKITTLE_THRESHOLD) {//TODO width of left cell instead of viewport
            skittles.add(new MiniSkittle(MathUtils.random(0, camera.viewportWidth - MINISKITTLE_WIDTH)/camera.viewportWidth,
                    camera.viewportHeight + MINISKITTLE_HEIGHT,
                    MathUtils.random(0.0f, 360.0f),
                    MathUtils.random(0, MiniSkittle.Color.values().length-1)));
            amountMiniSkittles++;
        }
    }

    static Texture scaleImage(String path, int width, int height){
        Pixmap original = new Pixmap(Gdx.files.internal(path));
        Pixmap scaled = new Pixmap(width, height, original.getFormat());
        scaled.drawPixmap(original,
                0, 0, original.getWidth(), original.getHeight(),
                0, 0, scaled.getWidth(), scaled.getHeight()
        );
        Texture texture = new Texture(scaled);
        original.dispose();
        scaled.dispose();
        return texture;
    }

    @Override
    public void resize(int width, int height) {
        camera.setToOrtho(false, width, height);
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
        System.out.println("hidden");
    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        shop.dispose();
        skittleTexture.dispose();
        clickerTexture.dispose();
        shopTexture.dispose();
        for (Texture t:
                miniSkittleTextures) {
            t.dispose();
        }
    }

    public void deleteSaveData() {
        shop.deleteSaveData();
    }
}
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
import com.badlogic.gdx.graphics.g2d.Sprite;
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
import passi.skittleclicker.objects.GoldenSkittle;
import passi.skittleclicker.objects.MiniSkittle;
import passi.skittleclicker.objects.Shop;
import passi.skittleclicker.objects.ShopGroup;
import passi.skittleclicker.util.AutoFocusScrollPane;
import passi.skittleclicker.util.GreyedOutImageButton;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScreen implements Screen {

    private static final int MAX_DISPLAYED_UPGRADES = 5;
    private static final float MAX_GOLDLIGHT_SCALE = 1.1f;
    private static final float MIN_GOLDLIGHT_SCALE = 0.9f;
    private boolean goldLightIsGrowing = true;
    private final float goldLightGrowSpeed = 0.001f;
    private static float SKITTLE_WIDTH = 200;
    private static float SKITTLE_HEIGHT = 200;
    private static final float LIGHT_RADIUS = 100;

    private final CustomShapeRenderer shapeRenderer;

    private final SkittleClickerGame game;
    private final OrthographicCamera camera;
    private Shop shop;
    private final DecimalFormat format;

    private final Texture skittleTexture;
    private final Texture goldenSkittleTexture;
    private final Texture goldLightTexture;
    private final Sprite goldLightSprite;
    private final List<Texture> miniSkittleTextures;
    private final Texture clickerTexture;

    private final Ellipse skittleRepresentation;
    private final Ellipse goldenSkittleRepresentation;
    public Music bgm;

    private double skittlesPerSecond;
    private int clickerAnimationIndex;

    private final Queue<MiniSkittle> miniSkittles = new ConcurrentLinkedQueue<>();
    private int amountMiniSkittles;
    private static final int MINISKITTLE_WIDTH = 25;
    private static final int MINISKITTLE_HEIGHT = 25;
    private static final float MINISKITTLE_SPEED = 0.4f;//0.8f;
    private static final float MINISKITTLE_ROTATION_SPEED = 0.25f;

    private float generalRotation = 0;

    private int clicksPerSecond = 0;
    private ScheduledExecutorService service;

    private int autoSaveTimer;
    private final Stage stage;
    private final ShaderProgram shader;

    List<ClickListener> clickListeners; //TODO add to all buttons

    List<GreyedOutImageButton> shopButtons;
    HorizontalGroup upgradeGroup = new HorizontalGroup();
    Image menuBar;
    Image storeTitle;

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
        this.goldenSkittleTexture = new Texture(Gdx.files.internal("skittle_green.png"));
        this.goldLightTexture = scaleImage("gold_light2.png", (int)(SKITTLE_WIDTH + (2*LIGHT_RADIUS)), (int)(SKITTLE_HEIGHT+ (2*LIGHT_RADIUS)));
        this.goldLightSprite = new Sprite(goldLightTexture);
        goldLightSprite.setOrigin(goldLightSprite.getWidth()/2,goldLightSprite.getHeight()/2);
//        this.skittleTexture = scaleImage("big_skittle.png", 100, 100);
        this.miniSkittleTextures = new ArrayList<>();
        miniSkittleTextures.add(new Texture(Gdx.files.internal("skittle_red.png")));
        miniSkittleTextures.add(new Texture(Gdx.files.internal("skittle_green.png")));
        miniSkittleTextures.add(new Texture(Gdx.files.internal("skittle_purple.png")));

        this.clickerTexture = new Texture(Gdx.files.internal("pointer.png"));
//        this.clickerTexture = scaleImage("pointer.png", 3, 3);

        this.skittleRepresentation = new Ellipse();
        this.goldenSkittleRepresentation = new Ellipse();

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
        int numberOfClickListeners = shop.numberOfShopGroups() + shop.numberOfUpgrades();
        for (int i = 0; i < numberOfClickListeners; i++) {
            clickListeners.add(new ClickListener());
        }

        this.shopButtons = new ArrayList<>();

        this.autoSaveTimer = 58;

//        if (continueGame) {
        loadDataForShop();
//        }

        setupStage();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveProgress));
    }

    private void setupStage() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setDebug(false);

        Table clickerTable = new Table();
        clickerTable.setFillParent(false);
        clickerTable.setDebug(false);
//        clickerTable.add(new TextButton("Clicker part", skin));

        Table imageTable = new Table();
        imageTable.setFillParent(false);
        imageTable.setDebug(false);

        Table middleTable = new Table();
        middleTable.setFillParent(false);
        middleTable.setDebug(false);

        Table shopTable = new Table();
        shopTable.setFillParent(false);
        shopTable.setDebug(false);

        AutoFocusScrollPane scrollImageTable = new AutoFocusScrollPane(imageTable);
        AutoFocusScrollPane scrollShopTable = new AutoFocusScrollPane(shopTable);
        scrollShopTable.setFlickScroll(true); //disables scroll by drag which scrolled horizontally
        scrollShopTable.setFillParent(false);
        scrollShopTable.setOverscroll(false, false);
        scrollShopTable.setDebug(false);
        scrollImageTable.setFlickScroll(true);
        scrollImageTable.setOverscroll(false, false);
        scrollImageTable.setFillParent(false);

        menuBar = new Image(new Texture("imageButtonTestError.png"));
        middleTable.add(menuBar).expand().fill().height(50);
        middleTable.row();
        middleTable.add(scrollImageTable).expand().fill();

//        rootTable.add(menuBar).expand().fill().maxHeight(100).colspan(5);
//        rootTable.row();
        Texture border = new Texture("wood_border.png");
        rootTable.add(clickerTable).expand().uniform();
        rootTable.add(new Image(border)).expandY().fillY();
        rootTable.add(middleTable).expand().uniform().fill();
        rootTable.add(new Image(border)).expandY().fillY();
        rootTable.add(scrollShopTable).width(500);

        stage.addActor(rootTable);

        //create buttons
        ImageButton imageButton1; //TODO load individual image Buttons
        ImageButton imageButton2;

        //add contents to tables

        Texture horizontalBorder = scaleImage("wood_border_horizontal.png", 390, 12);
        imageTable.add(new Image(horizontalBorder)).expandX().fillX();
        imageTable.row();

        for (int i = 0; i < 20; i++) {
            imageTable.add(new Image(scaleImage("valhalla_frame1.png", 500, 100))).fillX();
            imageTable.row();
            imageTable.add(new Image(horizontalBorder)).expandX().fillX();
            imageTable.row();
        }

        storeTitle = new Image(new Texture("imageButtonTestError.png"));
        shopTable.add(storeTitle).expand().fill().height(50).maxWidth(500);
        shopTable.row();
        shopTable.add(new Image(horizontalBorder)).expandX().fill().maxWidth(500);
        shopTable.row();
        shopTable.add(upgradeGroup).left().maxWidth(500);
        shopTable.row();
        shopTable.add(new Image(horizontalBorder)).expandX().fill().maxWidth(500);

        for (int i = 0; i < shop.numberOfShopGroups(); i++) {
            shopTable.row(); //.pad(10, 0, 10, 0);
            shopButtons.add(setupShopButton(clickListeners.get(i), "button_wood_mousie.png",
                    "button_wood_mousie2.png",500, 100, false));
            shopTable.add(shopButtons.get(i)).fill().expand().left().maxWidth(500);
        }

        int addedUpgrades = 0;
        for (int i = 0; i < shop.numberOfUpgrades(); i++) {
            shopButtons.add(setupShopButton(clickListeners.get(shop.numberOfShopGroups()+i), "upgrade_wood.png",
                    "upgrade_wood2.png", 100, 100, true));
            if (shop.getUpgrades().get(i).isUnlocked()){
                shop.displayedUpgrade(i);
            } else {
                if (addedUpgrades < MAX_DISPLAYED_UPGRADES) {
                    upgradeGroup.addActor(shopButtons.get(shop.numberOfShopGroups()+i));
                    shop.displayedUpgrade(i);
                    addedUpgrades++;
                }
            }
        }
    }

    void saveProgress(){
        List<Long> shopGroupCounts = new ArrayList<>();
        for (ShopGroup s:
                shop.getShopGroups()) {
            shopGroupCounts.add(s.getNumber());
        }
        Data.saveProgress(shop.getSkittles(), shopGroupCounts, shop.getUpgrades());
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        clickerAnimationIndex = -1;
        clicksPerSecond = 0;

        Gdx.input.setInputProcessor(stage);

        service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() / 4);

        scheduleService(service);
    }

    private GreyedOutImageButton setupShopButton(ClickListener clickListener, String imageUpPath, String imageDownPath, int width, int height, boolean isUpgrade) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(scaleImage(imageUpPath,  width, height)));
        Drawable drawablePressed = new TextureRegionDrawable(new TextureRegion(scaleImage(imageDownPath, width, height)));
        GreyedOutImageButton shopButton = new GreyedOutImageButton(drawable, drawablePressed, shader);

        //TODO setVisible(false) for not yet unlocked

        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) { //TODO make mapping instead of implicit pos in list
                int index = shopButtons.indexOf(shopButton);
                if (index < shop.numberOfShopGroups()){
                    ShopGroup shopGroup = shop.getShopGroups().get(index);
                    if (shopGroup.getNumber() < shopGroup.getMAX_NUMBER()
                            && shop.getSkittles() >= shopGroup.getCurrentCost()) {
                        shop.pay(shopGroup.getCurrentCost());
                        shopGroup.incrementNumber();
                    }
                } else {
                    index -= shop.numberOfShopGroups();
                     if (shop.getSkittles() >= shop.getUpgrades().get(index).getCost()){
                         System.out.println("Upgrade" + index +" purchased");
                         shop.unlockUpgrade(index);
                         shop.pay(shop.getUpgrade(index).getCost());
                         shopButton.remove();
                         addNewUpgradeButton();
                     }
                }
            }
        });
        shopButton.addListener(clickListener);
        return shopButton;
    }

    private void addNewUpgradeButton() {
        int i = shop.getNewUpgradeIndex();
        if (i == -1) {
            System.out.println("no new upgrades found");
        } else {
            System.out.println("removed and add button number " + (i+shop.numberOfShopGroups()));
            upgradeGroup.addActor(shopButtons.get(i + shop.numberOfShopGroups()));
            shop.displayedUpgrade(i);
        }
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
        if (GoldenSkittle.isInState(GoldenSkittle.State.ACTIVE)){
            game.getBatch().begin();
            renderGoldLight();
            game.getBatch().end();
        } else if (game.getBatch().getColor() != Color.WHITE){
            game.getBatch().setColor(Color.WHITE);
        }
        game.getBatch().begin();
        renderSkittles();
        renderBigSkittle();
        game.getBatch().end();

        renderClicker();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

        game.getBatch().begin();

        //draw overlay info
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
                camera.viewportHeight - menuBar.getHeight() - 10);
        game.getFont().draw(game.getBatch(), "Max long value " + Long.MAX_VALUE,
                camera.position.x - (camera.viewportWidth / 2f) + 10,
                camera.viewportHeight - menuBar.getHeight() - 40);

        int yOffset = 100;
        for (int i = 0; i < shop.numberOfShopGroups(); i++) {
            ShopGroup shopGroup = shop.getShopGroups().get(i);
            //draw debug info
            game.getFont().draw(game.getBatch(), shopGroup.getType()+" " + shopGroup.getNumber() + " / " + shopGroup.getMAX_NUMBER()
                            + " [Cost: "+ shopGroup.getCurrentCost()+"] skittles per sec " + shopGroup.getSkittlesPerSecond(),
                    camera.position.x - (camera.viewportWidth / 2.2f) - 50,
                    camera.position.y + (camera.viewportHeight / 2.2f) - 300 - yOffset
            );
            yOffset += 30;

            //render text on shop buttons and disable
            GreyedOutImageButton button = shopButtons.get(i);
            Vector2 buttonScreenCoords = getScreenCoords(button);
            game.getFont().draw(game.getBatch(), shopGroup.getType() + " " + shopGroup.getNumber(),
                        buttonScreenCoords.x,
                        buttonScreenCoords.y);
//            menuBar.draw(game.getBatch(), 1); //making sure text is rendered behind menu bar,obsolete with new menu
            button.setIsGreyedOut(shopGroup.getCurrentCost() > shop.getSkittles());
        }
        Vector2 shopNameScreenCoords = getScreenCoords(storeTitle);
        game.getFont().draw(game.getBatch(), "Shop", shopNameScreenCoords.x, shopNameScreenCoords.y);
        System.out.println("Shop drawn at " + shopNameScreenCoords.x + ", " + shopNameScreenCoords.y);

        for (Actor u:
             upgradeGroup.getChildren()) {
            GreyedOutImageButton button = (GreyedOutImageButton) u;
            int index = shopButtons.indexOf(button) - shop.numberOfShopGroups();
            button.setIsGreyedOut(shop.getUpgrade(index).getCost() > shop.getSkittles());
        }

        if (GoldenSkittle.isInState(GoldenSkittle.State.SKITTLE)){
            game.getBatch().draw(goldenSkittleTexture, goldenSkittleRepresentation.x, goldenSkittleRepresentation.y,
                    goldenSkittleRepresentation.width, goldenSkittleRepresentation.height);
        }
        game.getBatch().end();


        if (autoSaveTimer < 2){
            int width = 130;
            int height = 50;
            float x = (camera.viewportWidth/2) - ((float) width/2);
            float y = 10;
            renderRoundRect(x, y, width, height);
            game.getBatch().begin();
            game.getFont().draw(game.getBatch(), "AUTOSAVED", x, y+height);
            game.getBatch().end();
        }


        for (int i = 0; i < clickListeners.size(); i++) {
            if(clickListeners.get(i).isOver()){
                renderToolTip(i);
            }
        }


        // Remove disappeared skittles
        miniSkittles.forEach(miniSkittle -> {
            if (miniSkittle.getY() <= -MINISKITTLE_HEIGHT) {
                miniSkittles.remove(miniSkittle);
                amountMiniSkittles--;
            }
        });

        //Process user input
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && skittleRepresentation.contains(getUnprojectedScreenCoords(100))) {
            SKITTLE_HEIGHT -= 10;
            SKITTLE_WIDTH -= 10;

            shop.click();
            clicksPerSecond++;
            addSkittle();
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && SKITTLE_WIDTH < 200 && SKITTLE_HEIGHT < 200) {
            SKITTLE_WIDTH = 200;
            SKITTLE_HEIGHT = 200;
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && GoldenSkittle.isInState(GoldenSkittle.State.SKITTLE)
                && goldenSkittleRepresentation.contains(getUnprojectedScreenCoords(goldenSkittleTexture.getWidth()/2f))) {
//            clickedGoldenSkittle();
            System.out.println("clicked golden skittle");
            GoldenSkittle.clicked();
            shop.goldenActive(true);
        }
    }

    private void renderBigSkittle() {
        skittleRepresentation.set((camera.viewportWidth / 6f) - (SKITTLE_WIDTH / 2f) - (SKITTLE_WIDTH < 200 ? 5 : 0),
                (camera.viewportHeight / 2f) - (SKITTLE_HEIGHT / 2f) - (SKITTLE_HEIGHT < 200 ? 5 : 0), SKITTLE_WIDTH, SKITTLE_HEIGHT);
        game.getBatch().draw(skittleTexture, skittleRepresentation.x, skittleRepresentation.y,
                SKITTLE_WIDTH, SKITTLE_HEIGHT);
    }

    private void renderGoldLight(){
        Color c = game.getBatch().getColor();
        goldLightSprite.setPosition(skittleRepresentation.x - LIGHT_RADIUS, skittleRepresentation.y - LIGHT_RADIUS);
        game.getBatch().setColor(255, 220, 0, 0.3f);
        goldLightSprite.rotate(0.2f);
        if (goldLightIsGrowing){
            goldLightSprite.setScale(goldLightSprite.getScaleX() + goldLightGrowSpeed);
            if (goldLightSprite.getScaleX() > MAX_GOLDLIGHT_SCALE) goldLightIsGrowing = false;
        } else {
            goldLightSprite.setScale(goldLightSprite.getScaleX() - goldLightGrowSpeed);
            if (goldLightSprite.getScaleX() < MIN_GOLDLIGHT_SCALE) goldLightIsGrowing = true;
        }
        goldLightSprite.draw(game.getBatch());
        game.getBatch().setColor(c.r, c.g, c.b, 1f);
//        game.getBatch().setColor(Color.WHITE);
    }

    private Vector2 getScreenCoords(Actor actor){
        Vector2 vector = actor.localToScreenCoordinates(new Vector2(0, camera.viewportHeight + actor.getHeight()));
        vector.y = -vector.y;
        return vector;
    }

    private void renderToolTip(int i) {
        int width = 100;
        int height = 50;
        float x = (2 * camera.viewportWidth / 3) - width;
        float y = Math.max(0, getUnprojectedScreenCoords(0).y - height);
        renderRoundRect(x, y, width, height);

        String str;
        game.getBatch().begin();
        if (i < shop.numberOfShopGroups()){
            ShopGroup shopGroup = shop.getShopGroups().get(i);
            str = shopGroup.getType()+" " + shopGroup.getNumber() + " / " + shopGroup.getMAX_NUMBER() + " [Cost: "+ shopGroup.getCurrentCost()+"]";

        }  else {
            int upgradeIndex = i - shop.numberOfShopGroups();
            str = "Upgrade " + upgradeIndex + " [Cost: "+ shop.getUpgrade(upgradeIndex).getCost() +"]";
        }
        game.getFont().draw(game.getBatch(), str, x, y+height);
        game.getBatch().end();
    }

    private void renderRoundRect(float x, float y, int width, int height) {
        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(140 / 255f, 140 / 255f, 140 / 255f, 1.0f);
        shapeRenderer.roundedRect(x, y, width, height, 10);
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);
    }

    private void loadDataForShop() {
        List<Object> objects = Data.loadProgress(shop.numberOfShopGroups(), shop.numberOfUpgrades());
        shop.setupShop(objects);
        //TODO load upgrades
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
//            for (int j = 0; j < shop.getUpgrades().size(); j++) {
//                if (shop.getUpgrades().get(j).isUnlocked()) System.out.println(j);
//            }
            autoSaveTimer++;
            if (autoSaveTimer > 60){
                saveProgress();
                autoSaveTimer = 0;
                System.out.println("auto saved"); //TODO auto save popup
            }

            GoldenSkittle.incrementTimer();
            if (GoldenSkittle.isRespawnTime()){
                GoldenSkittle.respawn();
                goldenSkittleRepresentation.set(MathUtils.random(0, (2*camera.viewportWidth/3) - goldenSkittleTexture.getWidth()),
                        MathUtils.random(0, camera.viewportHeight - goldenSkittleTexture.getHeight()), goldenSkittleTexture.getWidth(), goldenSkittleTexture.getHeight());
            } else if (GoldenSkittle.isDespawnTime()){
                System.out.println("failed to get golden skittle");
                GoldenSkittle.despawn();
            } else if (GoldenSkittle.isActiveEndTime()){
                System.out.println("end of golden duration");
                GoldenSkittle.activeEnd();
                shop.goldenActive(false);
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
            float SKITTLE_MAX_WIDTH = 200;
            float SKITTLE_MAX_HEIGHT = 200;
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
        miniSkittles.forEach(miniSkittle -> {
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
        float MINISKITTLE_THRESHOLD = -1; //just in case the skittles bring bad performance
        if (MINISKITTLE_THRESHOLD == -1 || amountMiniSkittles <= MINISKITTLE_THRESHOLD) {
            miniSkittles.add(new MiniSkittle(MathUtils.random(0, camera.viewportWidth - MINISKITTLE_WIDTH)/camera.viewportWidth,
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
        skittleTexture.dispose();
        clickerTexture.dispose();
        for (Texture t:
                miniSkittleTextures) {
            t.dispose();
        }
        stage.dispose();
    }

    public void deleteSaveData() {
//        shop.deleteSaveData();
        shop = new Shop();
        int displayedUpgrades = upgradeGroup.getChildren().size;
        for (int i = 0; i < displayedUpgrades; i++) {
            upgradeGroup.removeActorAt(0, false);
        }
        for (int i = 0; i < MAX_DISPLAYED_UPGRADES; i++) {
            addNewUpgradeButton();
        }
    }
}
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
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.math.*;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Event;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.*;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.data.Data;
import passi.skittleclicker.fixes.CustomShapeRenderer;
import passi.skittleclicker.objects.*;
import passi.skittleclicker.util.*;

import java.text.DecimalFormat;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class GameScreen implements Screen{

    private static String layoutStyle = "iron";
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
    private final Texture greySkittleTexture;
    private final Texture goldenSkittleTexture;
    private final Texture goldLightTexture;
    private final Sprite goldLightSprite;
    private final List<Texture> miniSkittleTextures;
    private final Texture clickerTexture;

    private final Ellipse skittleRepresentation;
    private final Ellipse goldenSkittleRepresentation;
    public Music bgm;
    private final Sound clickSound;

    private long skittlesPerSecond;
    private int clickerAnimationIndex;

    private final Queue<MiniSkittle> miniSkittles = new ConcurrentLinkedQueue<>();
    private final Queue<ClickSkittle> clickSkittles = new ConcurrentLinkedQueue<>();
    private float generalRotation = 0;
    private int clicksPerSecond = 0;
    private ScheduledExecutorService service;
    private int autoSaveTimer;
    private final Stage stage;
    private final ShaderProgram shader;
    private final List<ClickListener> shopClickListeners;
//    private final List<ClickListener[]> imageClickListeners;
    private final List<GreyedOutImageButton> shopButtons;
    private final HorizontalGroup upgradeGroup = new HorizontalGroup();
    private Table menuBar;
    private final List<Image> dummyImageList;
    private final List<Image> borderList;
    private Image storeTitle;
    private Table clickerTable;
    private final Skin skin;
    private final boolean IS_DEBUG_ENABLED = true;
    private String borderVerticalPath = layoutStyle + "_border.png";
    private String borderHorizontalPath = layoutStyle + "_border_horizontal.png";
    private final Texture milkTexture;
    private final TextureRegion milkRegion;
    private final Texture valhallaFrameSheet;
    private final Animation<TextureRegion> valhallaAnimation;
    private final List<Texture> valhallaSelections;
    private final Texture borderHorizontalTexture;
    private float stateTime;
    private TextureRegion currentFrameValhalla;
    private final int MAX_VISIBLE_LOCKED_SHOPBUTTONS = 1;
    private int milkFrame;
    private final Texture darkBackground;
    private final Texture goldGlow;
    private final Texture overlayTexture;
    private final List<Image[]> imageRepresentations;
    private final List<Texture> imageBackgroundTextures;
    private final GlyphLayout shopTextLayout;
    private final GlyphLayout skittleNumberLayout;
    private final GlyphLayout skittleTextLayout;
    private final GlyphLayout skittlesPerSecLayout;
    private final GlyphLayout skittlesPerSecTextLayout;
    private final GlyphLayout clickSkittleTextLayout;
    private final GlyphLayout tooltipTextLayout;
    private final GlyphLayout tooltipTitleLayout;
    private final GlyphLayout autosaveTextLayout;
    private final GlyphLayout shopGroupNumberLayout;

    public GameScreen(SkittleClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.shop = new Shop();
        this.stage = new Stage(new ScreenViewport());
        this.stateTime = 0;
        this.milkFrame = 0;

        updateStageSkin();
        borderHorizontalTexture = new Texture(borderHorizontalPath);

        this.format = new DecimalFormat("#,###");
        // temporary until we have asset manager in
        skin = new Skin(Gdx.files.internal("skin_default/uiskin.json"));//"skin_neutralizer/neutralizer-ui.json"

        this.shader = new ShaderProgram(Gdx.files.internal("grey.vsh"), Gdx.files.internal("grey.fsh"));
//        this.lightShader = new ShaderProgram(Gdx.files.internal("light.vsh"), Gdx.files.internal("light.fsh"));

        this.valhallaFrameSheet = new Texture("valhalla_framegrid.png");
        TextureRegion[] valhallaFrames = TextureUtil.getTextureRegions(valhallaFrameSheet, 4, 6);
        valhallaAnimation = new Animation<>(0.12f,valhallaFrames);
//        currentFrameValhalla = valhallaAnimation.getKeyFrame(stateTime, true);
        this.valhallaSelections = new ArrayList<>();
        for (int i = 0; i <= 15; i++) {
            valhallaSelections.add(new Texture("selections/selection" + i + ".png"));
        }

        this.imageRepresentations = new ArrayList<>();
        this.imageBackgroundTextures = new ArrayList<>();

        this.goldGlow = new Texture("gold_glow2.png");
        this.darkBackground = new Texture("dark_background.png");
        this.milkTexture = new Texture("milk.png");
        this.milkRegion = new TextureRegion(milkTexture);
        this.skittleTexture = new Texture(Gdx.files.internal("big_skittle.png"));
        this.greySkittleTexture = new Texture("grey_skittle.png");
        this.goldenSkittleTexture = TextureUtil.scaleImage("golden_skittle.png",100,100);
        this.goldLightTexture = TextureUtil.scaleImage("gold_light4.png", (int)(SKITTLE_WIDTH + (2*LIGHT_RADIUS)), (int)(SKITTLE_HEIGHT+ (2*LIGHT_RADIUS)));
        this.goldLightSprite = new Sprite(goldLightTexture);
        goldLightSprite.setOrigin(goldLightSprite.getWidth()/2,goldLightSprite.getHeight()/2);
//        this.skittleTexture = scaleImage("big_skittle.png", 100, 100);
        this.miniSkittleTextures = new ArrayList<>();
        miniSkittleTextures.add(new Texture("skittle_red.png"));
        miniSkittleTextures.add(new Texture("skittle_green.png"));
        miniSkittleTextures.add(new Texture("skittle_purple.png"));
        this.overlayTexture = new Texture("overlay_button_" + layoutStyle + ".png");

        this.clickerTexture = new Texture(Gdx.files.internal("pointer_white.png"));
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

        shopTextLayout = new GlyphLayout();
        shopTextLayout.setText(game.getFont(), "Shop");
        skittleNumberLayout = new GlyphLayout();
        skittleTextLayout = new GlyphLayout();
        skittleTextLayout.setText(game.getFont(), "Skittles");
        skittlesPerSecLayout = new GlyphLayout();
        skittlesPerSecTextLayout = new GlyphLayout();
        skittlesPerSecTextLayout.setText(FontUtil.FONT_20, "Skittles per second");
        clickSkittleTextLayout = new GlyphLayout();
        tooltipTitleLayout = new GlyphLayout();
        tooltipTextLayout = new GlyphLayout();
        autosaveTextLayout = new GlyphLayout();
        autosaveTextLayout.setText(FontUtil.FONT_20, "Autosaved.");
        shopGroupNumberLayout = new GlyphLayout();

        clickSound = Gdx.audio.newSound(Gdx.files.internal("click.wav"));

        this.shopClickListeners = new ArrayList<>();
        int numberOfClickListeners = shop.numberOfShopGroups() + shop.numberOfUpgrades();
        for (int i = 0; i < numberOfClickListeners; i++) {
            shopClickListeners.add(new ClickListener());
        }
//        this.imageClickListeners = new ArrayList<>();
//        for (int i = 1; i < shop.numberOfShopGroups(); i++) {
//            ClickListener[] clickListeners = new ClickListener[(int)shop.getShopGroups().get(i).getMAX_NUMBER()];
//            for (int j = 0; j < clickListeners.length; j++) {
//                clickListeners[j] = new ClickListener();
//            }
//            imageClickListeners.add(clickListeners);
//        }

        this.shopButtons = new ArrayList<>();
        this.dummyImageList = new ArrayList<>();
        this.borderList= new ArrayList<>();

        this.autoSaveTimer = 58;

        loadDataForShop();

        setupStage();

        Runtime.getRuntime().addShutdownHook(new Thread(this::saveProgress));
    }

    private void setupStage() {
        // Create a table that fills the screen. Everything else will go inside this table.
        Table rootTable = new Table();
        rootTable.setFillParent(true);
        rootTable.setDebug(IS_DEBUG_ENABLED);

        clickerTable = new Table();
        clickerTable.setFillParent(false);
        clickerTable.setDebug(IS_DEBUG_ENABLED);
//        clickerTable.add(new TextButton("Clicker part", skin));

        Table imageTable = new Table();
        imageTable.setFillParent(false);
        imageTable.setDebug(IS_DEBUG_ENABLED);

        Table middleTable = new Table();
        middleTable.setFillParent(false);
        middleTable.setDebug(IS_DEBUG_ENABLED);

        Table shopTable = new Table();
        shopTable.setFillParent(false);
        shopTable.setDebug(IS_DEBUG_ENABLED);

        AutoFocusScrollPane scrollImageTable = new AutoFocusScrollPane(imageTable);
        AutoFocusScrollPane scrollShopTable = new AutoFocusScrollPane(shopTable);
        scrollShopTable.setFlickScroll(true); //disables scroll by drag which scrolled horizontally
        scrollShopTable.setFillParent(false);
        scrollShopTable.setOverscroll(false, false);
        scrollShopTable.setDebug(IS_DEBUG_ENABLED);
        scrollImageTable.setFlickScroll(true);
        scrollImageTable.setOverscroll(false, false);
        scrollImageTable.setFillParent(false);

        menuBar = new Table();//new Image(new Texture("imageButtonTestError.png"));
//        Drawable drawable = new TextureRegionDrawable(new TextureRegion(scaleImage("upgrade_wood.png", 50, 50)));
//        Drawable drawablePressed = new TextureRegionDrawable(new TextureRegion(scaleImage("upgrade_wood2.png", 50, 50)));
//        ImageButton menuButton1 = new ImageButton(drawable, drawablePressed); //alternative mit image button

        final TextButton menuButton = new TextButton("\n Main Menu \n", skin); //TODO de-scuff this, probably imagebutton without skin
        final TextButton prefButton = new TextButton("\n Preferences \n", skin);
        menuButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeScreen(SkittleClickerGame.MENU);
            }
        });
        prefButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                changeScreen(SkittleClickerGame.PREFERENCES);
            }
        });
//        menuButton.setHeight(70);
//        menuButton.setSize(50,50);
//        menuButton.setFillParent(true);
        menuBar.add(menuButton).expand().left();
        menuBar.add(new Image(overlayTexture)).expand();
        menuBar.add(prefButton).right();

        //add contents to tables
        Texture horizontalBorder = new Texture(borderHorizontalPath);//scaleImage(borderHorizontalPath, 390, 12);
        imageTable.add(new Image(horizontalBorder)).expandX().fillX().height(12);
        imageTable.row();

        //adding dummy alpha images to image table, because you cant easily replace table cells (used for scrolling and coords)
        Texture valhallaTexture = new Texture("valhalla.png");
        Texture borderPlaceholder = new Texture("placeholder_border_horizontal.png");
        double valTexScale = 0.75;
        for (int i = 0; i < shop.numberOfShopGroups()-1; i++) {
            Image image = new Image (TextureUtil.scaleImage("image_placeholder.png",
                    (int) Math.round(valhallaTexture.getWidth()*valTexScale),
                    (int) Math.round(valhallaTexture.getHeight()*valTexScale)));
            dummyImageList.add(image);
            imageTable.add(dummyImageList.get(i)).fillX();//scaleImage("valhalla_frame1.png", 500, 100)
            imageTable.row();
            Image border = new Image(borderPlaceholder);
            borderList.add(border);
            imageTable.add(borderList.get(i)).expandX().fillX().height(12);
            imageTable.row();
            setupImages(i, imageTable);
        }

        middleTable.add(menuBar).expand().fill().height(50);
        middleTable.row();
        middleTable.add(new Image(new Texture(borderHorizontalPath))).expand().fill();
        middleTable.row();
        middleTable.add(scrollImageTable).expand().fill();

        storeTitle = new Image(new Texture("storetitle_placeholder.png"));
        shopTable.add(storeTitle).expand().fill().height(50).maxWidth(500);
        shopTable.row();
        shopTable.add(new Image(horizontalBorder)).expandX().fill().maxWidth(500).height(12);
        shopTable.row();
        shopTable.add(upgradeGroup).left().maxWidth(500);
        shopTable.row();
        shopTable.add(new Image(horizontalBorder)).expandX().fill().maxWidth(500).height(12);

        int visibleLockedButtonCount = 0;
        for (int i = 0; i < shop.numberOfShopGroups(); i++) {
            shopTable.row(); //.pad(10, 0, 10, 0);
            shopButtons.add(setupShopButton(shopClickListeners.get(i), "button_" + layoutStyle + ".png",
                    "button_" + layoutStyle + "_shadow.png", "button_" + layoutStyle + "_light.png",
                    "shopgroups/" + shop.shopgroupTypeToString(shop.getShopGroups().get(i).getType()) + ".png"));
            shopTable.add(shopButtons.get(i)).fill().expand().left().maxWidth(500);
            //only show unlocked + max visible not unlocked shop group buttons
            if (shop.getShopGroups().get(i).getNumber() > 0){
                shopButtons.get(i).setVisible(true);
            } else {
                if (visibleLockedButtonCount < MAX_VISIBLE_LOCKED_SHOPBUTTONS){
                    shopButtons.get(i).setVisible(true);
                    visibleLockedButtonCount++;
                } else {
                    shopButtons.get(i).setVisible(false);
                }
            }
        }

        int addedUpgrades = 0;
        for (int i = 0; i < shop.numberOfUpgrades(); i++) {
            shopButtons.add(setupShopButton(shopClickListeners.get(shop.numberOfShopGroups()+i), "upgrade_" + layoutStyle + ".png",
                    "upgrade_" + layoutStyle + "_shadow.png", "upgrade_" + layoutStyle + "_light.png",
                    shop.getUpgrade(i).getImagePath()));
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

        Texture border = new Texture(borderVerticalPath);
        rootTable.add(clickerTable).expand().fill().minWidth(200);
        rootTable.add(new Image(border)).expandY().fillY();
        rootTable.add(middleTable).maxWidth((int) Math.round(valhallaTexture.getWidth()*valTexScale));
        rootTable.add(new Image(border)).expandY().fillY();
        rootTable.add(scrollShopTable).width(500);

        stage.addActor(rootTable);
    }

    private void setupImages(int index, Table table) {
        ShopGroup shopGroup = shop.getShopGroups().get(index + 1);
        String name = shop.shopgroupTypeToString(shopGroup.getType());
//        imageBackgroundTextures.add(new Texture("images/background_" + ((index%6)+1) + ".png"));
        imageBackgroundTextures.add(new Texture("images/background_test3.png"));
        //TODO add backgrounds
        Image[] images = new Image[(int)shopGroup.getMAX_NUMBER()];
        for (int i = 0; i < images.length; i++) {
            if (index == 1){
                images[i] = new Image(new Texture("images/ducks/" + name + "_" + (i+1) + ".png"));
            } else {
                images[i] = new Image(TextureUtil.scaleImage("images/" + name + ".png", 50, 50));
            }


//            images[i].setScale(0.1f);
//            images[i].addListener(imageClickListeners.get(index)[i]);
//            stage.addListener(imageClickListeners.get(index)[i]);
        }
        imageRepresentations.add(images);
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

        stateTime = 0;
        clickerAnimationIndex = -1;
        clicksPerSecond = 0;

        Gdx.input.setInputProcessor(stage);

        service = Executors.newScheduledThreadPool(Runtime.getRuntime().availableProcessors() / 4);

        scheduleService(service);
    }

    private GreyedOutImageButton setupShopButton(ClickListener clickListener, String imageUpPath, String imageDownPath,
                                                 String imageMouseOverPath, String iconPath) {
        Drawable drawable = new TextureRegionDrawable(new TextureRegion(TextureUtil.combineTextures(
                new Texture(imageUpPath), new Texture(iconPath))));
        Drawable drawablePressed = new TextureRegionDrawable(new TextureRegion(TextureUtil.combineTextures(
                new Texture(imageDownPath), new Texture(iconPath))));
        Drawable drawableMouseOver = new TextureRegionDrawable(new TextureRegion(TextureUtil.combineTextures(
                new Texture(imageMouseOverPath), new Texture(iconPath))));
//        GreyedOutImageButton shopButton = new GreyedOutImageButton(drawable, drawablePressed, shader);
        ImageButton.ImageButtonStyle imageButtonStyle = new ImageButton.ImageButtonStyle(null,null,null, drawable, drawablePressed,null);
        imageButtonStyle.imageOver = drawableMouseOver;
        GreyedOutImageButton shopButton = new GreyedOutImageButton(imageButtonStyle, shader);
        shopButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
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
            changeScreen(SkittleClickerGame.MENU);
        }

        if (stateTime < Float.MAX_VALUE/3 ){
            stateTime += Gdx.graphics.getDeltaTime();
        } else {
            stateTime = 0;
        }

        currentFrameValhalla = valhallaAnimation.getKeyFrame(stateTime, true);

//        Gdx.gl.glClearColor(0.21f/* + b*/, 0.53f/* + b*/, 0.70f/* + b*/, 1);
        Gdx.gl.glClearColor(0f, 0.1f, 0.2f, 1);
//        Gdx.gl.glClearColor(0.47f, 0.73f, 0.55f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        game.getBatch().begin();
        if (GoldenSkittle.isInState(GoldenSkittle.State.ACTIVE)){
            renderGoldLight();
        } else if (game.getBatch().getColor() != Color.WHITE){
            game.getBatch().setColor(Color.WHITE);
        }

        renderMiniSkittles();
        renderBigSkittle();
        renderClicker();

        if (game.getBatch().getColor() != Color.WHITE){
            game.getBatch().setColor(Color.WHITE);
        }

        renderMilk();
        drawImageTable();
        for (int i = 0; i < borderList.size(); i++) {
            Actor border = borderList.get(i);
            if (shop.getShopGroups().get(i+1).getNumber() > 0){
                drawActor(border, borderHorizontalTexture);
            }
        }
        game.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();

        game.getBatch().begin();

        //draw overlay info
        skittleNumberLayout.setText(game.getFont(), format.format(shop.getSkittles()));
        skittlesPerSecLayout.setText(FontUtil.FONT_20, format.format(skittlesPerSecond)); //

        game.getBatch().draw(darkBackground, 0, camera.viewportHeight - 220, clickerTable.getWidth(), 160);

        game.getFont().draw(game.getBatch(), skittleNumberLayout,
                (clickerTable.getWidth() - skittleNumberLayout.width)/2,
                camera.viewportHeight - menuBar.getHeight() - 30);

        game.getFont().draw(game.getBatch(), skittleTextLayout,
                (clickerTable.getWidth() - skittleTextLayout.width)/2,
                camera.viewportHeight - menuBar.getHeight() - 60);

        FontUtil.FONT_20.draw(game.getBatch(),  skittlesPerSecLayout,
                (clickerTable.getWidth() - skittlesPerSecLayout.width)/2,
                camera.viewportHeight - menuBar.getHeight() - 100);

        FontUtil.FONT_20.draw(game.getBatch(),  skittlesPerSecTextLayout,
                (clickerTable.getWidth() - skittlesPerSecTextLayout.width)/2,
                camera.viewportHeight - menuBar.getHeight() - 120);


        if (IS_DEBUG_ENABLED){
            game.getFont().draw(game.getBatch(), "Click Milk Mod " + shop.getMilkClicksMod(),
                    camera.position.x - (camera.viewportWidth / 2f) + 10,
                    camera.position.y - (camera.viewportHeight / 2f) + 100);
            game.getFont().draw(game.getBatch(), "Rendered skittles: " + miniSkittles.size(),
                    camera.position.x - (camera.viewportWidth / 2f) + 10,
                    camera.position.y - (camera.viewportHeight / 2f) + 70);
            game.getFont().draw(game.getBatch(), "FPS: " + Gdx.graphics.getFramesPerSecond(),
                    camera.position.x - (camera.viewportWidth / 2f) + 10,
                    camera.position.y - (camera.viewportHeight / 2f) + 40);
            game.getFont().draw(game.getBatch(), "Max long value " + Long.MAX_VALUE,
                    camera.position.x - (camera.viewportWidth / 2f) + 10,
                    camera.viewportHeight - menuBar.getHeight() - 40);
        }


        int yOffset = 100;
        int visibleLockedButtonCount = 0;
        for (int i = 0; i < shop.numberOfShopGroups(); i++) {
            ShopGroup shopGroup = shop.getShopGroups().get(i);
            //draw debug info
            if (IS_DEBUG_ENABLED){
                game.getFont().draw(game.getBatch(), shopGroup.getType()+" " + shopGroup.getNumber() + " / " + shopGroup.getMAX_NUMBER()
                                + " [Cost: "+ shopGroup.getCurrentCost()+"] skittles per sec " + shopGroup.getSkittlesPerSecond(),
                        camera.position.x - (camera.viewportWidth / 2.2f) - 50,
                        camera.position.y + (camera.viewportHeight / 2.2f) - 300 - yOffset
                );
                yOffset += 30;
            }

            //render text on shop buttons and grey out/set visible if needed
            GreyedOutImageButton button = shopButtons.get(i);
            if (button.isVisible()){
                if (shopGroup.getNumber() == 0) {
                    visibleLockedButtonCount++;
                }
                Vector2 buttonScreenCoords = ScreenUtil.getScreenCoords(button, camera);

                int yPadding = 25;
                game.getFont().draw(game.getBatch(), shop.shopgroupTypeToString(shopGroup.getType()),
                        buttonScreenCoords.x + 100,
                        buttonScreenCoords.y - yPadding);
                shopGroupNumberLayout.setText(FontUtil.FONT_60, shopGroup.getNumber()+"");
                FontUtil.FONT_60.draw(game.getBatch(), shopGroupNumberLayout,
                        buttonScreenCoords.x + button.getWidth() - shopGroupNumberLayout.width/2 - 70,
                        buttonScreenCoords.y - yPadding - 10);
                if (button.isGreyedOut()){
                    game.getBatch().draw(greySkittleTexture, buttonScreenCoords.x + 100,
                            buttonScreenCoords.y - (yPadding + 50), 20,20);
                } else {
                    game.getBatch().draw(skittleTexture, buttonScreenCoords.x + 100,
                            buttonScreenCoords.y - (yPadding + 50), 20,20);
                }

                FontUtil.FONT_20.draw(game.getBatch(), shopGroup.getCurrentCost() + "",
                        buttonScreenCoords.x + 125,
                        buttonScreenCoords.y - (yPadding + 32));

                button.setIsGreyedOut(shopGroup.getCurrentCost() > shop.getSkittles());
            } else {
                if (visibleLockedButtonCount < MAX_VISIBLE_LOCKED_SHOPBUTTONS){
                    button.setVisible(true);
                    button.setIsGreyedOut(shopGroup.getCurrentCost() > shop.getSkittles());
                    visibleLockedButtonCount++;
                }
            }
//            menuBar.draw(game.getBatch(), 1); //making sure text is rendered behind menu bar,obsolete with new menu
        }
        Vector2 shopNameScreenCoords = ScreenUtil.getScreenCoords(storeTitle, camera);

        FontUtil.FONT_30.draw(game.getBatch(),
                shopTextLayout,
                shopNameScreenCoords.x + storeTitle.getWidth()/2 - shopTextLayout.width/2,
                shopNameScreenCoords.y - storeTitle.getHeight()/3);

        for (Actor a:
             upgradeGroup.getChildren()) {
            GreyedOutImageButton button = (GreyedOutImageButton) a;
            int index = shopButtons.indexOf(button) - shop.numberOfShopGroups();
            button.setIsGreyedOut(shop.getUpgrade(index).getCost() > shop.getSkittles());
        }

//        renderMilk();
        renderClickSkittles();

        if (GoldenSkittle.isInState(GoldenSkittle.State.SKITTLE)){
            game.getBatch().draw(goldenSkittleTexture, goldenSkittleRepresentation.x, goldenSkittleRepresentation.y,
                    goldenSkittleRepresentation.width, goldenSkittleRepresentation.height);
        } else if (GoldenSkittle.isInState(GoldenSkittle.State.ACTIVE)) {
            game.getBatch().draw(goldGlow, 0, 0, clickerTable.getWidth(), clickerTable.getHeight());
        }

        game.getBatch().end();


        if (autoSaveTimer < 2){
            int x = Math.round((camera.viewportWidth - autosaveTextLayout.width)/2);
            int y = 15;
            int padding = 10;
//            renderRoundRect(x, y, autosaveTextLayout.width, autosaveTextLayout.height);
            game.getBatch().begin();
            game.getBatch().draw(overlayTexture, x- padding, y - padding, autosaveTextLayout.width + 2*padding, y + autosaveTextLayout.height + padding);
            FontUtil.FONT_20.draw(game.getBatch(), autosaveTextLayout, x, y + autosaveTextLayout.height);
            game.getBatch().end();
        }


        for (int i = 0; i < shopClickListeners.size(); i++) {
            if(shopClickListeners.get(i).isOver()){
                renderToolTip(i);
            }
        }


        // Remove disappeared skittles
        miniSkittles.forEach(miniSkittle -> {
            if (miniSkittle.getY() <= -MiniSkittle.HEIGHT) {
                miniSkittles.remove(miniSkittle);
            }
        });

        clickSkittles.forEach(clickSkittle -> {
            if (!clickSkittle.isAlive()) {
                clickSkittles.remove(clickSkittle);
            }
        });

        //Process user input
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && skittleRepresentation.contains(getUnprojectedScreenCoords(100))) {
            SKITTLE_HEIGHT -= 10;
            SKITTLE_WIDTH -= 10;
            clickSound.play(game.getPreferences().getSoundVolume() * 0.3f);
            shop.click();
            clicksPerSecond++;
            addSkittle();
            addClickSkittle();
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
        Vector2 mousePos = getUnprojectedScreenCoords(0);
        for (Image[] i:
             imageRepresentations) {
            for (int j = 0; j < i.length; j++) {
//                if ( i[j].isTouchFocusTarget()) System.out.println("MOUSE IS OVER");
                if (mousePos.x > i[j].getX() && mousePos.x < i[j].getX() + i[j].getWidth()*i[j].getScaleX()
                && mousePos.y > i[j].getY() && mousePos.y < i[j].getY() + i[j].getHeight()*i[j].getScaleY()){
                    System.out.println(imageRepresentations.indexOf(i) + ", " + j); //TODO show names of twitch subs / channels in tooltip
                }
            }
        }
    }

    private void renderMilk() {
        float milkSpeed = 1f; //if lower than 1 it causes milk to be rendered less than framerate
        int milkX = Math.round(milkFrame * milkSpeed);
        float relativeTableWidth = clickerTable.getWidth() / milkTexture.getWidth();
        int MAX_MILK_FRAMES = milkTexture.getWidth();
        float yPos = -100 + 100 * (float)shop.getMilkClicksPercent();

        milkFrame++;
        milkRegion.setRegion(milkTexture.getWidth() - milkX, 0, (int)Math.min(clickerTable.getWidth(), milkX), milkTexture.getHeight());
        if (MilkState.getCurrentState() != null){
            game.getBatch().setColor(MilkState.getCurrentStateColor());
            game.getBatch().draw(milkRegion, 0, yPos);
            for (int i = 0; i < relativeTableWidth; i++) {
                int width = Math.min((int)clickerTable.getWidth() - (milkTexture.getWidth()*i) - milkX, milkTexture.getWidth());
                width = Math.max(width, 0);
                milkRegion.setRegion(0, 0, width, milkTexture.getHeight());
                game.getBatch().draw(milkRegion, (milkTexture.getWidth()*i) + milkX, yPos);
            }
        }
        if (milkX >= MAX_MILK_FRAMES){
            milkFrame = 0;
        }
        game.getBatch().setColor(Color.WHITE);
    }

    private void drawImageTable() {
        int index = 1;
        for (Actor dummyImage:
                dummyImageList) {
            if (index >= shop.numberOfShopGroups()) break;
            int number = (int) shop.getShopGroups().get(index).getNumber();
//            drawActor(dummyImage, valhallaTexture);
            if (number > 0) {
                if (index == 4){ // valhalla is 4th shopgroup
                    drawAnimation(dummyImage, currentFrameValhalla);
                    drawActor(dummyImage, valhallaSelections.get(Math.min(number, valhallaSelections.size()-1)));
                } else if (index == 2) {
                    drawActor(dummyImage, imageBackgroundTextures.get(index - 1));
                    Image duckImage = imageRepresentations.get(index - 1)[number-1];
                    Vector2 borderScreenCoords = ScreenUtil.getScreenCoords(dummyImage, camera);
                    duckImage.setPosition(borderScreenCoords.x + dummyImage.getWidth()/2 -duckImage.getWidth()/2,borderScreenCoords.y - duckImage.getHeight());
                    duckImage.draw(game.getBatch(), 1);
                } else {
                    drawActor(dummyImage, imageBackgroundTextures.get(index - 1));
                    drawImages(dummyImage, imageRepresentations.get(index - 1), number);
                }
            }

//            Actor actor = imageTableList.get(index); // for animation
//            Animation = new ?
//            drawActor(dummyImage, texture);
            index++;
        }
    }

    private void drawImages(Actor a, Image[] images, int number) {
        Vector2 borderScreenCoords = ScreenUtil.getScreenCoords(a, camera);
        if (borderScreenCoords.y < camera.viewportHeight + a.getHeight() && borderScreenCoords.y > -a.getHeight()){
//            game.getBatch().draw(image);
            float xOffset = 10 + borderScreenCoords.x;
            float yOffset = 10 + borderScreenCoords.y;
            for (int i = 0; i < number; i++) {
                if (i < 12){
                    images[i].setPosition( xOffset+ 50*i,  yOffset- a.getHeight() + 50); //TODO set pos
                } else {
                    images[i].setPosition(xOffset + 50* (i - 12), yOffset - a.getHeight());
                }

                images[i].draw(game.getBatch(), 1);
            }
        }
    }

    private void drawActor(Actor a, Texture texture) {
        //Needed for changing number of rendered images, because you cant just replace table images
        Vector2 borderScreenCoords = ScreenUtil.getScreenCoords(a, camera);
        if (borderScreenCoords.y < camera.viewportHeight + a.getHeight() && borderScreenCoords.y > -a.getHeight()){
            game.getBatch().draw(texture,
                    borderScreenCoords.x,
                    borderScreenCoords.y - a.getHeight(),
                    a.getWidth(),
                    a.getHeight());
        }
    }

    private void drawAnimation(Actor a, TextureRegion textureRegion) {
        //Needed for changing number of rendered images, because you cant just replace table images
        Vector2 borderScreenCoords = ScreenUtil.getScreenCoords(a, camera);
        if (borderScreenCoords.y < camera.viewportHeight + a.getHeight() && borderScreenCoords.y > -a.getHeight()){
            game.getBatch().draw(textureRegion,
                    borderScreenCoords.x,
                    borderScreenCoords.y - a.getHeight(),
                    a.getWidth(),
                    a.getHeight());

        }
    }

    private void changeScreen(int screenNumber) {
        service.shutdown();
        game.changeScreen(screenNumber);
        saveProgress();
    }

    private void renderBigSkittle() {
        skittleRepresentation.set((clickerTable.getWidth()/2) - (SKITTLE_WIDTH / 2f) - (SKITTLE_WIDTH < 200 ? 5 : 0),
                (camera.viewportHeight / 2f) - (SKITTLE_HEIGHT / 2f) - (SKITTLE_HEIGHT < 200 ? 5 : 0), SKITTLE_WIDTH, SKITTLE_HEIGHT);
        game.getBatch().draw(skittleTexture, skittleRepresentation.x, skittleRepresentation.y,
                SKITTLE_WIDTH, SKITTLE_HEIGHT);
    }

    private void renderGoldLight(){
        Color c = game.getBatch().getColor();
        goldLightSprite.setPosition(skittleRepresentation.x - LIGHT_RADIUS, skittleRepresentation.y - LIGHT_RADIUS);
        game.getBatch().setColor(255, 220, 0, 0.3f); //TODO maybe replace with actual shader for left screen part ar one point
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

    private void renderToolTip(int i) {
        String title;
        String text;
        if (i < shop.numberOfShopGroups()){
            ShopGroup shopGroup = shop.getShopGroups().get(i);
            title = shop.shopgroupTypeToString(shopGroup.getType()) +" " + shopGroup.getNumber() + " / " + shopGroup.getMAX_NUMBER()
                    + " [Cost: "+ shopGroup.getCurrentCost() +"]";
            text = shopGroup.getText();
        }  else {
            int upgradeIndex = i - shop.numberOfShopGroups();
            title = "Upgrade " + upgradeIndex + " [Cost: "+ shop.getUpgrade(upgradeIndex).getCost() +"]";
            text = shop.getUpgrade(upgradeIndex).getText();
        }
        tooltipTitleLayout.setText(FontUtil.FONT_30, title);
        tooltipTextLayout.setText(FontUtil.FONT_20, text);
        float padding = 50;
        float width = Math.max(tooltipTitleLayout.width, tooltipTextLayout.width);
        float height = tooltipTitleLayout.height + tooltipTextLayout.height;
        float x = camera.viewportWidth - shopButtons.get(0).getWidth() - width;
        float y = Math.max(2*padding, getUnprojectedScreenCoords(0).y - height);
//        renderRoundRect(x - 2*padding, y - 2*padding, width + 2*padding, height + 2*padding);
        game.getBatch().begin();
//        game.getBatch().draw(overlayTexture, x - 2*padding, y - 2*padding, width + 2*padding, height + 2*padding);

        int borderSize = 10;
        TextureUtil.drawPartOfSprite(game.getBatch(), overlayTexture,
                x - 2*padding, y - 2*padding,
                width + 2*padding, height + 2*padding, borderSize, borderSize,
                overlayTexture.getWidth() - 2*borderSize, overlayTexture.getHeight() - 2*borderSize);

        //left border
        TextureUtil.drawPartOfSprite(game.getBatch(), overlayTexture,
                x - 2*padding, y - 2*padding, borderSize,
                height + 2*padding, 0, borderSize,
                borderSize, overlayTexture.getHeight() - 2*borderSize);
        //right border
        TextureUtil.drawPartOfSprite(game.getBatch(), overlayTexture,
                x + width - borderSize, y - 2*padding, borderSize,
                height + 2*padding, overlayTexture.getWidth() - borderSize, borderSize,
                borderSize, overlayTexture.getHeight() - 2*borderSize);
        //top border
        TextureUtil.drawPartOfSprite(game.getBatch(), overlayTexture,
                x - 2*padding, y + height - borderSize, width + 2*padding,
                borderSize, 0, overlayTexture.getHeight() - borderSize,
                overlayTexture.getWidth(), borderSize);
        //bottom border
        TextureUtil.drawPartOfSprite(game.getBatch(), overlayTexture,
                x - 2* padding, y - 2*padding, width + 2*padding,
                borderSize, 0, overlayTexture.getHeight() - borderSize,
                overlayTexture.getWidth(), borderSize);

        FontUtil.FONT_30.draw(game.getBatch(), tooltipTitleLayout, x - padding, y + height - 30);
        FontUtil.FONT_20.draw(game.getBatch(), tooltipTextLayout, x - padding, y + height - 75);
        game.getBatch().end();
    }

//    private void renderRoundRect(float x, float y, float width, float height) {
//        Gdx.gl.glEnable(GL20.GL_BLEND);
//        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
//        shapeRenderer.setProjectionMatrix(camera.combined);
//        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
//        shapeRenderer.setColor(155 / 255f, 155 / 255f, 155 / 255f, 1.0f);
//        shapeRenderer.roundedRect(x, y, width, height, 10);
//        shapeRenderer.end();
//        Gdx.gl.glDisable(GL20.GL_BLEND);
//    }

    private void loadDataForShop() {
        List<Object> objects = Data.loadProgress(shop.numberOfShopGroups(), shop.numberOfUpgrades());
        shop.setupShop(objects);
    }

    private void scheduleService(ScheduledExecutorService service) {
        service.scheduleAtFixedRate(() -> {
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            shop.updateSkittles();
            skittlesPerSecond = shop.getSkittlesPerSecond() + Math.round(clicksPerSecond * shop.getClickModifier());
            addSkittle();
            clicksPerSecond = 0;
//            for (int j = 0; j < shop.getUpgrades().size(); j++) {
//                if (shop.getUpgrades().get(j).isUnlocked()) System.out.println(j);
//            }
            autoSaveTimer++;
            if (autoSaveTimer > 60){
                saveProgress();
                autoSaveTimer = 0;
                System.out.println("auto saved");
            } else  if (autoSaveTimer % 5 == 0){
                shop.milkClickTimeDecrement();
            }

            GoldenSkittle.incrementTimer();
            if (GoldenSkittle.isRespawnTime()){
                GoldenSkittle.respawn();
                goldenSkittleRepresentation.set(MathUtils.random(0, (2*camera.viewportWidth/3) - goldenSkittleTexture.getWidth()),
                        MathUtils.random(0, camera.viewportHeight - goldenSkittleTexture.getHeight() - menuBar.getHeight()),
                        goldenSkittleTexture.getWidth(),
                        goldenSkittleTexture.getHeight());
            } else if (GoldenSkittle.isDespawnTime()){
                System.out.println("failed to get golden skittle");
                GoldenSkittle.despawn();
            } else if (GoldenSkittle.isActiveEndTime()){
                System.out.println("end of golden duration");
                GoldenSkittle.activeEnd();
                shop.goldenActive(false);
            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
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

//            game.getBatch().begin();
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
//            game.getBatch().end();
        }
    }

    private void renderMiniSkittles() {
        miniSkittles.forEach(miniSkittle -> {
            renderSkittles(miniSkittle);
            miniSkittle.setY(miniSkittle.getY() - MiniSkittle.SPEED);
            miniSkittle.setRotation((miniSkittle.getRotation() + MiniSkittle.ROTATION_SPEED) % 360.0f);
        });
    }

    private void renderClickSkittles(){
        clickSkittles.forEach(clickSkittle -> {
            clickSkittleTextLayout.setText(FontUtil.FONT_20, "+"+format.format(clickSkittle.getSkittleAmount()));
            renderSkittles(clickSkittle);
            FontUtil.FONT_20.draw(game.getBatch(),
                    clickSkittleTextLayout,
                    clickSkittle.getOriginX() - clickSkittleTextLayout.width/2,
                    3*clickSkittle.getOriginY() - 2*clickSkittle.getY() + 50); // change 3* and 2* for higher/lower fall speed (must stay exactly 1 apart)
            clickSkittle.update();
        });
    }

    private void renderSkittles(MiniSkittle miniSkittle) {
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
        float xPos = miniSkittle instanceof ClickSkittle ? miniSkittle.getX() : miniSkittle.getX()/camera.viewportWidth*clickerTable.getWidth();
        game.getBatch().draw(miniSkittleTexture,
                xPos,
                miniSkittle.getY(),
                MiniSkittle.WIDTH, MiniSkittle.HEIGHT);
    }

    private void addSkittle() {
        int MINISKITTLE_THRESHOLD = -1; //just in case the skittles bring bad performance
        if (MINISKITTLE_THRESHOLD == -1 || miniSkittles.size() <= MINISKITTLE_THRESHOLD) {
            miniSkittles.add(new MiniSkittle(MathUtils.random(0, camera.viewportWidth - MiniSkittle.WIDTH),
                    camera.viewportHeight + MiniSkittle.HEIGHT,
                    MathUtils.random(0.0f, 360.0f),
                    MathUtils.random(0, MiniSkittle.Color.values().length-1)));
        }
    }

    private void addClickSkittle() {
        int CLICKSKITTLE_THRESHOLD = -1; //just in case the skittles bring bad performance
        if (CLICKSKITTLE_THRESHOLD == -1 || clickSkittles.size() <= CLICKSKITTLE_THRESHOLD) {
            Vector2 pos = getUnprojectedScreenCoords(0);
            clickSkittles.add(new ClickSkittle(pos.x - ClickSkittle.WIDTH/2f,
                    pos.y - ClickSkittle.HEIGHT/2f,
                    MathUtils.random(0.0f, 360.0f),
                    MathUtils.random(0, MiniSkittle.Color.values().length-1),
                    shop.getSkittlesPerClick()));
        }
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
        MilkState.deleteSaveData();
        int displayedUpgrades = upgradeGroup.getChildren().size;
        for (int i = 0; i < shop.numberOfShopGroups(); i++) {
            shopButtons.get(i).setVisible(i < MAX_VISIBLE_LOCKED_SHOPBUTTONS);
        }
        for (int i = 0; i < displayedUpgrades; i++) {
            upgradeGroup.removeActorAt(0, false);
        }
        for (int i = 0; i < MAX_DISPLAYED_UPGRADES; i++) {
            addNewUpgradeButton();
        }
    }

    public void updateStageSkin() { // doesnt really do anything because textures inside tables are not changed
        layoutStyle = game.getPreferences().getStageSkin();
        borderVerticalPath = layoutStyle + "_border.png";
        borderHorizontalPath = layoutStyle + "_border_horizontal.png";
    }
}
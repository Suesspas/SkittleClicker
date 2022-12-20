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
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.util.*;

import java.util.ArrayList;
import java.util.List;

public class CreditsScreen implements Screen {
    private SkittleClickerGame game;
    private OrthographicCamera camera;
    private final GlyphLayout creditsLayout;
    private Stage stage;
    private final String musicCredits1 = """
                When I Was A Boy by Tokyo Music Walker | https://soundcloud.com/user-356546060
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY 3.0
                https://creativecommons.org/licenses/by/3.0/""";
    private final String musicCredits2 = """
                Field Of Fireflies by Purrple Cat | https://purrplecat.com/
                Music promoted on https://www.chosic.com/free-music/all/
                Creative Commons Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
                https://creativecommons.org/licenses/by-sa/3.0/""";
    private final String musicCredits3 = """
                Morning Routine by Ghostrifter Official | https://soundcloud.com/ghostrifter-official
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY-SA 3.0
                https://creativecommons.org/licenses/by-sa/3.0/""";
    private final String musicCredits4 = """
                Faithful Mission by Artificial.Music | https://soundcloud.com/artificial-music/
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY 3.0
                https://creativecommons.org/licenses/by/3.0/""";
    private final String musicCredits5 = """
                Where The Waves Take Us by Purrple Cat | https://purrplecat.com/
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY-SA 3.0
                https://creativecommons.org/licenses/by-sa/3.0/""";
    private final String musicCredits6 = """
                City Lights by Ghostrifter bit.ly/ghostrifter-yt
                Creative Commons - Attribution-NoDerivs 3.0 Unported - CC BY-ND 3.0
                Music promoted by https://www.chosic.com/free-music/all/""";
    private final String musicCredits7 = """
            Happy by MusicbyAden | https://soundcloud.com/musicbyaden
            Music promoted by https://www.chosic.com/free-music/all/
            Creative Commons CC BY-SA 3.0
            https://creativecommons.org/licenses/by-sa/3.0/
            """;
    private final String musicCreditsPurr = """
                Am A Mouse by SirPurrr | https://soundcloud.com/sirpurrr
                Who was kind enough to allow me to use his music in the game.""";
    private final String credits = "This is a non-commercial game for Mousie and\n" +
            "her beautiful community at twitch.tv/mousiefuzz.\n" +
            "It's always a pleasure to hang out there and I am glad\n" +
            "that I am a part of this community.\n\n" +
            "The game is inspired by Cookie Clicker\n" +
            "Check it out at https://orteil.dashnet.org/cookieclicker/ \n" +
            "Or buy the steam version to support the creators:\n" +
            "https://store.steampowered.com/app/1454400/Cookie_Clicker/ \n\n" +
            "The project started as a fork of https://github.com/cerus/cookie-clicker-game \n" +
            "Even if the game does not have a lot in common with it at this point,\n" +
            "I am thankful for the project providing a good starting point.\n\n" +
            "Music used: \n\n" + musicCreditsPurr+ "\n\n" + musicCredits1 + "\n\n" + musicCredits2 + "\n\n"
            + musicCredits3 + "\n\n" + musicCredits4 + "\n\n"
            + musicCredits5 + "\n\n" + musicCredits6 + "\n\n" + musicCredits7 +"\n\n\n"
            + "Art: \n\n"
            + "Main Menu Emotes and some Pixel Art is from Mousiefuzz.\n" +
            "Namely the Mountain, Dance Floor and Cyberpunk image\n" +
            "representations were made by her.\n" +
            "Follow her at twitch.tv/mousiefuzz.\n\n"
            + "The backgrounds are AI generated. So no credits here.\n" +
            "Just wanted to make it clear I did not draw them.\n\n" +
            "The rest of the pixel art and animations are made by me.";
//            + "Backgrounds from Screaming Brain Studios\n" +
//            "https://screamingbrainstudios.itch.io/seamless-space-backgrounds";
    private final List<Image> dummyImageList;
    private TextureRegion background; //= new Texture("backgrounds/Starfield_08-1024x1024.png");
    private BitmapFont font = FontUtil.FONT_20;
    private final Texture darkBG = new Texture("dark_background.png");
    private final float SCROLL_AMOUNT = 100f;
    private float scrollOffset;
    AutoFocusScrollPane scrollPane;

    public CreditsScreen(SkittleClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera();
        this.stage = new Stage(new ScreenViewport());
        Table table = new Table();
        table.setFillParent(false);
        table.setDebug(false);
        scrollOffset = 0;

        this.background = game.getBackground(3);//2
        dummyImageList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Image image = new Image(TextureUtil.scaleImage("image_placeholder.png",
                    1000,
                    300));
            dummyImageList.add(image);
            table.add(image).fillX();//scaleImage("valhalla_frame1.png", 500, 100)
            table.row();
        }
        scrollPane = new AutoFocusScrollPane(table);
        scrollPane.setFlickScroll(true); //disables scroll by drag which scrolled horizontally
        scrollPane.setFillParent(true);
        scrollPane.setDebug(false);
        scrollPane.setOverscroll(false, false);
        stage.addActor(scrollPane);
        creditsLayout = new GlyphLayout();
        creditsLayout.setText(FontUtil.FONT_20, credits);
//        table.add(new ImageButton(TextureUtil.getImageButtonStyle("wood", 350, 105)));
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.input.setInputProcessor(stage);
//        font.setColor(Color.BLACK);
    }

    @Override
    public void render(float delta) {
        camera.update();
        game.getBatch().setProjectionMatrix(camera.combined);

        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        game.getBatch().begin();
        game.getBatch().draw(background, 0, 0, camera.viewportWidth, camera.viewportHeight);
        scrollOffset -= SCROLL_AMOUNT;
        Vector2 screenCoords = ScreenUtil.getScreenCoords(dummyImageList.get(0), camera);
//        scrollPane.scrollTo(screenCoords.x, screenCoords.y - scrollOffset, screenCoords.x, screenCoords.y - scrollOffset);
        game.getBatch().draw(darkBG, dummyImageList.get(dummyImageList.size()-1).getX() - 50,
                dummyImageList.get(dummyImageList.size()-1).getY(), 1000, 2000);
        game.getFont().draw(game.getBatch(), "Credits", screenCoords.x + 3, screenCoords.y - 50);
        font.draw(game.getBatch(), credits, screenCoords.x + 3, screenCoords.y - 150);

//        FontUtil.FONT_20.draw(game.getBatch(), credits, 100, Gdx.graphics.getHeight() - 50);

        game.getBatch().end();

        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 60f));
        stage.draw();
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

    }

    @Override
    public void dispose() {

    }
}

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
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.util.FontUtil;

import java.awt.*;
import java.io.IOException;
import java.net.URISyntaxException;

public class CreditsScreen implements Screen {
    private SkittleClickerGame game;
    private OrthographicCamera camera;
    private Label titleLabel;
    private final String musicCredits1 = """
                When I Was A Boy by Tokyo Music Walker | https://soundcloud.com/user-356546060
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY 3.0
                https://creativecommons.org/licenses/by/3.0/
                \s""";
    private final String musicCredits2 = """
                Field Of Fireflies by Purrple Cat | https://purrplecat.com/
                Music promoted on https://www.chosic.com/free-music/all/
                Creative Commons Attribution-ShareAlike 3.0 Unported (CC BY-SA 3.0)
                https://creativecommons.org/licenses/by-sa/3.0/
                \s
                """;
    private final String musicCredits3 = """
                Morning Routine by Ghostrifter Official | https://soundcloud.com/ghostrifter-official
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY-SA 3.0
                https://creativecommons.org/licenses/by-sa/3.0/
                \s""";
    private final String musicCredits4 = """
                Faithful Mission by Artificial.Music | https://soundcloud.com/artificial-music/
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY 3.0
                https://creativecommons.org/licenses/by/3.0/
                \s""";
    private final String musicCredits5 = """
                Where The Waves Take Us by Purrple Cat | https://purrplecat.com/
                Music promoted by https://www.chosic.com/free-music/all/
                Creative Commons CC BY-SA 3.0
                https://creativecommons.org/licenses/by-sa/3.0/
                \s""";
    private final String musicCredits6 = """
                City Lights by Ghostrifter bit.ly/ghostrifter-yt
                Creative Commons — Attribution-NoDerivs 3.0 Unported — CC BY-ND 3.0
                Music promoted by https://www.chosic.com/free-music/all/
                \s""";
    private final String musicCreditsPurr = """
                Am A Mouse by SirPurrr | https://soundcloud.com/sirpurrr
                Who was kind enough to allow me to use his music in the game.""";
    private final String credits = musicCreditsPurr+ "\n" + musicCredits1 + "\n" + musicCredits2 + "\n"
            + musicCredits3 + "\n" + musicCredits4 + "\n"
            + musicCredits5 + "\n" + musicCredits6 + "\n";

    public CreditsScreen(SkittleClickerGame game) {
        this.game = game;
        this.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void show() {
        camera.setToOrtho(false, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
    }

    @Override
    public void render(float delta) {
        camera.update();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new MainMenuScreen(game));
        }

        game.getBatch().begin();

        FontUtil.FONT_20.draw(game.getBatch(), credits, 100, Gdx.graphics.getHeight() - 50);

        game.getBatch().end();
    }

    @Override
    public void resize(int width, int height) {

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

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

package passi.skittleclicker.objects;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.fixes.CustomShapeRenderer;
import passi.skittleclicker.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Disposable {

    public static final int MAX_CLICKER = 54;
    public static final int MAX_GRANDMAS = 20;
    public static final int MAX_BAKERIES = 20;
    public static final int MAX_FACTORIES = 20;

    private CustomShapeRenderer shapeRenderer = new CustomShapeRenderer();
    private boolean visible = false;

    private Texture closeTexture = new Texture(Gdx.files.internal("close.png"));
    private Texture buyTexture = new Texture(Gdx.files.internal("buy.png"));

    public Rectangle closeRepresentation = new Rectangle();
    public Rectangle buyClickerRepresentation = new Rectangle();
    public Rectangle buyGrandmaRepresentation = new Rectangle();
    public Rectangle buyBakeryRepresentation = new Rectangle();
    public Rectangle buyFactoryRepresentation = new Rectangle();

    private float buyClickerButtonWidth = buyTexture.getWidth();
    private float buyClickerButtonHeight = buyTexture.getHeight();
    private float buyGrandmaButtonWidth = buyTexture.getWidth();
    private float buyGrandmaButtonHeight = buyTexture.getHeight();
    private float buyBakeryButtonWidth = buyTexture.getWidth();
    private float buyBakeryButtonHeight = buyTexture.getHeight();
    private float buyFactoryButtonWidth = buyTexture.getWidth();
    private float buyFactoryButtonHeight = buyTexture.getHeight();

    private float animationAlpha = 0.0f;
    private long skittles = 0;
    private long skittlesPerClick = 1;
    ShopGroup playerShopGroup;
    ShopGroup clickerShopGroup;
    ShopGroup grannyShopGroup;
    ShopGroup bakeryShopGroup;
    ShopGroup factoryShopGroup;

    public Shop(){
        List<Upgrade> upgrades = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            upgrades.add(new Upgrade("Upgrade " + i, 100 * i, 1 + i));
        }
        playerShopGroup = new ShopGroup(ShopGroup.Type.PLAYER, 1, upgrades, 1);
        clickerShopGroup = new ShopGroup(ShopGroup.Type.CLICKER, 1, upgrades, 54);
        grannyShopGroup = new ShopGroup(ShopGroup.Type.GRANNY, 3, upgrades, 20);
        bakeryShopGroup = new ShopGroup(ShopGroup.Type.BAKERY, 10, upgrades, 20);
        factoryShopGroup = new ShopGroup(ShopGroup.Type.FACTORY, 50, upgrades, 20);
    }

    public void render(SkittleClickerGame game, OrthographicCamera camera) {
        if (!visible) {
            return;
        }

        if (animationAlpha > 0) {
            animationAlpha -= 0.10;
        }

        Gdx.gl.glEnable(GL20.GL_BLEND);
        Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        shapeRenderer.setProjectionMatrix(camera.combined);
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(140 / 255f, 140 / 255f, 140 / 255f, 1.0f - animationAlpha);
        shapeRenderer.roundedRect(
                camera.position.x - (camera.viewportWidth / 2.2f),
                camera.position.y - (camera.viewportHeight / 2.2f),
                camera.viewportWidth / 2.2f * 2,
                camera.viewportHeight / 2.2f * 2, 10
        );
        shapeRenderer.end();
        Gdx.gl.glDisable(GL20.GL_BLEND);

        closeRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 30,
                camera.position.y + (camera.viewportHeight / 2.2f) - 30,
                25,
                25
        );


        game.getBatch().begin();

        game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -animationAlpha));
        game.getBatch().draw(
                closeTexture,
                closeRepresentation.x,
                closeRepresentation.y,
                closeRepresentation.width,
                closeRepresentation.height
        );
        game.getBatch().setColor(1, 1, 1, 1);

        if (clickerShopGroup.getNumber() >= MAX_CLICKER || skittles < 5) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Clicker: " + clickerShopGroup.getNumber() + " / " + MAX_CLICKER + " [Cost: 5]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 30
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyClickerRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyClickerButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 40 - buyClickerButtonHeight / 2f,
                buyClickerButtonWidth,
                buyClickerButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyClickerRepresentation.x,
                buyClickerRepresentation.y,
                buyClickerRepresentation.width,
                buyClickerRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        if (grannyShopGroup.getNumber() >= MAX_GRANDMAS || skittles < 100) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Grandma: " + grannyShopGroup.getNumber() + " / " + MAX_GRANDMAS + " [Cost: 100]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 60
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyGrandmaRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyGrandmaButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 70 - buyGrandmaButtonHeight / 2f,
                buyGrandmaButtonWidth,
                buyGrandmaButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyGrandmaRepresentation.x,
                buyGrandmaRepresentation.y,
                buyGrandmaRepresentation.width,
                buyGrandmaRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        if (bakeryShopGroup.getNumber() >= MAX_BAKERIES || skittles < 250) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Bakery: " + bakeryShopGroup.getNumber() + " / " + MAX_BAKERIES + " [Cost: 250]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 90
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyBakeryRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyBakeryButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 100 - buyBakeryButtonHeight / 2f,
                buyBakeryButtonWidth,
                buyBakeryButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyBakeryRepresentation.x,
                buyBakeryRepresentation.y,
                buyBakeryRepresentation.width,
                buyBakeryRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        if (factoryShopGroup.getNumber() >= MAX_FACTORIES || skittles < 1000) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Factory: " + factoryShopGroup.getNumber() + " / " + MAX_FACTORIES + " [Cost: 1000]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - 120
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        buyFactoryRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyFactoryButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - 130 - buyFactoryButtonHeight / 2f,
                buyFactoryButtonWidth,
                buyFactoryButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyFactoryRepresentation.x,
                buyFactoryRepresentation.y,
                buyFactoryRepresentation.width,
                buyFactoryRepresentation.height
        );
        game.getBatch().setColor(Color.WHITE);

        game.getBatch().end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && closeRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setVisible(false);
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyClickerRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && clickerShopGroup.getNumber() < MAX_CLICKER
                && skittles >= 5) {
            skittles -= 5;
            clickerShopGroup.incrementNumber();

            buyClickerButtonHeight -= 5;
            buyClickerButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyClickerButtonWidth < buyTexture.getWidth()
                && buyClickerButtonHeight < buyTexture.getHeight()) {
            buyClickerButtonWidth = buyTexture.getWidth();
            buyClickerButtonHeight = buyTexture.getHeight();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyGrandmaRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && grannyShopGroup.getNumber() < MAX_GRANDMAS
                && skittles >= 100) {
            skittles -= 100;
            grannyShopGroup.incrementNumber();

            buyGrandmaButtonHeight -= 5;
            buyGrandmaButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyGrandmaButtonWidth < buyTexture.getWidth()
                && buyGrandmaButtonHeight < buyTexture.getHeight()) {
            buyGrandmaButtonWidth = buyTexture.getWidth();
            buyGrandmaButtonHeight = buyTexture.getHeight();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyBakeryRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && bakeryShopGroup.getNumber() < MAX_BAKERIES
                && skittles >= 250) {
            skittles -= 250;
            bakeryShopGroup.incrementNumber();

            buyBakeryButtonHeight -= 5;
            buyBakeryButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyBakeryButtonWidth < buyTexture.getWidth()
                && buyBakeryButtonHeight < buyTexture.getHeight()) {
            buyBakeryButtonWidth = buyTexture.getWidth();
            buyBakeryButtonHeight = buyTexture.getHeight();
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyFactoryRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && factoryShopGroup.getNumber() < MAX_FACTORIES
                && skittles >= 1000) {
            skittles -= 1000;
            factoryShopGroup.incrementNumber();

            buyFactoryButtonHeight -= 5;
            buyFactoryButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyFactoryButtonWidth < buyTexture.getWidth()
                && buyFactoryButtonHeight < buyTexture.getHeight()) {
            buyFactoryButtonWidth = buyTexture.getWidth();
            buyFactoryButtonHeight = buyTexture.getHeight();
        }
    }

    private Vector2 getUnprojectedScreenCoords(Camera camera, float minus) {
        Vector3 screenCoords = new Vector3();
        screenCoords.set(Gdx.input.getX(), Gdx.input.getY(), 0);
        camera.unproject(screenCoords);

        return new Vector2(screenCoords.x - minus, screenCoords.y - minus);
    }

    public boolean isNotVisible() {
        return !visible;
    }

    public void setupShop(Object[] objects){
        setSkittles(objects[0] instanceof Long ? (Long) objects[0] : (Integer) objects[0]);
        clickerShopGroup.setNumber(objects[1] instanceof Long ? (Long) objects[1] : (Integer) objects[1]);
        grannyShopGroup.setNumber(objects[2] instanceof Long ? (Long) objects[2] : (Integer) objects[2]);
        bakeryShopGroup.setNumber(objects[3] instanceof Long ? (Long) objects[3] : (Integer) objects[3]);
        factoryShopGroup.setNumber(objects[4] instanceof Long ? (Long) objects[4] : (Integer) objects[4]);
        //TODO add loading upgrades
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
        if (visible) {
            animationAlpha = 1.0f;
        }
    }

    public long getClickerNumber() {
        return clickerShopGroup.getNumber();
    }


    public long getSkittles() {
        return skittles;
    }

    public long getSkittlesPerSecond(){
        return (clickerShopGroup.getSkittlesPerSecond())+ (grannyShopGroup.getSkittlesPerSecond())
                + (bakeryShopGroup.getSkittlesPerSecond()) + (factoryShopGroup.getSkittlesPerSecond());
    }

    public void setSkittles(long skittles) {
        this.skittles = skittles;
    }

    public long getGrannyNumber() {
        return grannyShopGroup.getNumber();
    }
    public long getBakeryNumber() {
        return bakeryShopGroup.getNumber();
    }

    public long getFactoryNumber() {
        return factoryShopGroup.getNumber();
    }
    @Override
    public void dispose() {
        shapeRenderer.dispose();
        closeTexture.dispose();
        buyTexture.dispose();
    }

    public void updateSkittles() {
        skittles += getSkittlesPerSecond();
    }

    public void click() {
        skittles += skittlesPerClick;
    }

    public void deleteSaveData() {
        setSkittles(0);
        clickerShopGroup.setNumber(0);
        grannyShopGroup.setNumber(0);
        bakeryShopGroup.setNumber(0);
        factoryShopGroup.setNumber(0);
        //TODO set upgrade progress to 0
    }
}
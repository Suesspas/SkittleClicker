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
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Disposable;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.fixes.CustomShapeRenderer;
import passi.skittleclicker.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

public class Shop implements Disposable {
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
    ShopGroup playerShopGroup;
    ShopGroup clickerShopGroup;
    ShopGroup grannyShopGroup;
    ShopGroup bakeryShopGroup;
    ShopGroup factoryShopGroup;
    List<ShopGroup> shopGroups;

    public Shop(){
        List<Upgrade> upgrades = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            upgrades.add(new Upgrade("Upgrade " + i, 100 * i, 1 + i));
        }
        playerShopGroup = new ShopGroup(ShopGroup.Type.PLAYER, 1, upgrades, 1, 0);
        clickerShopGroup = new ShopGroup(ShopGroup.Type.CLICKER, 1, upgrades, 54, 5);
        grannyShopGroup = new ShopGroup(ShopGroup.Type.GRANNY, 3, upgrades, 20, 100);
        bakeryShopGroup = new ShopGroup(ShopGroup.Type.BAKERY, 10, upgrades, 20, 250);
        factoryShopGroup = new ShopGroup(ShopGroup.Type.FACTORY, 50, upgrades, 20, 1000);
        //Order in which shopGroups are added has to be the same as order of buttons #jank
        shopGroups = new ArrayList<>();
        shopGroups.add(clickerShopGroup);
        shopGroups.add(grannyShopGroup);
        shopGroups.add(bakeryShopGroup);
        shopGroups.add(factoryShopGroup);
        //player always last
        shopGroups.add(playerShopGroup);

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

        renderShopGroup(game,camera, clickerShopGroup,30);
        renderShopGroup(game,camera, grannyShopGroup,60);
        renderShopGroup(game, camera, bakeryShopGroup,90);
        renderShopGroup(game,camera, factoryShopGroup,120);
        for (int i = 0; i < 15; i++) {
            renderShopGroup(game,camera, factoryShopGroup,120+(30*i));
        }


        game.getBatch().end();


        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && closeRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                || Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            setVisible(false);
        }

        addButtonFunctionality(buyClickerRepresentation, camera, clickerShopGroup, buyClickerButtonHeight, buyClickerButtonWidth);

        addButtonFunctionality(buyGrandmaRepresentation, camera, grannyShopGroup,  buyGrandmaButtonHeight, buyGrandmaButtonWidth);

        addButtonFunctionality(buyBakeryRepresentation, camera, bakeryShopGroup,  buyBakeryButtonHeight, buyBakeryButtonWidth);

        addButtonFunctionality(buyFactoryRepresentation, camera, factoryShopGroup,  buyFactoryButtonHeight, buyFactoryButtonWidth);
    }

    private void addButtonFunctionality(Rectangle buyClickerRepresentation, OrthographicCamera camera, ShopGroup shopGroup, float buyButtonHeight, float buyButtonWidth) {
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)
                && buyClickerRepresentation.contains(getUnprojectedScreenCoords(camera, 0))
                && shopGroup.getNumber() < shopGroup.getMAX_NUMBER()
                && skittles >= shopGroup.getCurrentCost()) {
            skittles -= shopGroup.getCurrentCost();
            shopGroup.incrementNumber();

            buyButtonHeight -= 5;
            buyButtonWidth -= 5;
        } else if (!Gdx.input.isButtonPressed(Input.Buttons.LEFT)
                && buyButtonWidth < buyTexture.getWidth()
                && buyButtonHeight < buyTexture.getHeight()) {
            buyButtonWidth = buyTexture.getWidth();
            buyButtonHeight = buyTexture.getHeight();
        }
    }

    private void renderShopGroup(SkittleClickerGame game, OrthographicCamera camera, ShopGroup shopGroup, int yOffset) {

        if (shopGroup.getNumber() >= shopGroup.getMAX_NUMBER() || skittles < shopGroup.getCurrentCost()) {
            FontUtil.KOMIKA.setColor(Color.RED);
            game.getBatch().setColor(game.getBatch().getColor().add(0, 0, 0, -0.5f));
        }
        FontUtil.KOMIKA.draw(
                game.getBatch(),
                "Bakery: " + shopGroup.getNumber() + " / " + shopGroup.getMAX_NUMBER() + " [Cost: "+ shopGroup.getCurrentCost()+"]",
                camera.position.x - (camera.viewportWidth / 2.2f) + 20,
                camera.position.y + (camera.viewportHeight / 2.2f) - yOffset
        );
        FontUtil.KOMIKA.setColor(Color.WHITE);
        switch (shopGroup.getType()){
            case CLICKER:
                renderShopGroupButton(buyClickerRepresentation, camera, buyClickerButtonWidth, 40, buyClickerButtonHeight, game);
                break;
            case GRANNY:
                renderShopGroupButton(buyGrandmaRepresentation, camera, buyGrandmaButtonWidth, 70, buyGrandmaButtonHeight, game);
                break;
            case BAKERY:
                renderShopGroupButton(buyBakeryRepresentation, camera, buyBakeryButtonWidth, 100, buyBakeryButtonHeight, game);
                break;
            case FACTORY:
                renderShopGroupButton(buyFactoryRepresentation, camera, buyFactoryButtonWidth, 130, buyFactoryButtonHeight, game);
                break;

        }
        game.getBatch().setColor(Color.WHITE);
    }

    private void renderShopGroupButton(Rectangle buyBakeryRepresentation, OrthographicCamera camera, float buyShopGroupButtonWidth, int x, float buyShopGroupButtonHeight, SkittleClickerGame game) {
        buyBakeryRepresentation.set(
                camera.position.x - (camera.viewportWidth / 2.2f) + camera.viewportWidth / 2.2f * 2 - 110 - buyShopGroupButtonWidth / 2f,
                camera.position.y + (camera.viewportHeight / 2.2f) - x - buyShopGroupButtonHeight / 2f,
                buyShopGroupButtonWidth,
                buyShopGroupButtonHeight
        );
        game.getBatch().draw(
                buyTexture,
                buyBakeryRepresentation.x,
                buyBakeryRepresentation.y,
                buyBakeryRepresentation.width,
                buyBakeryRepresentation.height
        );
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

    public int numberOfShopGroups (){
        return shopGroups.size();
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

    public List<ShopGroup> getShopGroups() {
        return shopGroups;
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
        skittles += playerShopGroup.getBaseSkittles();
    }

    public void deleteSaveData() {
        setSkittles(0);
        clickerShopGroup.setNumber(0);
        grannyShopGroup.setNumber(0);
        bakeryShopGroup.setNumber(0);
        factoryShopGroup.setNumber(0);
        //TODO set upgrade progress to 0
    }

    public void pay(long cost) {
        skittles -= cost;
    }
}
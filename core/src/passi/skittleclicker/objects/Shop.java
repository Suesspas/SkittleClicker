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
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Disposable;
import passi.skittleclicker.SkittleClickerGame;
import passi.skittleclicker.fixes.CustomShapeRenderer;
import passi.skittleclicker.util.FontUtil;

import java.util.ArrayList;
import java.util.List;

public class Shop{
    private long skittles = 0;
    private static final long baseSkittlesPerClick = 1;
    private double clickModifier = 1;
    private double goldenModifier = 10;
    private boolean goldenActive;
    List<ShopGroup> shopGroups;
    List<Upgrade> upgrades;

    List<Integer> alreadyDisplayedUpgrades = new ArrayList<>();

    public Shop(){
        upgrades = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Upgrade " + i, 100 * i+1, 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Upgrade " + i, 100 * i+1, 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.BAKERY,"Upgrade " + i, 100 * i+1, 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.FACTORY,"Upgrade " + i, 100 * i+1, 2 + i));
        }
        ShopGroup clickerShopGroup = new ShopGroup(ShopGroup.Type.CLICKER, 1, 54, 5);
        ShopGroup grannyShopGroup = new ShopGroup(ShopGroup.Type.GRANNY, 3, 20, 100);
        ShopGroup bakeryShopGroup = new ShopGroup(ShopGroup.Type.BAKERY, 10, 20, 250);
        ShopGroup factoryShopGroup = new ShopGroup(ShopGroup.Type.FACTORY, 50, 20, 1000);

        //Order in which shopGroups are added has to be the same as order of buttons #jank
        shopGroups = new ArrayList<>();
        shopGroups.add(clickerShopGroup);
        shopGroups.add(grannyShopGroup);
        shopGroups.add(bakeryShopGroup);
        shopGroups.add(factoryShopGroup);
        for (int i = 4; i < ShopGroup.Type.values().length; i++) {
            shopGroups.add(new ShopGroup(ShopGroup.Type.values()[i], 100L *i, 42, 100L *i));
        }
        this.goldenActive = false;
    }

    public void setupShop(List<Object> objects){
        setSkittles(objects.get(0) instanceof Long ? (Long) objects.get(0) : (Integer) objects.get(0));
        shopGroups.get(0).setNumber(objects.get(1) instanceof Long ? (Long) objects.get(1) : (Integer) objects.get(1));
        shopGroups.get(1).setNumber(objects.get(2) instanceof Long ? (Long) objects.get(2) : (Integer) objects.get(2));
        shopGroups.get(2).setNumber(objects.get(3) instanceof Long ? (Long) objects.get(3) : (Integer) objects.get(3));
        shopGroups.get(3).setNumber(objects.get(4) instanceof Long ? (Long) objects.get(4) : (Integer) objects.get(4));
        for (int i = 4; i < numberOfShopGroups(); i++) {
            shopGroups.get(i).setNumber(objects.get(i+1) instanceof Long ? (Long) objects.get(i+1) : (Integer) objects.get(i+1));
        }

        for (int i = 0; i < upgrades.size(); i++) {
            if ((boolean)objects.get(shopGroups.size()+1+i)){
                unlockUpgrade(i);
            }
        }
    }

    public int numberOfShopGroups (){
        return shopGroups.size();
    }

    public int numberOfUpgrades(){
        return upgrades.size();
    }

    public long getClickerNumber() {
        return shopGroups.get(0).getNumber();
    }

    public List<ShopGroup> getShopGroups() {
        return shopGroups;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    public Upgrade getUpgrade(int index) {
        return upgrades.get(index);
    }

    public long getSkittles() { //MAX signed long value about 5x10^15
        return skittles;
    }

    public long getSkittlesPerSecond(){
        long result = 0;
        for (ShopGroup s:
             shopGroups) {
            result += s.getSkittlesPerSecond();
        }
        return Math.round(result * (goldenActive ? goldenModifier : 1));
    }

    public void setSkittles(long skittles) {
        this.skittles = skittles;
    }

    public long getGrannyNumber() {
        return shopGroups.get(1).getNumber();
    }
    public long getBakeryNumber() {
        return shopGroups.get(2).getNumber();
    }

    public long getFactoryNumber() {
        return shopGroups.get(3).getNumber();
    }

    public void updateSkittles() {
        skittles += getSkittlesPerSecond();
    }
    public void updateClickModifier(double modifier){
        clickModifier *= modifier;
    }
    public void click() {
        skittles += baseSkittlesPerClick * clickModifier * (goldenActive ? goldenModifier : 1);
    }

    public void deleteSaveData() {
        setSkittles(0);
        for (ShopGroup s:
             shopGroups) {
            s.setNumber(0);
        }
        for (Upgrade u:
             upgrades) {
            u.lock();
        }
    }

    public void pay(long cost) {
        skittles -= cost;
    }

    public void unlockUpgrade(int index) {
        Upgrade upgrade = upgrades.get(index);
        upgrade.unlock();
        for (ShopGroup s:
             shopGroups) {
            if (s.getType() == upgrade.getType()){
                s.updateModifier(upgrade.getModifier());
                return;
            }
        }
    }

    public void displayedUpgrade(int index){
        alreadyDisplayedUpgrades.add(index);
    }

    public int getNewUpgradeIndex() {
        for (int i = 0; i < upgrades.size(); i++) {
            if (!alreadyDisplayedUpgrades.contains(i)) return i;
        }
        return -1;
    }

    public List<Integer> getAlreadyDisplayedUpgrades() {
        return alreadyDisplayedUpgrades;
    }

    public void goldenActive(boolean b) {
        goldenActive = b;
    }
}
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class Shop{
    private long skittles = 0;
    private static final long baseSkittlesPerClick = 1;
    private double clickModifier = 1;
    private double goldenModifier = 10;
    private double generalModifier = 1;
    private boolean goldenActive;
    List<ShopGroup> shopGroups;
    List<Upgrade> upgrades;

    List<Integer> alreadyDisplayedUpgrades = new ArrayList<>();

    public Shop(){
        upgrades = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Upgrade " + i, 100 * (i+1), 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.GOLDEN,"Upgrade " + i, 100 * (i+1), 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.ALL,"Upgrade " + i, 100 * (i+1), 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Upgrade " + i, 100 * (i+1), 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Upgrade " + i, 100 * (i+1), 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Upgrade " + i, 100 * (i+1), 2 + i));
            upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Upgrade " + i, 100 * (i+1), 2 + i));
        }
        ShopGroup clickerShopGroup = new ShopGroup(ShopGroup.Type.CLICKER, 1, 54, 5, "Clicker Text");
        ShopGroup grannyShopGroup = new ShopGroup(ShopGroup.Type.GRANNY, 3, 20, 100, """
                A certain Cat with a Knight Title forces
                I mean politely asks the local neighbourhood grandmas
                to help with the skittles production""");
        ShopGroup bakeryShopGroup = new ShopGroup(ShopGroup.Type.DUCK, 10, 20, 250, """
                In a remote skittles swamp there are living many ducks 
                with normal amounts of legs. 
                Granting you an increase in skittles production 
                by 1 Skittle per Second for every leg they possess. 
                +4 to SpS\s""");
        ShopGroup factoryShopGroup = new ShopGroup(ShopGroup.Type.SKITTLES_BAR, 50, 20, 1000, "Factory Text");

        //Order in which shopGroups are added has to be the same as order of buttons #jank
        shopGroups = new ArrayList<>();
        shopGroups.add(clickerShopGroup);
        shopGroups.add(grannyShopGroup);
        shopGroups.add(bakeryShopGroup);
        shopGroups.add(factoryShopGroup);
        //placeholders
        for (int i = 7; i < ShopGroup.Type.values().length; i++) {
            shopGroups.add(new ShopGroup(ShopGroup.Type.values()[i], 100L *i, 42, 100L *i, "Clicker Text"));
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
        return Math.round(result * generalModifier * (goldenActive ? goldenModifier : 1));
    }

    public void setSkittles(long skittles) {
        this.skittles = skittles;
    }
    public void updateSkittles() {
        skittles += getSkittlesPerSecond();
    }
    public void updateClickModifier(double modifier){
        clickModifier *= modifier;
    }
    public void updateGoldenModifier(double modifier){
        goldenModifier *= modifier;
    }
    public void updateGeneralModifier(double modifier){
        generalModifier *= modifier;
    }
    public void click() {
        skittles += getSkittlesPerClick();
    }

    public double getClickModifier(){
        return clickModifier;
    }

    public long getSkittlesPerClick(){
        return Math.round(baseSkittlesPerClick * clickModifier * generalModifier *(goldenActive ? goldenModifier : 1));
    }

    public void deleteSaveData() { //TODO probably just call Constructor, see if it works
        setSkittles(0);
        for (ShopGroup s:
             shopGroups) {
            s.setNumber(0);
        }
        for (Upgrade u:
             upgrades) {
            u.lock();
        }
        alreadyDisplayedUpgrades.clear();
    }

    public void pay(long cost) {
        skittles -= cost;
    }

    public void unlockUpgrade(int index) {
        Upgrade upgrade = upgrades.get(index);
        upgrade.unlock();
        ShopGroup.Type type = upgrade.getType();
        switch (type) {
            case PLAYER -> updateClickModifier(upgrade.getModifier());
            case GOLDEN -> updateGoldenModifier(upgrade.getModifier());
            case ALL -> updateGeneralModifier(upgrade.getModifier());
            default -> {
                for (ShopGroup s : shopGroups) {
                    if (s.getType() == upgrade.getType()) {
                        s.updateModifier(upgrade.getModifier());
                        return;
                    }
                }
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
    public String shopgroupTypeToString(ShopGroup.Type type){
        String s = type.name().charAt(0) + type.name().toLowerCase(Locale.ROOT).substring(1);
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '_'){
                s = s.substring(0, i) + " " + s.substring(i+1, i+2).toUpperCase(Locale.ROOT) + s.substring(i+2);
            }
        }
        return s;
    }
}
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
        ShopGroup clickerShopGroup = new ShopGroup(ShopGroup.Type.CLICKER, 1, 54, 5, """
                Why click yourself when you can just let others do it?
                People from chat come together and help you make more
                and more skittles! 
                Spam those left mouse buttons for maximum skittles output.""");
        ShopGroup grannyShopGroup = new ShopGroup(ShopGroup.Type.GRANNY, 3, 24, 100, """
                A certain Cat with a Knight Title forces
                I mean politely asks the local neighbourhood grandmas
                to help with the skittles production""");
        ShopGroup duckShopGroup = new ShopGroup(ShopGroup.Type.DUCK, 10, 18, 250, """
                In a remote skittles swamp many ducks
                with normal amounts of legs can be found.
                Granting you an increase in skittles production
                by 1 Skittle per Second for every leg they possess.
                +4 to SpS\s""");
        ShopGroup skittlesBarShopGroup = new ShopGroup(ShopGroup.Type.SKITTLES_BAR, 50, 24, 1000, """
                You start your own bar to share the sweet flavor of
                skittles drinks with many people!
                Somehow you still end up making more skittles with every
                bar you open despite them being your main ingredient.""");
        ShopGroup cyberpunkBarShopGroup = new ShopGroup(ShopGroup.Type.CYBERPUNK_BAR, 100, 15, 300, """
                You already own skittles bars, but do you own
                cyberpunk skittles bars? 
                Hire knowledgeable people to work for you 
                and contract them to create highly profitable drinks.
                Just tell them to not overdo it with the Karmotrine.""");
        ShopGroup mountainShopGroup = new ShopGroup(ShopGroup.Type.MOUNTAIN, 100, 24, 300, """
                It is said that Skittle Mountain holds many secrets and challenges.
                But once you reach the summit you will have uncovered many things,
                maybe even about yourself. 
                This gives you the energy to make more and more skittles.
                And the best part? You can climb the mountain multiple times
                and keep improving your skittles production.""");
        ShopGroup collabShopGroup = new ShopGroup(ShopGroup.Type.COLLAB, 100, 24, 300, """
                Live streaming skittles production seems to yield even more
                delicious skittles!
                Get together with your streaming friends for a big collab
                and turn those virtual skittles into real ones.""");
        ShopGroup isekaiShopGroup = new ShopGroup(ShopGroup.Type.ISEKAI, 100, 24, 300, """
                The skittle reserves of this world are no longer enough for you.
                Since you heard that in other worlds there might be magic,
                you need to explore this option. 
                If you're lucky you might even find someone 
                that wields the highest form of attack magic:
                Skittle Explosion Magic. EKSU PURO SION""");
        ShopGroup tailorGroup = new ShopGroup(ShopGroup.Type.TAILOR, 100, 24, 300, """
                Skilled Tailors, Making skittles clothes out of 
                the Fabric of reality. Some can even harness the powers 
                of those skittle dresses and wear them, 
                granting them increased strength in combat.""");
        ShopGroup dragonShopGroup = new ShopGroup(ShopGroup.Type.DRAGON, 100, 24, 300, """
                If you were thinking this game starts to drag on... I got you!
                Dragon is the next way you can increase your skittles production.
                More specifically skittles synthesized from the rare ingredient
                dragon tail. Most dragons would never allow you touch to their tail,
                but some are almost as enthusiastic about you eating their tail as
                they are about wearing a maid outfit.""");
        ShopGroup danceFloorShopGroup = new ShopGroup(ShopGroup.Type.DANCE_FLOOR, 100, 24, 300, """
                HASHIRE SORI YO
                KAZE NO YOU NI
                TSUKIMIHARA WO
                PADORU PADORU""");

        //Order in which shopGroups are added has to be the same as order of buttons #jank
        shopGroups = new ArrayList<>();
        shopGroups.add(clickerShopGroup);
        shopGroups.add(grannyShopGroup);
        shopGroups.add(duckShopGroup);
        shopGroups.add(skittlesBarShopGroup);
        shopGroups.add(cyberpunkBarShopGroup);
        shopGroups.add(mountainShopGroup);
        shopGroups.add(collabShopGroup);
        shopGroups.add(isekaiShopGroup);
        shopGroups.add(tailorGroup);
        shopGroups.add(dragonShopGroup);
        shopGroups.add(danceFloorShopGroup);

        //Upgrades
        upgrades = new ArrayList<>();

        upgrades.add(new Upgrade(ShopGroup.Type.PLAYER,"Upgrade " + 1, 100, 2,
                shopgroupTypeToString(ShopGroup.Type.PLAYER) + "Upgrade #" + 1, "shopgroups/Granny.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GOLDEN,"Upgrade " + 2, 100, 2,
                shopgroupTypeToString(ShopGroup.Type.GOLDEN) + "Upgrade #" + 1, "shopgroups/Granny.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ALL,"Upgrade " + 3, 100, 2,
                shopgroupTypeToString(ShopGroup.Type.ALL) + "Upgrade #" + 1, "shopgroups/Granny.png"));
        //bronze upgrades
        upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Bronze Clicker", 100, 2,
                    shopgroupTypeToString(ShopGroup.Type.CLICKER) + "Bronze Upgrade, doubles production of Clickers",
                "upgrades/clicker_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Bronze Granny", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.GRANNY) + "Bronze Upgrade, doubles production of Grannies",
                "upgrades/granny_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Bronze Ducks", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DUCK) + "Bronze Upgrade, doubles production of Ducks",
                "upgrades/duck_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Bronze Skittles Bar", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.SKITTLES_BAR) + "Bronze Upgrade, doubles production of Skittles Bars",
                "upgrades/skittles bar_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.CYBERPUNK_BAR,"Bronze Cyberpunk Bar", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.CYBERPUNK_BAR) + "Bronze Upgrade, doubles production of Cyberpunk Bars",
                "upgrades/cyberpunk bar_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.MOUNTAIN,"Bronze Mountain", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.MOUNTAIN) + "Bronze Upgrade, doubles production of Mountains",
                "upgrades/mountain_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.COLLAB,"Bronze Collab", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.COLLAB) + "Bronze Upgrade, doubles production of Collabs",
                "upgrades/collab_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ISEKAI,"Bronze Isekai", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.ISEKAI) + "Bronze Upgrade, doubles production of Isekais",
                "upgrades/isekai_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.TAILOR,"Bronze Tailor", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.TAILOR) + "Bronze Upgrade, doubles production of Tailors",
                "upgrades/tailor_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DRAGON,"Bronze Dragon", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DRAGON) + "Bronze Upgrade, doubles production of Dragons",
                "upgrades/dragon_bronze.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DANCE_FLOOR,"Bronze Dance Floor", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DANCE_FLOOR) + "Bronze Upgrade, doubles production of Dance Floors",
                "upgrades/dance floor_bronze.png"));

        //silver upgrades
        upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Silver Clicker", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.CLICKER) + "Silver Upgrade, doubles production of Clickers",
                "upgrades/clicker_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Silver Granny", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.GRANNY) + "Silver Upgrade, doubles production of Grannies",
                "upgrades/granny_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Silver Ducks", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DUCK) + "Silver Upgrade, doubles production of Ducks",
                "upgrades/duck_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Silver Skittles Bar", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.SKITTLES_BAR) + "Silver Upgrade, doubles production of Skittles Bars",
                "upgrades/skittles bar_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.CYBERPUNK_BAR,"Silver Cyberpunk Bar", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.CYBERPUNK_BAR) + "Silver Upgrade, doubles production of Cyberpunk Bars",
                "upgrades/cyberpunk bar_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.MOUNTAIN,"Silver Mountain", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.MOUNTAIN) + "Silver Upgrade, doubles production of Mountains",
                "upgrades/mountain_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.COLLAB,"Silver Collab", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.COLLAB) + "Silver Upgrade, doubles production of Collabs",
                "upgrades/collab_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ISEKAI,"Silver Isekai", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.ISEKAI) + "Silver Upgrade, doubles production of Isekais",
                "upgrades/isekai_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.TAILOR,"Silver Tailor", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.TAILOR) + "Silver Upgrade, doubles production of Tailors",
                "upgrades/tailor_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DRAGON,"Silver Dragon", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DRAGON) + "Silver Upgrade, doubles production of Dragons",
                "upgrades/dragon_silver.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DANCE_FLOOR,"Silver Dance Floor", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DANCE_FLOOR) + "Silver Upgrade, doubles production of Dance Floors",
                "upgrades/dance floor_silver.png"));

        //gold upgrades
        upgrades.add(new Upgrade(ShopGroup.Type.CLICKER,"Gold Clicker", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.CLICKER) + "Gold Upgrade, doubles production of Clickers",
                "upgrades/clicker_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.GRANNY,"Gold Granny", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.GRANNY) + "Gold Upgrade, doubles production of Grannies",
                "upgrades/granny_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DUCK,"Gold Ducks", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DUCK) + "Gold Upgrade, doubles production of Ducks",
                "upgrades/duck_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.SKITTLES_BAR,"Gold Skittles Bar", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.SKITTLES_BAR) + "Gold Upgrade, doubles production of Skittles Bars",
                "upgrades/skittles bar_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.CYBERPUNK_BAR,"Gold Cyberpunk Bar", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.CYBERPUNK_BAR) + "Gold Upgrade, doubles production of Cyberpunk Bars",
                "upgrades/cyberpunk bar_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.MOUNTAIN,"Gold Mountain", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.MOUNTAIN) + "Gold Upgrade, doubles production of Mountains",
                "upgrades/mountain_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.COLLAB,"Gold Collab", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.COLLAB) + "Gold Upgrade, doubles production of Collabs",
                "upgrades/collab_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.ISEKAI,"Gold Isekai", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.ISEKAI) + "Gold Upgrade, doubles production of Isekais",
                "upgrades/isekai_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.TAILOR,"Gold Tailor", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.TAILOR) + "Gold Upgrade, doubles production of Tailors",
                "upgrades/tailor_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DRAGON,"Gold Dragon", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DRAGON) + "Gold Upgrade, doubles production of Dragons",
                "upgrades/dragon_gold.png"));
        upgrades.add(new Upgrade(ShopGroup.Type.DANCE_FLOOR,"Gold Dance Floor", 100, 2,
                shopgroupTypeToString(ShopGroup.Type.DANCE_FLOOR) + "Gold Upgrade, doubles production of Dance Floors",
                "upgrades/dance floor_gold.png"));

        this.goldenActive = false;
    }

    public void setupShop(List<Object> objects){
        setSkittles(objects.get(0) instanceof Long ? (Long) objects.get(0) : (Integer) objects.get(0));
//        shopGroups.get(0).setNumber(objects.get(0) instanceof Long ? (Long) objects.get(1) : (Integer) objects.get(1));
//        shopGroups.get(1).setNumber(objects.get(2) instanceof Long ? (Long) objects.get(2) : (Integer) objects.get(2));
//        shopGroups.get(2).setNumber(objects.get(3) instanceof Long ? (Long) objects.get(3) : (Integer) objects.get(3));
//        shopGroups.get(3).setNumber(objects.get(4) instanceof Long ? (Long) objects.get(4) : (Integer) objects.get(4));
        for (int i = 0; i < numberOfShopGroups(); i++) {
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
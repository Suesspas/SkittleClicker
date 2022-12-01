package passi.skittleclicker.objects;

import java.util.List;

public class ShopGroup {
    enum Type {
        PLAYER,
        CLICKER,
        GRANNY,
        BAKERY,
        FACTORY
    };
    private final Type type;
    private long number;
    private long cost;
    private final long MAX_NUMBER;
    private final long baseSkittles;
    private List<Upgrade> upgrades;


    public ShopGroup(Type type, long baseSkittles, List<Upgrade> upgrades, long MAX_NUMBER, long cost) {
        this.type = type;
        this.number = 0;
        this.MAX_NUMBER = MAX_NUMBER;
        this.cost = cost;
        this.baseSkittles = baseSkittles;
        this.upgrades = upgrades;
    }

    public long getSkittlesPerSecond(){
        return Math.round(number * baseSkittles * getModifier());
    }

    private double getModifier(){
        double modifier = 1;
        for (Upgrade u: upgrades) {
            if (u.isUnlocked()){
                modifier *= u.getModifier();
            }
        }
        return modifier;
    }

    public Type getType() {
        return type;
    }

    public long getNumber() {
        return number;
    }

    public long getBaseSkittles() {
        return baseSkittles;
    }

    public long getCurrentCost() {
        return cost * number;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }
    public long getMAX_NUMBER() {
        return MAX_NUMBER;
    }

    public void setNumber(long number) {
        this.number = number;
    }

    public void incrementNumber() {
        this.number++;
    }
    public void upgradeProgress(){

    }
}

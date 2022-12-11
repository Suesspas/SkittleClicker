package passi.skittleclicker.objects;

public class ShopGroup {

    enum Type {
        PLAYER,
        GOLDEN,
        ALL,
        CLICKER,
        GRANNY,
        DUCK,
        SKITTLES_BAR,
        PLACEHOLDER1,
        PLACEHOLDER2,
        PLACEHOLDER3,
        PLACEHOLDER4,
        PLACEHOLDER5,
        PLACEHOLDER6,
        PLACEHOLDER7,
        PLACEHOLDER8,
    };
    private final Type type;
    private long number;
    private final long baseCost;
    private final long MAX_NUMBER;
    private final long baseSkittles;
    private double modifier;
    private final String text;


    public ShopGroup(Type type, long baseSkittles, long MAX_NUMBER, long baseCost, String text) {
        this.type = type;
        this.number = 0;
        this.MAX_NUMBER = MAX_NUMBER;
        this.baseCost = baseCost;
        this.baseSkittles = baseSkittles;
        this.modifier = 1;
        this.text = text;
    }

    public long getSkittlesPerSecond(){
        return Math.round(number * baseSkittles * modifier);
    }

    public void updateModifier(double modifier){
        this.modifier *= modifier;
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
        return baseCost * (number+1);
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
    public String getText() {
        return text;
    }
}

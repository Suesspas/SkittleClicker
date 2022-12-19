package passi.skittleclicker.objects;

public class ShopGroup {

    enum Type {
        PLAYER,
        GOLDEN,
        MILK,
        ALL,
        CLICKER,
        GRANNY,
        DUCK,
        SKITTLES_BAR,
        MOUNTAIN,
        COLLAB,
        CYBERPUNK_BAR,
        ISEKAI,
        TAILOR,
        DRAGON,
        DANCE_FLOOR,
    };
    private final Type type;
    private long number;
    private final long baseCost;
    private final long MAX_NUMBER;
    private final long baseSkittles;
    private double modifier;
    private final String text;
    private final double costIncreaseMult = 0.1;


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
        return Math.round(baseCost * ((number * costIncreaseMult) + 1));
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

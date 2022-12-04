package passi.skittleclicker.objects;

public class Upgrade {
    private final String Name;
    private final long cost;
    private final double modifier; //Modifier for Skittles gain, usually > 1
    private boolean unlocked;
    ShopGroup.Type type;

    public Upgrade(ShopGroup.Type type, String name, long cost, double modifier) {
        this.type = type;
        Name = name;
        this.cost = cost;
        this.modifier = modifier;
        this.unlocked = false;
    }

    public String getName() {
        return Name;
    }

    public double getModifier() {
        return modifier;
    }

    public long getCost() {
        return cost;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        this.unlocked = true;
    }

    public void lock() {
        this.unlocked = false;
    }
}

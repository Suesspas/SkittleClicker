package passi.skittleclicker.objects;

public class Upgrade {
    private final String Name;
    private final long cost;
    private final double modifier; //Modifier for Skittles gain, usually > 1
    private boolean unlocked;
    private final ShopGroup.Type type;
    private final String text;
    private final String imagePath;
    private boolean visible;

    public Upgrade(ShopGroup.Type type, String name, long cost, double modifier, String text, String imagePath) {
        this.type = type;
        Name = name;
        this.cost = cost;
        this.modifier = modifier;
        this.unlocked = false;
        this.text = text;
        this.imagePath = imagePath;
        this.visible = false;
    }

    public boolean isUnlocked() {
        return unlocked;
    }

    public void unlock() {
        this.unlocked = true;
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
    public ShopGroup.Type getType() {
        return type;
    }

    public String getText() {
        return text;
    }

    public String getImagePath() {
        return imagePath;
    }
    public void makeVisible(){
        visible = true;
    }
    public boolean isVisible(){
        return visible;
    }
}

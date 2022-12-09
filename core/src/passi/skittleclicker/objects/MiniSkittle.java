package passi.skittleclicker.objects;

public class MiniSkittle {

    public static final int WIDTH = 25;
    public static final int HEIGHT = 25;
    public static final float SPEED = 0.4f;//0.8f;
    public static final float ROTATION_SPEED = 0.25f;
    protected float x;
    protected float y;

    private float rotation;

    public enum Color{
        RED,
        GREEN,
        PURPLE
    }

    protected Color color;

    public Color getColor() {
        return color;
    }

    public MiniSkittle(float x, float y, float rotation, int color) {
        this.x = x;
        this.y = y;
        this.rotation = rotation;
        this.color = Color.values()[color];
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getRotation() {
        return rotation;
    }

    public void setRotation(float rotation) {
        this.rotation = rotation;
    }
}
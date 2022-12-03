package passi.skittleclicker.objects;

public class MiniSkittle {

    private float x_relativePos;

    private float y;

    private float rotation;

    public enum Color{
        RED,
        GREEN,
        PURPLE
    }

    Color color;

    public Color getColor() {
        return color;
    }

    public MiniSkittle(float x_relativePos, float y, float rotation, int color) {
        this.x_relativePos = x_relativePos;
        this.y = y;
        this.rotation = rotation;
        this.color = Color.values()[color];
    }

    public float getX_relativePos() {
        return x_relativePos;
    }

    public void setX_relativePos(float x_relativePos) {
        this.x_relativePos = x_relativePos;
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
package de.cerus.cookieclicker.objects;

public class MiniCookie {

    private float x;

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

    public MiniCookie(float x, float y, float rotation, int color) {
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
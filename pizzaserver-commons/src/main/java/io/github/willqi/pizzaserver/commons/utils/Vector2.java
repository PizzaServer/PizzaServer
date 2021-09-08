package io.github.willqi.pizzaserver.commons.utils;

public class Vector2 {

    private final float x;
    private final float y;


    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public Vector2 add(Vector2 vector) {
        return this.add(vector.getX(), vector.getY());
    }

    public Vector2 add(float x, float y) {
        return new Vector2(this.getX() + x, this.getY() + y);
    }

    public Vector2 subtract(Vector2 vector) {
        return this.subtract(vector.getX(), vector.getY());
    }

    public Vector2 subtract(float x, float y) {
        return new Vector2(this.getX() - x, this.getY() - y);
    }

    public Vector2 multiply(float multiplier) {
        return this.multiply(multiplier, multiplier);
    }

    public Vector2 multiply(Vector2 vector) {
        return this.multiply(vector.getX(), vector.getY());
    }

    public Vector2 multiply(float x, float y) {
        return new Vector2(this.getX() * x, this.getY() * y);
    }

    public Vector2 divide(float divider) {
        return this.divide(divider, divider);
    }

    public Vector2 divide(Vector2 vector) {
        return this.divide(vector.getX(), vector.getY());
    }

    public Vector2 divide(float x, float y) {
        return new Vector2(this.getX() / x, this.getY() / y);
    }

    public double distanceTo(Vector2i vector2i) {
        return Math.sqrt(Math.pow(this.getX() - vector2i.getX(), 2) + Math.pow(this.getY() - vector2i.getY(), 2));
    }

    public double distanceTo(Vector2 vector2) {
        return Math.sqrt(Math.pow(this.getX() - vector2.getX(), 2) + Math.pow(this.getY() - vector2.getY(), 2));
    }

    public Vector2i toVector2i() {
        return new Vector2i((int)this.x, (int)this.y);
    }

}

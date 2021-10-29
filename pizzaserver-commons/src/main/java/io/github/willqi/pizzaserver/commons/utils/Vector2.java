package io.github.willqi.pizzaserver.commons.utils;

public class Vector2 {

    private float x;
    private float y;


    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return this.x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return this.y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public Vector2 add(Vector2 vector) {
        return this.add(vector.getX(), vector.getY());
    }

    public Vector2 add(Vector2i vector2i) {
        return this.add(vector2i.getX(), vector2i.getY());
    }

    public Vector2 add(float x, float y) {
        return new Vector2(this.getX() + x, this.getY() + y);
    }

    public Vector2 subtract(Vector2 vector) {
        return this.subtract(vector.getX(), vector.getY());
    }

    public Vector2 subtract(Vector2i vector2i) {
        return this.subtract(vector2i.getX(), vector2i.getY());
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

    public Vector2 multiply(Vector2i vector2i) {
        return this.multiply(vector2i.getX(), vector2i.getY());
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

    public Vector2 divide(Vector2i vector2i) {
        return this.divide(vector2i.getX(), vector2i.getY());
    }

    public Vector2 divide(float x, float y) {
        return new Vector2(this.getX() / x, this.getY() / y);
    }

    public float distanceBetween(Vector2i vector2i) {
        return (float) Math.sqrt(Math.pow(this.getX() - vector2i.getX(), 2) + Math.pow(this.getY() - vector2i.getY(), 2));
    }

    public float distanceBetween(Vector2 vector2) {
        return (float) Math.sqrt(Math.pow(this.getX() - vector2.getX(), 2) + Math.pow(this.getY() - vector2.getY(), 2));
    }

    public float getLength() {
        return (float) Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
    }

    public float dot(Vector2 vector2) {
        return (this.getX() * vector2.getX()) + (this.getY() * vector2.getY());
    }

    public float dot(Vector2i vector2i) {
        return (this.getX() * vector2i.getX()) + (this.getY() * vector2i.getY());
    }

    public Vector2 normalize() {
        float length = this.getLength();
        return new Vector2(this.getX() / length, this.getY() / length);
    }

    public Vector2i toVector2i() {
        return new Vector2i((int) this.x, (int) this.y);
    }

    @Override
    public String toString() {
        return "Vector2(x=" + this.x + ", y=" + this.y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2) {
            Vector2 otherVector = (Vector2) obj;
            return NumberUtils.isNearlyEqual(otherVector.getX(), this.getX())
                    && NumberUtils.isNearlyEqual(otherVector.getY(), this.getY());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) ((43 * this.getX()) + (43 * this.getY()));
    }

}

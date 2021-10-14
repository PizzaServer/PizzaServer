package io.github.willqi.pizzaserver.commons.utils;

public class Vector2i {

    private final int x;
    private final int y;


    public Vector2i(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public Vector2i add(Vector2i vector) {
        return this.add(vector.getX(), vector.getY());
    }

    public Vector2i add(int x, int y) {
        return new Vector2i(this.getX() + x, this.getY() + y);
    }

    public Vector2i subtract(Vector2i vector) {
        return this.subtract(vector.getX(), vector.getY());
    }

    public Vector2i subtract(int x, int y) {
        return new Vector2i(this.getX() - x, this.getY() - y);
    }

    public Vector2i multiply(int multiplier) {
        return this.multiply(multiplier, multiplier);
    }

    public Vector2i multiply(Vector2i vector) {
        return this.multiply(vector.getX(), vector.getY());
    }

    public Vector2i multiply(int x, int y) {
        return new Vector2i(this.getX() * x, this.getY() * y);
    }

    public Vector2i divide(int divider) {
        return this.divide(divider, divider);
    }

    public Vector2i divide(Vector2i vector) {
        return this.divide(vector.getX(), vector.getY());
    }

    public Vector2i divide(int x, int y) {
        return new Vector2i(this.getX() / x, this.getY() / y);
    }

    public double distanceBetween(Vector2i vector2i) {
        return Math.sqrt(Math.pow(this.getX() - vector2i.getX(), 2) + Math.pow(this.getY() - vector2i.getY(), 2));
    }

    public double distanceBetween(Vector2 vector2) {
        return Math.sqrt(Math.pow(this.getX() - vector2.getX(), 2) + Math.pow(this.getY() - vector2.getY(), 2));
    }

    public double getLength() {
        return Math.sqrt(Math.pow(this.getX(), 2) + Math.pow(this.getY(), 2));
    }

    public Vector2 normalize() {
        double length = this.getLength();
        return new Vector2((float) (this.getX() / length), (float) (this.getY() / length));
    }

    public double dot(Vector2 vector2) {
        return (this.getX() * vector2.getX()) + (this.getY() * vector2.getY());
    }

    public double dot(Vector2i vector2i) {
        return (this.getX() * vector2i.getX()) + (this.getY() * vector2i.getY());
    }

    public Vector2 toVector2() {
        return new Vector2(this.x, this.y);
    }

    @Override
    public String toString() {
        return "Vector2i(x=" + this.x + ", y=" + this.y + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector2i) {
            Vector2i otherVector = (Vector2i) obj;
            return otherVector.getX() == this.getX() && otherVector.getY() == this.getY();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (43 * this.getX()) + (43 * this.getY());
    }

}

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

    public Vector2 toVector2() {
        return new Vector2(this.x, this.y);
    }

}

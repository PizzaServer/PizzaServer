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

    public Vector2i toVector2i() {
        return new Vector2i((int)this.x, (int)this.y);
    }

}

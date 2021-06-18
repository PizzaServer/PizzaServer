package io.github.willqi.pizzaserver.commons.utils;

public class Vector3i {

    private final int x;
    private final int y;
    private final int z;


    public Vector3i(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public int getZ() {
        return this.z;
    }

}

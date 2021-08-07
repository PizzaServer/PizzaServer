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

    public Vector3i add(Vector3i vector3i) {
        return this.add(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3i add(int x, int y, int z) {
        return new Vector3i(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public Vector3i subtract(Vector3i vector3i) {
        return this.subtract(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3i subtract(int x, int y, int z) {
        return new Vector3i(this.getX() - x, this.getY() - y, this.getZ() - z);
    }

    public Vector3i multiply(int multiplier) {
        return this.multiply(multiplier, multiplier, multiplier);
    }

    public Vector3i multiply(Vector3i vector3i) {
        return this.multiply(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3i multiply(int x, int y, int z) {
        return new Vector3i(this.getX() * x, this.getY() * y, this.getZ() * z);
    }

    public Vector3i divide(int divider) {
        return this.divide(divider, divider, divider);
    }

    public Vector3i divide(Vector3i vector3i) {
        return this.divide(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3i divide(int x, int y, int z) {
        return new Vector3i(this.getX() / x, this.getY() / y, this.getZ() / z);
    }

    public Vector3 toVector3() {
        return new Vector3(this.x, this.y, this.z);
    }

}

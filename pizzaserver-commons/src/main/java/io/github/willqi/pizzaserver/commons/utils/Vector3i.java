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

    public double distanceTo(Vector3i vector3i) {
        return Math.sqrt(Math.pow(this.getX() - vector3i.getX(), 2) + Math.pow(this.getY() - vector3i.getY(), 2) + Math.pow(this.getZ() - vector3i.getZ(), 2));
    }

    public double distanceTo(Vector3 vector3) {
        return Math.sqrt(Math.pow(this.getX() - vector3.getX(), 2) + Math.pow(this.getY() - vector3.getY(), 2) + Math.pow(this.getZ() - vector3.getZ(), 2));
    }

    public Vector3 toVector3() {
        return new Vector3(this.x, this.y, this.z);
    }

}

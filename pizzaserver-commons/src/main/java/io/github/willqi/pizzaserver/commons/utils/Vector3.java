package io.github.willqi.pizzaserver.commons.utils;

public class Vector3 {

    private final float x;
    private final float y;
    private final float z;


    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getZ() {
        return this.z;
    }

    public Vector3 add(Vector3 vector3) {
        return this.add(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public Vector3 subtract(Vector3i vector3) {
        return this.subtract(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Vector3 subtract(float x, float y, float z) {
        return new Vector3(this.getX() - x, this.getY() - y, this.getZ() - z);
    }

    public Vector3 multiply(float multiplier) {
        return this.multiply(multiplier, multiplier, multiplier);
    }

    public Vector3 multiply(Vector3 vector3) {
        return this.multiply(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Vector3 multiply(float x, float y, float z) {
        return new Vector3(this.getX() * x, this.getY() * y, this.getZ() * z);
    }

    public Vector3 divide(float divider) {
        return this.divide(divider, divider, divider);
    }

    public Vector3 divide(Vector3 vector3) {
        return this.divide(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Vector3 divide(float x, float y, float z) {
        return new Vector3(this.getX() / x, this.getY() / y, this.getZ() / z);
    }

    public Vector3i toVector3i() {
        return new Vector3i((int)this.x, (int)this.y, (int)this.z);
    }

    public double distanceTo(Vector3i vector3i) {
        return Math.sqrt(Math.pow(this.getX() - vector3i.getX(), 2) + Math.pow(this.getY() - vector3i.getY(), 2) + Math.pow(this.getZ() - vector3i.getZ(), 2));
    }

    public double distanceTo(Vector3 vector3) {
        return Math.sqrt(Math.pow(this.getX() - vector3.getX(), 2) + Math.pow(this.getY() - vector3.getY(), 2) + Math.pow(this.getZ() - vector3.getZ(), 2));
    }

    @Override
    public String toString() {
        return "Vector3(x="+x+",y="+y+",z="+z+")";
    }
}

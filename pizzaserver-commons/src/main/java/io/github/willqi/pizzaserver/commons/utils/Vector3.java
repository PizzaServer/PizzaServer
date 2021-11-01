package io.github.willqi.pizzaserver.commons.utils;

public class Vector3 {

    private float x;
    private float y;
    private float z;


    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
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

    public float getZ() {
        return this.z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public Vector3 add(Vector3 vector3) {
        return this.add(vector3.getX(), vector3.getY(), vector3.getZ());
    }

    public Vector3 add(Vector3i vector3i) {
        return this.add(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3 add(float x, float y, float z) {
        return new Vector3(this.getX() + x, this.getY() + y, this.getZ() + z);
    }

    public Vector3 subtract(Vector3i vector3i) {
        return this.subtract(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3 subtract(Vector3 vector3) {
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

    public Vector3 multiply(Vector3i vector3i) {
        return this.multiply(vector3i.getX(), vector3i.getY(), vector3i.getZ());
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

    public Vector3 divide(Vector3i vector3i) {
        return this.divide(vector3i.getX(), vector3i.getY(), vector3i.getZ());
    }

    public Vector3 divide(float x, float y, float z) {
        return new Vector3(this.getX() / x, this.getY() / y, this.getZ() / z);
    }

    public Vector3i toVector3i() {
        return new Vector3i((int) this.x, (int) this.y, (int) this.z);
    }

    public double distanceBetween(Vector3i vector3i) {
        return Math.sqrt(Math.pow(this.getX() - vector3i.getX(), 2)
                + Math.pow(this.getY() - vector3i.getY(), 2)
                + Math.pow(this.getZ() - vector3i.getZ(), 2));
    }

    public double distanceBetween(Vector3 vector3) {
        return Math.sqrt(Math.pow(this.getX() - vector3.getX(), 2)
                + Math.pow(this.getY() - vector3.getY(), 2)
                + Math.pow(this.getZ() - vector3.getZ(), 2));
    }

    public float getLength() {
        return (float) Math.sqrt(Math.pow(this.getX(), 2)
                + Math.pow(this.getY(), 2)
                + Math.pow(this.getZ(), 2));
    }

    public Vector3 normalize() {
        float length = this.getLength();
        return new Vector3(this.getX() / length, this.getY() / length, this.getZ() / length);
    }

    public Vector3 round() {
        return new Vector3(Math.round(this.getX()), Math.round(this.getY()), Math.round(this.getZ()));
    }

    public float dot(Vector3 vector3) {
        return (this.getX() * vector3.getX()) + (this.getY() * vector3.getY()) + (this.getZ() * vector3.getZ());
    }

    public float dot(Vector3i vector3i) {
        return (this.getX() * vector3i.getX()) + (this.getY() * vector3i.getY()) + (this.getZ() * vector3i.getZ());
    }

    @Override
    public String toString() {
        return "Vector3(x=" + this.x + ", y=" + this.y + ", z=" + this.z + ")";
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Vector3) {
            Vector3 otherVector = (Vector3) obj;
            return NumberUtils.isNearlyEqual(otherVector.getX(), this.getX())
                    && NumberUtils.isNearlyEqual(otherVector.getY(), this.getY())
                    && NumberUtils.isNearlyEqual(otherVector.getZ(), this.getZ());
        }
        return false;
    }

    @Override
    public int hashCode() {
        return (int) ((43 * this.getX()) + (43 * this.getY()) + (43 * this.getZ()));
    }

}

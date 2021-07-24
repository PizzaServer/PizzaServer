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

    public Vector3i toVector3i() {
        return new Vector3i((int)this.x, (int)this.y, (int)this.z);
    }

    public String toString() {
        return "Vector3(x="+x+",y="+y+",z="+z+")";
    }
}

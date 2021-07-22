package io.github.willqi.pizzaserver.commons.utils;

public class BoundingBox {

    private final Vector3 boundariesA;
    private final Vector3 boundariesB;


    public BoundingBox(Vector3 boundariesA, Vector3 boundariesB) {
        this.boundariesA = boundariesA;
        this.boundariesB = boundariesB;
    }

    public Vector3 getBoundariesA() {
        return this.boundariesA;
    }

    public Vector3 getBoundariesB() {
        return this.boundariesB;
    }

}

package io.github.willqi.pizzaserver.commons.utils;

import java.util.Objects;

public class Tuple<A, B> {

    private final A objectA;
    private final B objectB;


    public Tuple(A objectA, B objectB) {
        this.objectA = objectA;
        this.objectB = objectB;
    }

    public A getObjectA() {
        return this.objectA;
    }

    public B getObjectB() {
        return this.objectB;
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.objectA, this.objectB);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple<?, ?> tuple = (Tuple<?, ?>)obj;
            return tuple.getObjectA().equals(this.objectA) && tuple.getObjectB().equals(this.objectB);
        } else {
            return false;
        }
    }
}

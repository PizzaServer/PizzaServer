package io.github.willqi.pizzaserver.commons.utils;

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
        return 31 * this.objectB.hashCode() * this.objectA.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tuple) {
            Tuple<?, ?> tuple = (Tuple<?, ?>)obj;
            return tuple.getObjectA().equals(this.getObjectA()) && tuple.getObjectB().equals(this.getObjectB());
        }
        return false;
    }
}

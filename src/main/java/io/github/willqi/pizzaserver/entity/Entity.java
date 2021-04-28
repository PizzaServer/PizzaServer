package io.github.willqi.pizzaserver.entity;

public abstract class Entity {

    public static long ID;

    private final long id;

    public Entity() {
        this.id = ID++;
    }

    public long getId() {
        return this.id;
    }

}

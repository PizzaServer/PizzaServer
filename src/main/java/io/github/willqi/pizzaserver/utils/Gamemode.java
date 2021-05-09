package io.github.willqi.pizzaserver.utils;

public enum Gamemode {

    SURVIVAL("Survival", 0),
    CREATIVE("Creative", 1),
    ADVENTURE("Adventure", 2);

    private String name;
    private int id;

    Gamemode(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public int getId() {
        return this.id;
    }

}
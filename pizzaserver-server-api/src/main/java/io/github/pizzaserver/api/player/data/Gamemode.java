package io.github.pizzaserver.api.player.data;

public enum Gamemode {

    SURVIVAL("Survival"),
    CREATIVE("Creative"),
    ADVENTURE("Adventure");


    private final String name;


    Gamemode(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }
}
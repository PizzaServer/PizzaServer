package io.github.pizzaserver.api.block.data;

public enum WoodType {
    OAK("Oak"),
    SPRUCE("Spruce"),
    BIRCH("Birch"),
    JUNGLE("Jungle"),
    ACACIA("Acacia"),
    DARK_OAK("Dark Oak"),
    CRIMSON("Crimson"),
    WARPED("Warped");


    private final String name;


    WoodType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}

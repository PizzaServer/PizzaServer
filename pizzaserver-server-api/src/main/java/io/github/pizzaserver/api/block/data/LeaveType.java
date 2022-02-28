package io.github.pizzaserver.api.block.data;

public enum LeaveType {
    OAK("Oak"),
    SPRUCE("Spruce"),
    BIRCH("Birch"),
    JUNGLE("Jungle");

    private final String name;

    LeaveType(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

}
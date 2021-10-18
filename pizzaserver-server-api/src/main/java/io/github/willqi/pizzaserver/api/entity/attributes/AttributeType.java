package io.github.willqi.pizzaserver.api.entity.attributes;

public enum AttributeType {
    HEALTH("minecraft:health"),
    ABSORPTION("minecraft:absorption"),
    FOOD("minecraft:player.hunger"),
    SATURATION("minecraft:player.saturation"),
    EXPERIENCE("minecraft:player.experience"),
    EXPERIENCE_LEVEL("minecraft:player.level"),
    MOVEMENT_SPEED("minecraft:movement");

    private final String id;


    AttributeType(String id) {
        this.id = id;
    }

    public String getId() {
        return this.id;
    }
}
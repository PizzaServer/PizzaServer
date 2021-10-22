package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * Entity component that defines the width and height of an entity.
 * Changing this will NOT affect the dimensions of the entity client side.
 */
public class EntityCollisionBoxComponent extends EntityComponent {

    private final float width;
    private final float height;

    public EntityCollisionBoxComponent(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    @Override
    public String getName() {
        return "minecraft:collision_box";
    }

}

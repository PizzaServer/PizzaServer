package io.github.pizzaserver.api.entity.definition.components.impl;

import io.github.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * Entity component that defines the width and height of an entity.
 */
public class EntityDimensionsComponent extends EntityComponent {

    private final float width;
    private final float height;
    private final float baseOffset;
    private final float eyeHeight;


    public EntityDimensionsComponent(float width, float height) {
        this(width, height, 0);
    }

    public EntityDimensionsComponent(float width, float height, float baseOffset) {
        this(width, height, height / 2 + 0.1f, baseOffset);
    }

    public EntityDimensionsComponent(float width, float height, float baseOffset, float eyeHeight) {
        this.width = width;
        this.height = height;
        this.baseOffset = baseOffset;
        this.eyeHeight = eyeHeight;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float getEyeHeight() {
        return this.eyeHeight;
    }

    public float getBaseOffset() {
        return this.baseOffset;
    }

    @Override
    public String getName() {
        return "minecraft:entity_dimensions";
    }

}

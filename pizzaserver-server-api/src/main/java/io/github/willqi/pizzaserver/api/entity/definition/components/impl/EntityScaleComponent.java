package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * Minecraft entity component to adjust the size of the entity.
 */
public class EntityScaleComponent extends EntityComponent {

    private final float scale;


    public EntityScaleComponent(float scale) {
        this.scale = scale;
    }

    @Override
    public String getName() {
        return "minecraft:scale";
    }

    public float getScale() {
        return this.scale;
    }

    @Override
    public String toString() {
        return super.toString() + "(scale=" + this.getScale() + ")";
    }

}

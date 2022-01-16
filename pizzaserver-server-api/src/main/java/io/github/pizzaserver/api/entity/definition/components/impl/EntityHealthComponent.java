package io.github.pizzaserver.api.entity.definition.components.impl;

import io.github.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * Entity component that defines the health of an entity.
 */
public class EntityHealthComponent extends EntityComponent {

    private final float[] healthRange;
    private final float maximumHealth;

    public EntityHealthComponent(float health, float maximumHealth) {
        this(health, health, maximumHealth);
    }

    public EntityHealthComponent(float[] healthRange, float maximumHealth) {
        this(healthRange[0], healthRange[1], maximumHealth);
    }

    public EntityHealthComponent(float minimumRandomHealth, float maximumRandomHealth, float maximumHealth) {
        this.healthRange = new float[] {minimumRandomHealth, maximumRandomHealth};
        this.maximumHealth = maximumHealth;
    }

    @Override
    public String getName() {
        return "minecraft:health";
    }

    public float[] getHealthRange() {
        return this.healthRange;
    }

    public float getMaximumHealth() {
        return this.maximumHealth;
    }
}

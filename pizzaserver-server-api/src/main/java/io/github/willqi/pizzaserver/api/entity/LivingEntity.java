package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.entity.inventory.LivingEntityInventory;

/**
 * An entity which can be treated as alive.
 */
public interface LivingEntity extends Entity {

    float getHealth();

    void setHealth(float health);

    float getMaxHealth();

    void setMaxHealth(float maxHealth);

    float getAbsorption();

    void setAbsorption(float absorption);

    float getMaxAbsorption();

    void setMaxAbsorption(float maxAbsorption);

    LivingEntityInventory getInventory();

}

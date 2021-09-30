package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.entity.inventory.LivingEntityInventory;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * An entity which can be treated as alive.
 */
public interface LivingEntity extends MovingEntity {

    float getHealth();

    void setHealth(float health);

    float getMaxHealth();

    void setMaxHealth(float maxHealth);

    float getAbsorption();

    void setAbsorption(float absorption);

    float getMaxAbsorption();

    void setMaxAbsorption(float maxAbsorption);

    /**
     * Retrieve the entity's current movement speed per tick.
     * This is used to determine how far this entity's input can move per tick
     * @return movement speed of a entity
     */
    float getMovementSpeed();

    /**
     * Change the entity's movement speed input per tick.
     * @param movementSpeed new movement speed
     */
    void setMovementSpeed(float movementSpeed);

    float getPitch();

    void setPitch(float pitch);

    float getYaw();

    void setYaw(float yaw);

    float getHeadYaw();

    void setHeadYaw(float headYaw);

    Vector3 getDirectionVector();

    LivingEntityInventory getInventory();

}

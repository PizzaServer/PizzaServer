package io.github.pizzaserver.api.entity.definition.components.impl;

import io.github.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * Entity Component which manages hunger
 */
public class EntityHungerComponent extends EntityComponent {
    //TODO: Add maxes for everything?

    // 0-20 for fullness on a normal player
    private int foodLevel;
    // The invisible 0-20 which is depleted before taking hunger
    private int foodSaturationLevel;
    // A timer for regeneration, if the entity is at 17 or higher hunger, they regenerate 1 half heart every 4 seconds
    // Same time for starving
    // If it's full, 1/6 * 1 half heart * saturation level is restored to a max of 1 half heart, when it reaches 10.5s
    // it's reduces to 0
    private float foodTickTimer;
    // A number which increases based on actions. Once it reaches 4, it resets and one point is removed from the
    // saturation level, then the food level if all saturation is gone
    private float foodExhaustionLevel;

    public EntityHungerComponent() {
        this(20, 5, 0, 0);
    }

    public EntityHungerComponent(int foodLevel, int foodSaturationLevel, float foodTickTimer, float foodExhaustionLevel) {
        this.foodLevel = foodLevel;
        this.foodSaturationLevel = foodSaturationLevel;
        this.foodTickTimer = foodTickTimer;
        this.foodExhaustionLevel = foodExhaustionLevel;
    }

    public int getFoodLevel() {
        return foodLevel;
    }

    public void setFoodLevel(int foodLevel) {
        this.foodLevel = foodLevel;
    }

    public int getFoodSaturationLevel() {
        return foodSaturationLevel;
    }

    public void setFoodSaturationLevel(int foodSaturationLevel) {
        this.foodSaturationLevel = foodSaturationLevel;
    }

    public float getFoodTickTimer() {
        return foodTickTimer;
    }

    public void setFoodTickTimer(float foodTickTimer) {
        this.foodTickTimer = foodTickTimer;
    }

    public float getFoodExhaustionLevel() {
        return foodExhaustionLevel;
    }

    public void setFoodExhaustionLevel(float foodExhaustionLevel) {
        this.foodExhaustionLevel = foodExhaustionLevel;
    }

    @Override
    public String getName() {
        return "minecraft:hunger";
    }
}

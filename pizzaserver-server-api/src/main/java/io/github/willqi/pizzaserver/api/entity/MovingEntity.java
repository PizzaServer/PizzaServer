package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.level.world.World;

/**
 * An entity which has the ability to move from its spawned location.
 */
public interface MovingEntity extends Entity {

    void moveTo(float x, float y, float z);

    void teleport(float x, float y, float z);

    void teleport(World world, float x, float y, float z);

}

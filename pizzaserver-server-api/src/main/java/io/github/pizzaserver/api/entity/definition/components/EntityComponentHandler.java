package io.github.pizzaserver.api.entity.definition.components;

import io.github.pizzaserver.api.entity.Entity;

public abstract class EntityComponentHandler<T extends EntityComponent> {

    /**
     * Called when this entity component is applied to an entity.
     * Used to register this component's data to an entity.
     *
     * @param entity    entity being registered for this component
     * @param component component being registered
     */
    public void onRegistered(Entity entity, T component) {

    }

    /**
     * Called when this entity component is to be removed from an entity.
     * Used to unregister this component's data from an entity.
     *
     * @param entity    entity being unregistered
     * @param component the component being unregistered
     */
    public void onUnregistered(Entity entity, T component) {

    }
}

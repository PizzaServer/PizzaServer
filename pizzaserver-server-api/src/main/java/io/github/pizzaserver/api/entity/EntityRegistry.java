package io.github.pizzaserver.api.entity;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;

import java.util.*;

public interface EntityRegistry {

    /**
     * Register an entity type.
     * @param entityDefinition definition class
     */
    void registerDefinition(EntityDefinition entityDefinition);

    /**
     * Register a new entity component.
     * @param componentClazz class of the component
     * @param defaultComponent default settings component
     * @param handler handles applying/removing said component
     */
    <T extends EntityComponent> void registerComponent(Class<T> componentClazz, T defaultComponent, EntityComponentHandler<T> handler);

    EntityDefinition getDefinition(String entityId);

    boolean hasEntityDefinition(String entityId);

    <T extends EntityComponent> EntityComponentHandler<T> getComponentHandler(Class<T> componentClazz);

    <T extends EntityComponent> T getDefaultComponent(Class<T> componentClazz);

    Set<Class<? extends EntityComponent>> getComponentClasses();

    Entity getEntity(String entityId);

    ItemEntity getItemEntity(ItemStack itemStack);

    Set<EntityDefinition> getDefinitions();

    static EntityRegistry getInstance() {
        return Server.getInstance().getEntityRegistry();
    }

}

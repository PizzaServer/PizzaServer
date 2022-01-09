package io.github.pizzaserver.server.entity;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.pizzaserver.api.entity.definition.impl.ItemEntityDefinition;
import io.github.pizzaserver.api.item.ItemStack;

import java.util.*;

public class ImplEntityRegistry implements EntityRegistry {

    private final Map<String, EntityDefinition> definitions = new HashMap<>();
    private final Map<Class<? extends EntityComponent>, EntityComponentHandler<?>> componentHandlers = new HashMap<>();
    private final Map<Class<? extends EntityComponent>, EntityComponent> defaultComponents = new HashMap<>();

    @Override
    public void registerDefinition(EntityDefinition entityDefinition) {
        this.definitions.put(entityDefinition.getId(), entityDefinition);
    }

    @Override
    public <T extends EntityComponent> void registerComponent(Class<T> componentClazz, T defaultComponent, EntityComponentHandler<T> handler) {
        this.componentHandlers.put(componentClazz, handler);
        this.defaultComponents.put(componentClazz, defaultComponent);
    }

    @Override
    public EntityDefinition getDefinition(String entityId) {
        if (!this.definitions.containsKey(entityId)) {
            throw new NullPointerException("Could not find a entity definition by the id of " + entityId);
        }
        return this.definitions.get(entityId);
    }

    @Override
    public boolean hasEntityDefinition(String entityId) {
        return this.definitions.containsKey(entityId);
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EntityComponent> EntityComponentHandler<T> getComponentHandler(Class<T> componentClazz) {
        EntityComponentHandler<T> handler = (EntityComponentHandler<T>) this.componentHandlers.getOrDefault(componentClazz, null);
        if (handler == null) {
            throw new NullPointerException("Misconfigured component. The requested component does not have a component handler.");
        }

        return handler;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T extends EntityComponent> T getDefaultComponent(Class<T> componentClazz) {
        T defaultComponent = (T) this.defaultComponents.getOrDefault(componentClazz, null);
        if (defaultComponent == null) {
            throw new NullPointerException("Misconfigured component. The requested component does not have a default component.");
        }

        return defaultComponent;
    }

    @Override
    public Set<Class<? extends EntityComponent>> getComponentClasses() {
        return Collections.unmodifiableSet(this.componentHandlers.keySet());
    }

    @Override
    public Entity getEntity(String entityId) {
        EntityDefinition entityDefinition = this.getDefinition(entityId);
        Entity entity;
        switch (entityDefinition.getId()) {
            case HumanEntityDefinition.ID:
                entity = new ImplHumanEntity(entityDefinition);
                break;
            case ItemEntityDefinition.ID:
                entity = new ImplItemEntity(entityDefinition);
                break;
            default:
                entity = new ImplEntity(entityDefinition);
                break;
        }

        entityDefinition.onCreation(entity);
        return entity;
    }

    @Override
    public ItemEntity getItemEntity(ItemStack itemStack) {
        ItemEntity entity = (ItemEntity) this.getEntity(ItemEntityDefinition.ID);
        entity.setItem(itemStack);
        return entity;
    }

    @Override
    public Set<EntityDefinition> getDefinitions() {
        return Collections.unmodifiableSet(new HashSet<>(this.definitions.values()));
    }

}

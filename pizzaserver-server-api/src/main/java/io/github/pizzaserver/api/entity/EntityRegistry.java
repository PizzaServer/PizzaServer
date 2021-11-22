package io.github.pizzaserver.api.entity;

import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.impl.ItemEntityDefinition;

import java.util.*;
import java.util.function.Function;

public class EntityRegistry {

    private static final Map<String, EntityDefinition> definitions = new HashMap<>();
    private static final Map<Class<? extends EntityComponent>, EntityComponentHandler<?>> componentHandlers = new HashMap<>();
    private static final Map<Class<? extends EntityComponent>, EntityComponent> defaultComponents = new HashMap<>();

    private static Function<EntityDefinition, Entity> entityConstructor;

    /**
     * Register an entity type.
     * @param entityDefinition definition class
     */
    public static void registerDefinition(EntityDefinition entityDefinition) {
        definitions.put(entityDefinition.getId(), entityDefinition);
    }

    /**
     * Register a new entity component.
     * @param componentClazz class of the component
     * @param defaultComponent default settings component
     * @param handler handles applying/removing said component
     */
    public static <T extends EntityComponent> void registerComponent(Class<T> componentClazz, T defaultComponent, EntityComponentHandler<T> handler) {
        componentHandlers.put(componentClazz, handler);
        defaultComponents.put(componentClazz, defaultComponent);
    }

    public static EntityDefinition getDefinition(String entityId) {
        if (!definitions.containsKey(entityId)) {
            throw new NullPointerException("Could not find a entity definition by the id of " + entityId);
        }
        return definitions.get(entityId);
    }

    public static boolean hasEntityDefinition(String entityId) {
        return definitions.containsKey(entityId);
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityComponent> EntityComponentHandler<T> getComponentHandler(Class<T> componentClazz) {
        EntityComponentHandler<T> handler = (EntityComponentHandler<T>) componentHandlers.getOrDefault(componentClazz, null);
        if (handler == null) {
            throw new NullPointerException("Misconfigured component. The requested component does not have a component handler.");
        }

        return handler;
    }

    @SuppressWarnings("unchecked")
    public static <T extends EntityComponent> T getDefaultComponent(Class<T> componentClazz) {
        T defaultComponent = (T) defaultComponents.getOrDefault(componentClazz, null);
        if (defaultComponent == null) {
            throw new NullPointerException("Misconfigured component. The requested component does not have a default component.");
        }

        return defaultComponent;
    }

    public static Set<Class<? extends EntityComponent>> getComponentClasses() {
        return Collections.unmodifiableSet(componentHandlers.keySet());
    }

    public static Entity getEntity(String entityId) {
        EntityDefinition entityDefinition = getDefinition(entityId);
        Entity entity = entityConstructor.apply(entityDefinition);

        entityDefinition.onCreation(entity);
        return entity;
    }

    public static ItemEntity getItemEntity(ItemStack itemStack) {
        ItemEntity entity = (ItemEntity) getEntity(ItemEntityDefinition.ID);
        entity.setItem(itemStack);
        return entity;
    }

    public static Set<EntityDefinition> getDefinitions() {
        return Collections.unmodifiableSet(new HashSet<>(definitions.values()));
    }

    /**
     * Called internally to set the entity constructor to use.
     * Used to abstract internal server workings away from plugin api.
     * @param constructor entity constructor
     */
    public static void setConstructor(Function<EntityDefinition, Entity> constructor) {
        entityConstructor = constructor;
    }

}

package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.types.EntityType;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class EntityRegistry {

    private static final Map<String, EntityType> types = new HashMap<>();

    private static Function<EntityType, Entity> entityConstructor;

    public static void register(EntityType entityType) {
        types.put(entityType.getEntityId(), entityType);
    }

    public static EntityType getEntityType(String entityId) {
        if (!types.containsKey(entityId)) {
            throw new NullPointerException("Could not find a entity type by the id of " + entityId);
        }
        return types.get(entityId);
    }

    public static boolean hasEntityType(String entityId) {
        return types.containsKey(entityId);
    }

    public static Entity getEntity(String entityId) {
        EntityType entityType = getEntityType(entityId);
        return entityConstructor.apply(entityType);
    }

    /**
     * Called internally to set the entity constructor to use.
     * Used to abstract internal server workings away from plugin api.
     * @param constructor entity constructor
     */
    public static void setEntityConstructor(Function<EntityType, Entity> constructor) {
        entityConstructor = constructor;
    }

}

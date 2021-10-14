package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.entity.types.EntityType;

import java.util.HashMap;
import java.util.Map;

public class EntityRegistry {

    private static final Map<String, EntityType> types = new HashMap<>();


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

    public static Entity createEntity(String entityId) {
        // TODO: implement
        EntityType entityType = getEntityType(entityId);
        throw new UnsupportedOperationException();
    }

}

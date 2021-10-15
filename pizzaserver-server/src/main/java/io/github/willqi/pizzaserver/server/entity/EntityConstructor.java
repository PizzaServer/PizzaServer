package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.types.EntityType;
import io.github.willqi.pizzaserver.api.entity.types.impl.HumanEntityType;

import java.util.function.Function;

public class EntityConstructor implements Function<EntityType, Entity> {

    private final Server server;


    public EntityConstructor(Server server) {
        this.server = server;
    }

    @Override
    public Entity apply(EntityType entityType) {
        switch (entityType.getEntityId()) {
            case HumanEntityType.ID:
                return new ImplHumanEntity((HumanEntityType) EntityRegistry.getEntityType(HumanEntityType.ID));
            default:
                return null;
        }
    }

}

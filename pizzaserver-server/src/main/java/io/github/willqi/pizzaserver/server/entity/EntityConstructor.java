package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;

import java.util.function.Function;

public class EntityConstructor implements Function<EntityDefinition, Entity> {

    private final Server server;


    public EntityConstructor(Server server) {
        this.server = server;
    }

    @Override
    public Entity apply(EntityDefinition entityDefinition) {
        if (entityDefinition.getEntityId().equals(HumanEntityDefinition.ID)) {
            return new ImplHumanEntity((HumanEntityDefinition) EntityRegistry.getDefinition(HumanEntityDefinition.ID));
        } else {
            return new ImplLivingEntity(entityDefinition);
        }
    }

}

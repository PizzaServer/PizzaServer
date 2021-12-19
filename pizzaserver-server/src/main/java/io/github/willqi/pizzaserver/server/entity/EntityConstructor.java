package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.impl.ItemEntityDefinition;

import java.util.function.Function;

public class EntityConstructor implements Function<EntityDefinition, Entity> {

    @Override
    public Entity apply(EntityDefinition entityDefinition) {
        // TODO: ImplEntity creation if it is not alive
        switch (entityDefinition.getId()) {
            case HumanEntityDefinition.ID:
                return new ImplHumanEntity((HumanEntityDefinition) entityDefinition);
            case ItemEntityDefinition.ID:
                return new ImplItemEntity((ItemEntityDefinition) entityDefinition);
            default:
                return new ImplEntity(entityDefinition);
        }
    }

}

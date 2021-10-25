package io.github.willqi.pizzaserver.api.entity.definition.components.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityDeathMessageComponent;

public class EntityDeathMessageComponentHandler extends EntityComponentHandler<EntityDeathMessageComponent> {

    @Override
    public void onRegistered(Entity entity, EntityDeathMessageComponent component) {
        entity.setShowDeathMessages(component.showDeathMessages());
    }

}

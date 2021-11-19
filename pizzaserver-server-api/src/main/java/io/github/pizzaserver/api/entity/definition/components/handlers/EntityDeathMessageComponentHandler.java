package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDeathMessageComponent;
import io.github.pizzaserver.api.entity.Entity;

public class EntityDeathMessageComponentHandler extends EntityComponentHandler<EntityDeathMessageComponent> {

    @Override
    public void onRegistered(Entity entity, EntityDeathMessageComponent component) {
        entity.setShowDeathMessages(component.showDeathMessages());
    }

}

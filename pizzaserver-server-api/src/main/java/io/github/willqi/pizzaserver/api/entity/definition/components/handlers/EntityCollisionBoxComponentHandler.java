package io.github.willqi.pizzaserver.api.entity.definition.components.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityCollisionBoxComponent;

public class EntityCollisionBoxComponentHandler extends EntityComponentHandler<EntityCollisionBoxComponent> {

    @Override
    public void onRegistered(Entity entity, EntityCollisionBoxComponent component) {
        entity.setWidth(component.getWidth());
        entity.setHeight(component.getHeight());
    }

}

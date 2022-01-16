package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDimensionsComponent;

public class EntityDimensionsComponentHandler extends EntityComponentHandler<EntityDimensionsComponent> {

    @Override
    public void onRegistered(Entity entity, EntityDimensionsComponent component) {
        entity.setWidth(component.getWidth());
        entity.setHeight(component.getHeight());
        entity.setEyeHeight(component.getEyeHeight());
        entity.setBaseOffset(component.getBaseOffset());
    }
}

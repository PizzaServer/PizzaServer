package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDimensionsComponent;
import io.github.pizzaserver.api.keychain.EntityKeys;

public class EntityDimensionsComponentHandler extends EntityComponentHandler<EntityDimensionsComponent> {

    @Override
    public void onRegistered(Entity entity, EntityDimensionsComponent component) {
        entity.set(EntityKeys.BOUNDING_BOX_WIDTH, component.getWidth());
        entity.set(EntityKeys.BOUNDING_BOX_HEIGHT, component.getHeight());
        entity.setEyeHeight(component.getEyeHeight());
        entity.setBaseOffset(component.getBaseOffset());
    }

}

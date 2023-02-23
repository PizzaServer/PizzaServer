package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityScaleComponent;
import io.github.pizzaserver.api.keychain.EntityKeys;

public class EntityScaleComponentHandler extends EntityComponentHandler<EntityScaleComponent> {

    @Override
    public void onRegistered(Entity entity, EntityScaleComponent component) {
        entity.set(EntityKeys.SCALE, component.getScale());
    }

    @Override
    public void onUnregistered(Entity entity, EntityScaleComponent component) {
        entity.set(EntityKeys.SCALE, entity.getComponent(EntityScaleComponent.class).getScale());
    }

}

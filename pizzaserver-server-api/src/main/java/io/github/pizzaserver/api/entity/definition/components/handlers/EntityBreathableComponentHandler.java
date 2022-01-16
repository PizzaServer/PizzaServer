package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBreathableComponent;

public class EntityBreathableComponentHandler extends EntityComponentHandler<EntityBreathableComponent> {

    @Override
    public void onRegistered(Entity entity, EntityBreathableComponent component) {
        entity.setMaxAirSupplyTicks((int) Math.ceil(component.getTotalSupplyTime() * 20));

        if (!entity.hasComponent(EntityBreathableComponent.class)) {
            entity.setAirSupplyTicks(entity.getMaxAirSupplyTicks());
        }
    }
}

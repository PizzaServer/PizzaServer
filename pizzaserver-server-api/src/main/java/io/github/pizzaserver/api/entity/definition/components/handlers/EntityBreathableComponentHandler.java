package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBreathableComponent;
import io.github.pizzaserver.api.keychain.EntityKeys;

public class EntityBreathableComponentHandler extends EntityComponentHandler<EntityBreathableComponent> {

    @Override
    public void onRegistered(Entity entity, EntityBreathableComponent component) {
        entity.set(EntityKeys.MAX_BREATHING_TICKS, (int) Math.ceil(component.getTotalSupplyTime() * 20));

        if (!entity.hasComponent(EntityBreathableComponent.class)) {
            int validatedMax = entity.expect(EntityKeys.MAX_BREATHING_TICKS);
            entity.set(EntityKeys.BREATHING_TICKS_REMAINING, validatedMax);
        }
    }

}

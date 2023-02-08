package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;
import io.github.pizzaserver.api.keychain.EntityKeys;

public class EntityHealthComponentHandler extends EntityComponentHandler<EntityHealthComponent> {

    @Override
    public void onRegistered(Entity entity, EntityHealthComponent component) {
        entity.set(EntityKeys.MAX_HEALTH, component.getMaximumHealth());

        // Only set the health of the entity if the entity did not have a previous health entity component
        if (!entity.hasComponent(EntityHealthComponent.class)) {
            float minimumHealth = component.getHealthRange()[0];
            float maximumHealth = component.getHealthRange()[1];

            entity.set(EntityKeys.HEALTH, (float) Math.floor(Math.random() * (maximumHealth - minimumHealth) + minimumHealth));
        }
    }

}

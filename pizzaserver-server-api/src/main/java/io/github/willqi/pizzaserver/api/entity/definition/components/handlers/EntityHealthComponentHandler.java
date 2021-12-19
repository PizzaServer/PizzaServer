package io.github.willqi.pizzaserver.api.entity.definition.components.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;

public class EntityHealthComponentHandler extends EntityComponentHandler<EntityHealthComponent> {

    @Override
    public void onRegistered(Entity entity, EntityHealthComponent component) {
        entity.setMaxHealth(component.getMaximumHealth());

        // Only set the health of the entity if the entity did not have a previous health entity component
        if (!entity.hasComponent(EntityHealthComponent.class)) {
            float minimumHealth = component.getHealthRange()[0];
            float maximumHealth = component.getHealthRange()[1];

            entity.setHealth((float) Math.floor(Math.random() * (maximumHealth - minimumHealth) + minimumHealth));
        }
    }

}

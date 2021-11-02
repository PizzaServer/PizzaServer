package io.github.willqi.pizzaserver.api.entity.definition.components.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;

public class EntityPhysicsComponentHandler extends EntityComponentHandler<EntityPhysicsComponent> {

    @Override
    public void onRegistered(Entity entity, EntityPhysicsComponent component) {
        entity.setGravity(component.hasGravity());
        entity.setCollision(component.hasCollision());
    }

}

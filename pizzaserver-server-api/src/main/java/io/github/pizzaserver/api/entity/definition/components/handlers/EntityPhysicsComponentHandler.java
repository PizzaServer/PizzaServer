package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;

public class EntityPhysicsComponentHandler extends EntityComponentHandler<EntityPhysicsComponent> {

    @Override
    public void onRegistered(Entity entity, EntityPhysicsComponent component) {
        entity.setGravity(component.hasGravity());
        entity.setCollision(component.hasCollision());
        entity.setPushable(component.isPushable());
        entity.setPistonPushable(component.isPistonPushable());
    }

}

package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;
import io.github.pizzaserver.api.keychain.EntityKeys;

public class EntityPhysicsComponentHandler extends EntityComponentHandler<EntityPhysicsComponent> {

    @Override
    public void onRegistered(Entity entity, EntityPhysicsComponent component) {
        entity.set(EntityKeys.GRAVITY_ENABLED, component.hasGravity());
        entity.set(EntityKeys.COLLISION_ENABLED, component.hasCollision());
        entity.setPushable(component.isPushable());
        entity.setPistonPushable(component.isPistonPushable());
    }

}

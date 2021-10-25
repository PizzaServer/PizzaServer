package io.github.willqi.pizzaserver.api.entity.definition.components.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityLootComponent;

import java.util.ArrayList;

public class EntityLootComponentHandler extends EntityComponentHandler<EntityLootComponent> {

    @Override
    public void onRegistered(Entity entity, EntityLootComponent component) {
        entity.setLoot(component.getLoot());
    }

    @Override
    public void onUnregistered(Entity entity, EntityLootComponent component) {
        entity.setLoot(new ArrayList<>());
    }

}

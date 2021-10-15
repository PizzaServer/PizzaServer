package io.github.willqi.pizzaserver.api.entity.types;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.EntityRegistry;
import io.github.willqi.pizzaserver.api.entity.types.spawnrules.EntitySpawnRules;

public abstract class BaseEntityType implements EntityType {

    @Override
    public Entity create() {
        return EntityRegistry.getEntity(this.getEntityId());
    }

    @Override
    public EntitySpawnRules getSpawnRules() {
        return null;
    }

    @Override
    public void onCreation(Entity entity) {}

}

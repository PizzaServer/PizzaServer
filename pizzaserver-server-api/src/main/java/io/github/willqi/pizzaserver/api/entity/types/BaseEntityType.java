package io.github.willqi.pizzaserver.api.entity.types;

import io.github.willqi.pizzaserver.api.entity.types.spawnrules.EntitySpawnRules;

public abstract class BaseEntityType implements EntityType {

    @Override
    public EntitySpawnRules getSpawnRules() {
        return null;
    }

}

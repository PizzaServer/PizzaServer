package io.github.willqi.pizzaserver.api.entity.types;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.types.spawnrules.EntitySpawnRules;

public interface EntityType {

    String getEntityId();

    EntitySpawnRules getSpawnRules();

    void onCreation(Entity entity);

}

package io.github.willqi.pizzaserver.api.entity.definition;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.definition.spawnrules.EntitySpawnRules;

public interface EntityDefinition {

    String getEntityId();

    EntityComponentGroup getComponentGroup(String groupId);

    MinecraftComponentEvent getEvent(String eventId);

    EntitySpawnRules getSpawnRules();

    Entity create();

    void onCreation(Entity entity);

}

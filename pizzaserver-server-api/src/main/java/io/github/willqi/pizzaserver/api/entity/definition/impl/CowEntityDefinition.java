package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;

public class CowEntityDefinition extends BaseEntityDefinition {

    @Override
    public String getEntityId() {
        return "minecraft:cow";
    }

    @Override
    public void onCreation(Entity entity) {

    }


}

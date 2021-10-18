package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;

public class CowEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:cow";

    @Override
    public String getEntityId() {
        return ID;
    }

    @Override
    public void onCreation(Entity entity) {

    }


}

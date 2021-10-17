package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;

public class ItemEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:item";

    @Override
    public String getEntityId() {
        return ID;
    }

}

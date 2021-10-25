package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;

public class ItemEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:item";


    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Item";
    }

    @Override
    public void onCreation(Entity entity) {
        entity.setVulnerable(false);
    }

}

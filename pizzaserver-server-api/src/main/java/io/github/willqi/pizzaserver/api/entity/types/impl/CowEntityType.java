package io.github.willqi.pizzaserver.api.entity.types.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.types.BaseEntityType;

public class CowEntityType extends BaseEntityType {

    @Override
    public String getEntityId() {
        return "minecraft:cow";
    }

    @Override
    public void onCreation(Entity entity) {
        entity.setEntityBehaviour(null);
    }


}

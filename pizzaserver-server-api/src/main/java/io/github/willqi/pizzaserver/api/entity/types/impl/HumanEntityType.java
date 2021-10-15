package io.github.willqi.pizzaserver.api.entity.types.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.types.BaseEntityType;
import io.github.willqi.pizzaserver.api.entity.types.behaviour.impl.HumanEntityBehaviour;

public class HumanEntityType extends BaseEntityType {

    public static final String ID = "minecraft:player";

    @Override
    public String getEntityId() {
        return ID;
    }

    @Override
    public void onCreation(Entity entity) {
        entity.setEntityBehaviour(new HumanEntityBehaviour(entity));
    }

}

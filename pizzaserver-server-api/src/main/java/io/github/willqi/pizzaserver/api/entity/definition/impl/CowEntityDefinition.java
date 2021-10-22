package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityCollisionBoxComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;

public class CowEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:cow";


    public CowEntityDefinition() {
        this.registerComponentGroup(new EntityComponentGroup("minecraft:default", new EntityComponent[] {
                new EntityCollisionBoxComponent(0.9f, 1.3f),
                new EntityHealthComponent(10f, 10f) }));
    }

    @Override
    public String getEntityId() {
        return ID;
    }

    @Override
    public void onCreation(Entity entity) {
        entity.addComponentGroup("minecraft:default");
    }


}

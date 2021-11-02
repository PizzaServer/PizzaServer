package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityCollisionBoxComponent;

public class ItemEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:item";


    public ItemEntityDefinition() {
        this.registerComponentGroup(new EntityComponentGroup("minecraft:default", new EntityComponent[]{
                new EntityCollisionBoxComponent(0.25f, 0.25f) }));
    }

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
        entity.addComponentGroup("minecraft:default");
        entity.setVulnerable(false);
    }

}

package io.github.pizzaserver.api.entity.definition.impl;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.BaseEntityDefinition;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDimensionsComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;

public class BoatEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:boat";


    public BoatEntityDefinition() {
        this.registerComponentGroup(new EntityComponentGroup("minecraft:default", new EntityComponent[] {
            new EntityDimensionsComponent(1.4f, 0.455f, 0.455f / 2 + 0.1f, 0.375f),
            new EntityHealthComponent(20, 20) }));
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Boat";
    }

    @Override
    public void onCreation(Entity entity) {
        entity.addComponentGroup("minecraft:default");
    }

}

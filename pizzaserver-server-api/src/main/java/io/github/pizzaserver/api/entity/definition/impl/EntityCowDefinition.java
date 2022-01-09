package io.github.pizzaserver.api.entity.definition.impl;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.BaseEntityDefinition;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBreathableComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDimensionsComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;

public class EntityCowDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:cow";


    public EntityCowDefinition() {
        this.registerComponentGroup(new EntityComponentGroup("minecraft:default", new EntityComponent[] {
                new EntityDimensionsComponent(0.9f, 1.3f),
                new EntityHealthComponent(10f, 10f),
                new EntityBreathableComponent(new EntityBreathableComponent.Properties()
                        .setTotalSupplyTime(15)
                        .setSuffocationInterval(10)
                        .setGenerateBubblesInWater(true)) }));
    }

    @Override
    public String getId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Cow";
    }

    @Override
    public void onCreation(Entity entity) {
        entity.addComponentGroup("minecraft:default");
    }


}

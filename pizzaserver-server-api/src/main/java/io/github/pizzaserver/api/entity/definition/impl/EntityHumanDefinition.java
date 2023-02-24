package io.github.pizzaserver.api.entity.definition.impl;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.definition.BaseEntityDefinition;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBreathableComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityDimensionsComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;

public class EntityHumanDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:player";


    public EntityHumanDefinition() {
        this.registerComponentGroup(new EntityComponentGroup("minecraft:default", new EntityComponent[] {
                new EntityDimensionsComponent(0.6f, 1.8f, 1.62f, 1.62f),
                new EntityHealthComponent(20f, 20f),
                new EntityPhysicsComponent(new EntityPhysicsComponent.Properties()
                        .setHasGravity(true)
                        .setCollision(true)
                        .setPistonPushable(true)
                        .setPushable(false)
                        .setGravity(0.08f)
                        .setDrag(0.02f)),
                new EntityBreathableComponent(new EntityBreathableComponent.Properties()
                        .setTotalSupplyTime(20)
                        .setSuffocationInterval(10)
                        .setGenerateBubblesInWater(true)
                        .setInhaleTime(3.75f)) }));
    }

    @Override
    public String getEntityId() {
        return ID;
    }

    @Override
    public String getName() {
        return "Human";
    }

    @Override
    public boolean isSummonable() {
        return false;
    }

    @Override
    public boolean hasSpawnEgg() {
        return false;
    }

    @Override
    public void onCreation(Entity entity) {
        entity.addComponentGroup("minecraft:default");
    }

}

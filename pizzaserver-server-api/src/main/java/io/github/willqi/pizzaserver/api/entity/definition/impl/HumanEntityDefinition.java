package io.github.willqi.pizzaserver.api.entity.definition.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.definition.BaseEntityDefinition;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityBreathableComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityCollisionBoxComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityHealthComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityPhysicsComponent;

public class HumanEntityDefinition extends BaseEntityDefinition {

    public static final String ID = "minecraft:player";


    public HumanEntityDefinition() {
        this.registerComponentGroup(new EntityComponentGroup("minecraft:default", new EntityComponent[] {
                new EntityCollisionBoxComponent(0.6f, 1.8f),
                new EntityHealthComponent(20f, 20f),
                new EntityPhysicsComponent(new EntityPhysicsComponent.Properties().setGravity(true).setCollision(true).setPistonPushable(true).setPushable(false)),
                new EntityBreathableComponent(new EntityBreathableComponent.Properties()
                        .setTotalSupplyTime(20)
                        .setSuffocationInterval(10)
                        .setGenerateBubblesInWater(true)
                        .setInhaleTime(3.75f)) }));
    }

    @Override
    public String getId() {
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

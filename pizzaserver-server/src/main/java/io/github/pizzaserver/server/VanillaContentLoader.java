package io.github.pizzaserver.server;

import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.impl.BlockEntityTypeChest;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.components.handlers.*;
import io.github.pizzaserver.api.entity.definition.components.impl.*;
import io.github.pizzaserver.api.block.types.impl.*;
import io.github.pizzaserver.api.entity.definition.impl.CowEntityDefinition;
import io.github.pizzaserver.api.entity.definition.impl.HumanEntityDefinition;
import io.github.pizzaserver.api.entity.definition.impl.ItemEntityDefinition;

import java.util.ArrayList;

public class VanillaContentLoader {

    public static void load() {
        loadItems();
        loadBlocks();
        loadBlockEntities();
        loadEntityComponents();
        loadEntities();
    }

    private static void loadItems() {

    }

    private static void loadBlocks() {
        BlockRegistry.getInstance().register(new BlockTypeAir());
        BlockRegistry.getInstance().register(new BlockTypeDirt());
        BlockRegistry.getInstance().register(new BlockTypeGrass());
        BlockRegistry.getInstance().register(new BlockTypeStone());
        BlockRegistry.getInstance().register(new BlockTypeWater());
        BlockRegistry.getInstance().register(new BlockTypeFlowingWater());
        BlockRegistry.getInstance().register(new BlockTypeChest());
    }

    private static void loadBlockEntities() {
        BlockEntityRegistry.getInstance().register(new BlockEntityTypeChest());
    }

    private static void loadEntityComponents() {
        EntityRegistry.getInstance().registerComponent(EntityScaleComponent.class,
                new EntityScaleComponent(1),
                new EntityScaleComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityCollisionBoxComponent.class,
                new EntityCollisionBoxComponent(0f, 0f),
                new EntityCollisionBoxComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityHealthComponent.class,
                new EntityHealthComponent(0, 0, 0),
                new EntityHealthComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityDamageSensorComponent.class,
                new EntityDamageSensorComponent(new EntityDamageSensorComponent.Sensor[0]),
                new EntityEmptyComponentHandler<>());
        EntityRegistry.getInstance().registerComponent(EntityLootComponent.class,
                new EntityLootComponent(new ArrayList<>()),
                new EntityLootComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityDeathMessageComponent.class,
                new EntityDeathMessageComponent(false),
                new EntityDeathMessageComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityPhysicsComponent.class, new EntityPhysicsComponent(new EntityPhysicsComponent.Properties()
                .setCollision(true)
                .setGravity(true)
                .setPushable(true)
                .setPistonPushable(true)
                .setGravityForce(0.08f)
                .setDragForce(0.02f)
                .setApplyDragBeforeGravity(false)), new EntityPhysicsComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityBossComponent.class,
                new EntityBossComponent(null, -1, false),
                new EntityBossComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityBurnsInDaylightComponent.class,
                new EntityBurnsInDaylightComponent(),
                new EntityEmptyComponentHandler<>());
        EntityRegistry.getInstance().registerComponent(EntityBreathableComponent.class,
                new EntityBreathableComponent(new EntityBreathableComponent.Properties()
                    .setGenerateBubblesInWater(true)
                    .setSuffocationInterval(10)
                    .setInhaleTime(5)
                    .setTotalSupplyTime(15)
        ), new EntityBreathableComponentHandler());
    }

    private static void loadEntities() {
        EntityRegistry.getInstance().registerDefinition(new HumanEntityDefinition());
        EntityRegistry.getInstance().registerDefinition(new ItemEntityDefinition());
        EntityRegistry.getInstance().registerDefinition(new CowEntityDefinition());
    }

}

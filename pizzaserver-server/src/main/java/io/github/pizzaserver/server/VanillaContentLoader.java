package io.github.pizzaserver.server;

import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.entity.definition.impl.BoatEntityDefinition;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.data.WoodType;
import io.github.pizzaserver.api.item.types.ItemTypeID;
import io.github.pizzaserver.api.item.types.impl.*;
import io.github.pizzaserver.server.blockentity.types.impl.*;
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
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.ACACIA_BOAT, WoodType.ACACIA));
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.BIRCH_BOAT, WoodType.BIRCH));
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.BOAT, WoodType.OAK));   // Why does Microsoft have minecraft:boat and minecraft:oak_boat?...
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.DARK_OAK_BOAT, WoodType.DARK_OAK));
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.JUNGLE_BOAT, WoodType.JUNGLE));
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.OAK_BOAT, WoodType.OAK));
        ItemRegistry.getInstance().register(new ItemTypeBoat(ItemTypeID.SPRUCE_BOAT, WoodType.SPRUCE));
        ItemRegistry.getInstance().register(new ItemTypeShears());
        ItemRegistry.getInstance().register(new ItemTypeStonePickaxe());
        ItemRegistry.getInstance().register(new ItemTypeWoodenPickaxe());
        ItemRegistry.getInstance().register(new ItemTypeWoodenSword());
        ItemRegistry.getInstance().register(new ItemTypeDiamondHelmet());
    }

    private static void loadBlocks() {
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.ACACIA_BUTTON, WoodType.ACACIA));
        BlockRegistry.getInstance().register(new BlockTypeAir());
        BlockRegistry.getInstance().register(new BlockTypeBed());
        BlockRegistry.getInstance().register(new BlockTypeBell());
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.BIRCH_BUTTON, WoodType.BIRCH));
        BlockRegistry.getInstance().register(new BlockTypeBlastFurnace());
        BlockRegistry.getInstance().register(new BlockTypeCampfire());
        BlockRegistry.getInstance().register(new BlockTypeCauldron());
        BlockRegistry.getInstance().register(new BlockTypeChest());
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.CRIMSON_BUTTON, WoodType.CRIMSON));
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.DARK_OAK_BUTTON, WoodType.DARK_OAK));
        BlockRegistry.getInstance().register(new BlockTypeDirt());
        BlockRegistry.getInstance().register(new BlockTypeFurnace());
        BlockRegistry.getInstance().register(new BlockTypeFlowingWater());
        BlockRegistry.getInstance().register(new BlockTypeGrass());
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.JUNGLE_BUTTON, WoodType.JUNGLE));
        BlockRegistry.getInstance().register(new BlockTypeLitBlastFurnace());
        BlockRegistry.getInstance().register(new BlockTypeLitFurnace());
        BlockRegistry.getInstance().register(new BlockTypeMobSpawner());
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.OAK_BUTTON, WoodType.OAK));
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.SPRUCE_BUTTON, WoodType.SPRUCE));
        BlockRegistry.getInstance().register(new BlockTypeStone());
        BlockRegistry.getInstance().register(new BlockTypeSoulCampfire());
        BlockRegistry.getInstance().register(new BlockTypeWoodenButton(BlockTypeID.WARPED_BUTTON, WoodType.WARPED));
        BlockRegistry.getInstance().register(new BlockTypeWater());
        BlockRegistry.getInstance().register(new BlockTypeIronOre());
    }

    private static void loadBlockEntities() {
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeBed());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeBell());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeBlastFurnace());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeCampfire());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeFurnace());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeCauldron());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeChest());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeFurnace());
        ImplServer.getInstance().getBlockEntityRegistry().register(new BlockEntityTypeMobSpawner());
    }

    private static void loadEntityComponents() {
        EntityRegistry.getInstance().registerComponent(EntityScaleComponent.class,
                new EntityScaleComponent(1),
                new EntityScaleComponentHandler());
        EntityRegistry.getInstance().registerComponent(EntityDimensionsComponent.class,
                new EntityDimensionsComponent(0f, 0f),
                new EntityDimensionsComponentHandler());
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
                new EntityBossComponent(),
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
        EntityRegistry.getInstance().registerDefinition(new BoatEntityDefinition());
        EntityRegistry.getInstance().registerDefinition(new CowEntityDefinition());
        EntityRegistry.getInstance().registerDefinition(new HumanEntityDefinition());
        EntityRegistry.getInstance().registerDefinition(new ItemEntityDefinition());
    }

}

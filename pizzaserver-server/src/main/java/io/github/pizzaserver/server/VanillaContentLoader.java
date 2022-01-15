package io.github.pizzaserver.server;

import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.behavior.impl.CreativeModePlacementOnlyBlockBehavior;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.impl.*;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.components.handlers.*;
import io.github.pizzaserver.api.entity.definition.components.impl.*;
import io.github.pizzaserver.api.entity.definition.impl.EntityBoatDefinition;
import io.github.pizzaserver.api.entity.definition.impl.EntityCowDefinition;
import io.github.pizzaserver.api.entity.definition.impl.EntityHumanDefinition;
import io.github.pizzaserver.api.entity.definition.impl.EntityItemDefinition;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.impl.*;
import io.github.pizzaserver.server.block.behavior.impl.ButtonBlockBehavior;
import io.github.pizzaserver.server.block.behavior.impl.HorziontalDirectionBlockBehavior;
import io.github.pizzaserver.server.blockentity.types.impl.*;
import io.github.pizzaserver.server.item.behavior.impl.ItemArmorBehavior;
import io.github.pizzaserver.server.item.behavior.impl.ItemBoatBehavior;

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
        ItemRegistry.getInstance().register(new ItemAcaciaBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemBirchBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemBoat(), new ItemBoatBehavior());   // Why does Microsoft have minecraft:boat and minecraft:oak_boat?...
        ItemRegistry.getInstance().register(new ItemDarkOakBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemDiamondHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemJungleBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemOakBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemSpruceBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemShears());
        ItemRegistry.getInstance().register(new ItemStonePickaxe());
        ItemRegistry.getInstance().register(new ItemWoodenPickaxe());
        ItemRegistry.getInstance().register(new ItemWoodenSword());
    }

    private static void loadBlocks() {
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.ACACIA), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockAir());
        BlockRegistry.getInstance().register(new BlockAllow(), new CreativeModePlacementOnlyBlockBehavior());
        BlockRegistry.getInstance().register(new BlockAmethyst());
        BlockRegistry.getInstance().register(new BlockAncientDebris());
        BlockRegistry.getInstance().register(new BlockAzalea(), new RequiresSolidBottomBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBarrier());
        BlockRegistry.getInstance().register(new BlockBed());
        BlockRegistry.getInstance().register(new BlockBedrock());
        BlockRegistry.getInstance().register(new BlockBell());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.BIRCH), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBlackstone());
        BlockRegistry.getInstance().register(new BlockBlastFurnace(), new HorziontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBookshelf());
        BlockRegistry.getInstance().register(new BlockBrick());
        BlockRegistry.getInstance().register(new BlockBuddingAmethyst());
        BlockRegistry.getInstance().register(new BlockCalcite());
        BlockRegistry.getInstance().register(new BlockCampfire());
        BlockRegistry.getInstance().register(new BlockCauldron());
        BlockRegistry.getInstance().register(new BlockChest(), new HorziontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockChiseledDeepslate());
        BlockRegistry.getInstance().register(new BlockChiseledNetherBrick());
        BlockRegistry.getInstance().register(new BlockChiseledPolishedBlackstone());
        BlockRegistry.getInstance().register(new BlockClay());
        BlockRegistry.getInstance().register(new BlockCoal());
        BlockRegistry.getInstance().register(new BlockCoalOre());
        BlockRegistry.getInstance().register(new BlockCobbledDeepslate());
        BlockRegistry.getInstance().register(new BlockCobblestone());
        BlockRegistry.getInstance().register(new BlockColoredTorchBP());
        BlockRegistry.getInstance().register(new BlockColoredTorchRG());
        BlockRegistry.getInstance().register(new BlockCopperOre());
        BlockRegistry.getInstance().register(new BlockCrackedDeepslateBrick());
        BlockRegistry.getInstance().register(new BlockCrackedDeepslateTile());
        BlockRegistry.getInstance().register(new BlockCrackedNetherBrick());
        BlockRegistry.getInstance().register(new BlockCrackedPolishedBlackstoneBrick());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.CRIMSON), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCrimsonNylium());
        BlockRegistry.getInstance().register(new BlockCryingObsidian());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.DARK_OAK), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockDirt());
        BlockRegistry.getInstance().register(new BlockFurnace(), new HorziontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockFlowingWater());
        BlockRegistry.getInstance().register(new BlockGrass());
        BlockRegistry.getInstance().register(new BlockGrassPath());
        BlockRegistry.getInstance().register(new BlockGravel());
        BlockRegistry.getInstance().register(new BlockInvisibleBedrock());
        BlockRegistry.getInstance().register(new BlockIronOre());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.JUNGLE), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockLitBlastFurnace(), new HorziontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockLitFurnace(), new HorziontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockMobSpawner());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.OAK), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPodzol());
        BlockRegistry.getInstance().register(new BlockSand());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.SPRUCE), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStone());
        BlockRegistry.getInstance().register(new BlockSoulCampfire());
        BlockRegistry.getInstance().register(new BlockTallGrass());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.WARPED), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWater());
        BlockRegistry.getInstance().register(new BlockWool());
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
        EntityRegistry.getInstance().registerDefinition(new EntityBoatDefinition());
        EntityRegistry.getInstance().registerDefinition(new EntityCowDefinition());
        EntityRegistry.getInstance().registerDefinition(new EntityHumanDefinition());
        EntityRegistry.getInstance().registerDefinition(new EntityItemDefinition());
    }

}

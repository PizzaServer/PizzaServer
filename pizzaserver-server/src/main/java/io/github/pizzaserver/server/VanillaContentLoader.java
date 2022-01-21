package io.github.pizzaserver.server;

import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.behavior.impl.CreativeModePlacementOnlyBlockBehavior;
import io.github.pizzaserver.api.block.behavior.impl.RequiresSolidBottomBlockBehavior;
import io.github.pizzaserver.api.block.data.*;
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
import io.github.pizzaserver.api.utils.DyeColor;
import io.github.pizzaserver.server.block.behavior.impl.*;
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
        ItemRegistry.getInstance().register(new ItemAmethystShard());
        ItemRegistry.getInstance().register(new ItemArrow());
        ItemRegistry.getInstance().register(new ItemBirchBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemBlazePowder());
        ItemRegistry.getInstance().register(new ItemBlazeRod());
        ItemRegistry.getInstance().register(new ItemBoat(), new ItemBoatBehavior());   // Why does Microsoft have minecraft:boat and minecraft:oak_boat?...
        ItemRegistry.getInstance().register(new ItemBone());
        ItemRegistry.getInstance().register(new ItemBook());
        ItemRegistry.getInstance().register(new ItemBowl());
        ItemRegistry.getInstance().register(new ItemBrick());
        ItemRegistry.getInstance().register(new ItemChainmailBoots(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemChainmailChestplate(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemChainmailHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemChainmailLeggings(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemCharcoal());
        ItemRegistry.getInstance().register(new ItemClayBall());
        ItemRegistry.getInstance().register(new ItemClock());
        ItemRegistry.getInstance().register(new ItemCoal());
        ItemRegistry.getInstance().register(new ItemCopperIngot());
        ItemRegistry.getInstance().register(new ItemDarkOakBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemDiamond());
        ItemRegistry.getInstance().register(new ItemDiamondAxe());
        ItemRegistry.getInstance().register(new ItemDiamondBoots(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemDiamondChestplate(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemDiamondHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemDiamondLeggings(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemDiamondPickaxe());
        ItemRegistry.getInstance().register(new ItemDiamondSword());
        ItemRegistry.getInstance().register(new ItemDragonBreath());
        ItemRegistry.getInstance().register(new ItemEmerald());
        ItemRegistry.getInstance().register(new ItemFeather());
        ItemRegistry.getInstance().register(new ItemFermentedSpiderEye());
        ItemRegistry.getInstance().register(new ItemFlint());
        ItemRegistry.getInstance().register(new ItemGhastTear());
        ItemRegistry.getInstance().register(new ItemGlisteringMelonSlice());
        ItemRegistry.getInstance().register(new ItemGlowInkSac());
        ItemRegistry.getInstance().register(new ItemGlowstoneDust());
        ItemRegistry.getInstance().register(new ItemGoldIngot());
        ItemRegistry.getInstance().register(new ItemGoldNugget());
        ItemRegistry.getInstance().register(new ItemGoldenBoots(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemGoldenChestplate(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemGoldenHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemGoldenLeggings(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemGoldenPickaxe());
        ItemRegistry.getInstance().register(new ItemGoldenSword());
        ItemRegistry.getInstance().register(new ItemGunpowder());
        ItemRegistry.getInstance().register(new ItemHeartOfTheSea());
        ItemRegistry.getInstance().register(new ItemInkSac());
        ItemRegistry.getInstance().register(new ItemIronBoots(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemIronChestplate(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemIronHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemIronIngot());
        ItemRegistry.getInstance().register(new ItemIronLeggings(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemIronNugget());
        ItemRegistry.getInstance().register(new ItemIronPickaxe());
        ItemRegistry.getInstance().register(new ItemIronSword());
        ItemRegistry.getInstance().register(new ItemJungleBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemLapisLazuli());
        ItemRegistry.getInstance().register(new ItemLeather());
        ItemRegistry.getInstance().register(new ItemLeatherBoots(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemLeatherChestplate(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemLeatherHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemLeatherLeggings(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemMagmaCream());
        ItemRegistry.getInstance().register(new ItemNautilusShell());
        ItemRegistry.getInstance().register(new ItemNetherBrick());
        ItemRegistry.getInstance().register(new ItemNetherStar());
        ItemRegistry.getInstance().register(new ItemNetheriteBoots(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemNetheriteChestplate(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemNetheriteHelmet(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemNetheriteIngot());
        ItemRegistry.getInstance().register(new ItemNetheriteLeggings(), new ItemArmorBehavior());
        ItemRegistry.getInstance().register(new ItemNetheritePickaxe());
        ItemRegistry.getInstance().register(new ItemNetheriteScrap());
        ItemRegistry.getInstance().register(new ItemNetheriteSword());
        ItemRegistry.getInstance().register(new ItemOakBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemPaper());
        ItemRegistry.getInstance().register(new ItemPrismarineCrystals());
        ItemRegistry.getInstance().register(new ItemPrismarineShard());
        ItemRegistry.getInstance().register(new ItemQuartz());
        ItemRegistry.getInstance().register(new ItemRabbitFoot());
        ItemRegistry.getInstance().register(new ItemRabbitHide());
        ItemRegistry.getInstance().register(new ItemRawCopper());
        ItemRegistry.getInstance().register(new ItemRawGold());
        ItemRegistry.getInstance().register(new ItemRawIron());
        ItemRegistry.getInstance().register(new ItemShears());
        ItemRegistry.getInstance().register(new ItemShulkerShell());
        ItemRegistry.getInstance().register(new ItemSlimeball());
        ItemRegistry.getInstance().register(new ItemSpruceBoat(), new ItemBoatBehavior());
        ItemRegistry.getInstance().register(new ItemStick());
        ItemRegistry.getInstance().register(new ItemStonePickaxe());
        ItemRegistry.getInstance().register(new ItemStoneSword());
        ItemRegistry.getInstance().register(new ItemSugar());
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
        BlockRegistry.getInstance().register(new BlockAzaleaLeaves());
        BlockRegistry.getInstance().register(new BlockAzaleaLeavesFlowered());
        BlockRegistry.getInstance().register(new BlockBarrier());
        BlockRegistry.getInstance().register(new BlockBed());
        BlockRegistry.getInstance().register(new BlockBedrock());
        BlockRegistry.getInstance().register(new BlockBell());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.BIRCH), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.BLACK), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBlackstone());
        BlockRegistry.getInstance().register(new BlockBlackstoneSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBlackstoneSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBlastFurnace(), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBlueIce(), new IceBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.BLUE), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBookshelf());
        BlockRegistry.getInstance().register(new BlockBrick());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.BROWN), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockBuddingAmethyst());
        BlockRegistry.getInstance().register(new BlockCalcite());
        BlockRegistry.getInstance().register(new BlockCampfire());
        BlockRegistry.getInstance().register(new BlockCauldron());
        BlockRegistry.getInstance().register(new BlockChest(), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockChiseledDeepslate());
        BlockRegistry.getInstance().register(new BlockChiseledNetherBrick());
        BlockRegistry.getInstance().register(new BlockChiseledPolishedBlackstone());
        BlockRegistry.getInstance().register(new BlockClay());
        BlockRegistry.getInstance().register(new BlockCoal());
        BlockRegistry.getInstance().register(new BlockCoalOre());
        BlockRegistry.getInstance().register(new BlockCobbledDeepslate());
        BlockRegistry.getInstance().register(new BlockCobbledDeepslateSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCobbledDeepslateSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCobblestone());
        BlockRegistry.getInstance().register(new BlockColoredTorchBP());
        BlockRegistry.getInstance().register(new BlockColoredTorchRG());
        BlockRegistry.getInstance().register(new BlockConcrete());
        BlockRegistry.getInstance().register(new BlockConcretePowder());
        BlockRegistry.getInstance().register(new BlockCopperOre());
        BlockRegistry.getInstance().register(new BlockCrackedDeepslateBrick());
        BlockRegistry.getInstance().register(new BlockCrackedDeepslateTile());
        BlockRegistry.getInstance().register(new BlockCrackedNetherBrick());
        BlockRegistry.getInstance().register(new BlockCrackedPolishedBlackstoneBrick());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.CRIMSON), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCrimsonNylium());
        BlockRegistry.getInstance().register(new BlockWoodenSlab(WoodType.CRIMSON), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWoodenSlab(WoodType.CRIMSON, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCryingObsidian());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.CYAN), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.DARK_OAK), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockDeadBush());
        BlockRegistry.getInstance().register(new BlockDeepslateBrick());
        BlockRegistry.getInstance().register(new BlockDeepslateBrickSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockDeepslateBrickSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockDeepslateCoalOre());
        BlockRegistry.getInstance().register(new BlockDeepslateCopperOre());
        BlockRegistry.getInstance().register(new BlockDeepslateDiamondOre());
        BlockRegistry.getInstance().register(new BlockDeepslateEmeraldOre());
        BlockRegistry.getInstance().register(new BlockDeepslateGoldOre());
        BlockRegistry.getInstance().register(new BlockDeepslateIronOre());
        BlockRegistry.getInstance().register(new BlockDeepslateLapisOre());
        BlockRegistry.getInstance().register(new BlockDeepslateRedstoneOre(), new RedstoneOreBehavior());
        BlockRegistry.getInstance().register(new BlockDeepslateTileSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockDeepslateTileSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockDeny());
        BlockRegistry.getInstance().register(new BlockDiamondBlock());
        BlockRegistry.getInstance().register(new BlockDiamondOre());
        BlockRegistry.getInstance().register(new BlockDirt());
        BlockRegistry.getInstance().register(new BlockStoneSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(StoneSlabType.RED_SANDSTONE, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(StoneSlabType.END_STONE_BRICK, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(StoneSlabType.MOSSY_STONE_BRICK, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockEmeraldBlock());
        BlockRegistry.getInstance().register(new BlockEmeraldOre());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(CopperType.EXPOSED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(CopperType.EXPOSED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockFurnace(), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockFlowingWater());
        BlockRegistry.getInstance().register(new BlockGoldOre());
        BlockRegistry.getInstance().register(new BlockGrass());
        BlockRegistry.getInstance().register(new BlockGrassPath());
        BlockRegistry.getInstance().register(new BlockGravel());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.GRAY), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.GREEN), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockIce(), new IceBlockBehavior());
        BlockRegistry.getInstance().register(new BlockInvisibleBedrock());
        BlockRegistry.getInstance().register(new BlockIronBlock());
        BlockRegistry.getInstance().register(new BlockIronOre());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.JUNGLE), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockLapisBlock());
        BlockRegistry.getInstance().register(new BlockLapisOre());
        BlockRegistry.getInstance().register(new BlockLight());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.LIGHT_BLUE), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.LIME), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockLitBlastFurnace(), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockLitDeepslateRedstoneOre(), new RedstoneOreBehavior());
        BlockRegistry.getInstance().register(new BlockFurnace(FurnaceType.LIT), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockLitRedstoneOre(), new RedstoneOreBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.MAGENTA), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockMobSpawner());
        BlockRegistry.getInstance().register(new BlockNetherrack());
        BlockRegistry.getInstance().register(new BlockNetherReactor());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.OAK), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.ORANGE), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(CopperType.OXIDIZED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(CopperType.OXIDIZED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.PINK), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPodzol());
        BlockRegistry.getInstance().register(new BlockPolishedBlackstoneButton(), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPolishedBlackstoneSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPolishedBlackstoneSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPolishedBlackstoneBrickSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPolishedBlackstoneBrickSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPolishedDeepslateSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockPolishedDeepslateSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.PURPLE), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockRedstoneWire());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.RED), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockRedstoneOre(), new RedstoneOreBehavior());
        BlockRegistry.getInstance().register(new BlockReserved6());
        BlockRegistry.getInstance().register(new BlockSand());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.LIGHT_GRAY), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockSnow());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.SPRUCE), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStone());
        BlockRegistry.getInstance().register(new BlockStoneButton(), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(StoneSlabType.RED_SANDSTONE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(StoneSlabType.END_STONE_BRICK), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockStoneSlab(StoneSlabType.MOSSY_STONE_BRICK), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockSoulCampfire());
        BlockRegistry.getInstance().register(new BlockTallGrass());
        BlockRegistry.getInstance().register(new BlockWoodenButton(WoodType.WARPED), new ButtonBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWoodenSlab(WoodType.WARPED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWoodenSlab(WoodType.WARPED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWater());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(CopperType.EXPOSED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(CopperType.EXPOSED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(CopperType.OXIDIZED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(CopperType.OXIDIZED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(CopperType.WEATHERED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWaxedCutCopperSlab(CopperType.WEATHERED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(CopperType.WEATHERED), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockCutCopperSlab(CopperType.WEATHERED, SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.WHITE), new HorizontalDirectionBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWool());
        BlockRegistry.getInstance().register(new BlockWoodenSlab(), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockWoodenSlab(SlabType.DOUBLE), new SlabBlockBehavior());
        BlockRegistry.getInstance().register(new BlockGlazedTerracotta(DyeColor.YELLOW), new HorizontalDirectionBlockBehavior());
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

package io.github.pizzaserver.server.blockentity.handler;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.type.*;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.impl.*;

import java.util.*;

/**
 * Responsible for deserializing/serializing block entity disk data.
 */
public class BlockEntityHandler {

    private static final Map<String, BlockEntityParser<? extends BlockEntity<? extends Block>>> parsers = new HashMap<>();
    private static final Map<String, String> blockIdsToParsers = new HashMap<>();


    private BlockEntityHandler() {}

    public static BlockEntity<? extends Block> fromDiskNBT(World world, NbtMap nbt) {
        String id = nbt.getString("id");
        if (!parsers.containsKey(id)) {
            return null;
        }

        return parsers.get(id).fromDiskNBT(world, nbt);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static NbtMap toDiskNBT(BlockEntity<? extends Block> blockEntity) {
        if (!parsers.containsKey(blockEntity.getId())) {
            return null;
        }

        BlockEntityParser parser = parsers.get(blockEntity.getId());
        return parser.toDiskNBT(blockEntity);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    public static NbtMap toNetworkNBT(BlockEntity<? extends Block> blockEntity) {
        String blockEntityId = blockEntity.getId();
        if (!parsers.containsKey(blockEntityId)) {
            return null;
        }

        BlockEntityParser parser = parsers.get(blockEntityId);
        return parser.toNetworkNBT(parser.toDiskNBT(blockEntity));
    }

    @SuppressWarnings("rawtypes")
    public static NbtMap toNetworkNBT(NbtMap diskNBT) {
        String blockEntityId = diskNBT.getString("id");
        if (!parsers.containsKey(blockEntityId)) {
            return null;
        }

        BlockEntityParser parser = parsers.get(blockEntityId);

        return parser.toNetworkNBT(diskNBT);
    }

    public static BlockEntity<? extends Block> create(String blockEntityId, BlockLocation location) {
        if (!parsers.containsKey(blockEntityId)) {
            return null;
        }

        return parsers.get(blockEntityId).create(location);
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static <T extends Block> Optional<BlockEntity<T>> create(T block) {
        String blockId = block.getBlockId();
        if (!blockIdsToParsers.containsKey(blockId)) {
            return Optional.empty();
        }

        BlockEntityParser parser = parsers.get(blockIdsToParsers.get(blockId));
        BlockEntity blockEntity = parser.create(block.getLocation());

        return Optional.of(blockEntity);
    }

    private static void register(Set<String> blockIds, String blockEntityId, BlockEntityParser<? extends BlockEntity<? extends Block>> parser) {
        parsers.put(blockEntityId, parser);
        for (String blockId : blockIds) {
            blockIdsToParsers.put(blockId, blockEntityId);
        }
    }

    static {
        register(Collections.singleton(BlockID.BARREL), BlockEntityBarrel.ID, new BlockEntityBarrelParser());
        // register(Collections.singleton(BlockID.BELL), BlockEntityBell.ID, new BlockEntityBellParser());
        register(Collections.singleton(BlockID.BED), BlockEntityBed.ID, new BlockEntityBedParser());
        register(Set.of(BlockID.BLAST_FURNACE, BlockID.LIT_BLAST_FURNACE), BlockEntityBlastFurnace.ID, new BlockEntityBlastFurnaceParser());
        register(Set.of(BlockID.CAMPFIRE, BlockID.SOUL_CAMPFIRE), BlockEntityCampfire.ID, new BlockEntityCampfireParser());
        register(Collections.singleton(BlockID.CAULDRON), BlockEntityCauldron.ID, new BlockEntityCauldronParser());
        register(Collections.singleton(BlockID.CHEST), BlockEntityChest.ID, new BlockEntityChestParser());
        register(Collections.singleton(BlockID.DISPENSER), BlockEntityDispenser.ID, new BlockEntityDispenserParser());
        register(Collections.singleton(BlockID.DROPPER), BlockEntityDropper.ID, new BlockEntityDropperParser());
        register(Collections.singleton(BlockID.HOPPER), BlockEntityHopper.ID, new BlockEntityHopperParser());
        register(Set.of(BlockID.FURNACE, BlockID.LIT_FURNACE), BlockEntityFurnace.ID, new BlockEntityFurnaceParser());
        register(Collections.singleton(BlockID.MOB_SPAWNER), BlockEntityMobSpawner.ID, new BlockEntityMobSpawnerParser());
    }

}

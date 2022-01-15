package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityChest;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockEntityTypeChest implements BlockEntityType {

    @Override
    public String getId() {
        return BlockEntityChest.ID;
    }

    @Override
    public Set<Block> getBlocks() {
        return Collections.singleton(BlockRegistry.getInstance().getBlock(BlockID.CHEST));
    }

    @Override
    public BlockEntityChest create(Block block) {
        return new BlockEntityChest(block.getLocation());
    }

    @Override
    public BlockEntityChest deserializeDisk(World world, NbtMap diskNBT) {
        BlockEntityChest chestEntity = new BlockEntityChest(new BlockLocation(world,
                Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"))));

        List<NbtMap> itemNBTs = diskNBT.getList("Items", NbtType.COMPOUND);
        for (NbtMap itemNBT : itemNBTs) {
            int slot = itemNBT.getByte("slot");
            chestEntity.getInventory().setSlot(slot, ItemUtils.deserializeDiskNBTItem(itemNBT));
        }

        return chestEntity;
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity blockEntity) {
        BlockEntityChest blockEntityChest = (BlockEntityChest) blockEntity;

        List<NbtMap> itemNBTs = new ArrayList<>();
        for (Item item : blockEntityChest.getInventory().getSlots()) {
            if (!item.isEmpty()) {
                itemNBTs.add(ItemUtils.serializeWithSlotForDisk(item));
            }
        }

        return NbtMap.builder()
                .putString("id", this.getId())
                .putList("Items", NbtType.COMPOUND, itemNBTs)
                .putByte("isMovable", (byte) 1) // TODO: retrieve from block entity
                .putInt("x", blockEntity.getLocation().getX())
                .putInt("y", blockEntity.getLocation().getY())
                .putInt("z", blockEntity.getLocation().getZ())
                .build();
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putByte("isMovable", diskNBT.getByte("isMovable"))
                .putInt("x", diskNBT.getInt("x"))
                .putInt("y", diskNBT.getInt("y"))
                .putInt("z", diskNBT.getInt("z"))
                .build();
    }

}

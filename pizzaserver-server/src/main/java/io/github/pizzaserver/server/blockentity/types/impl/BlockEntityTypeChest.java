package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityCampfire;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityChest;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockEntityTypeChest implements BlockEntityType<BlockChest> {

    @Override
    public String getId() {
        return BlockEntityChest.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.CHEST);
    }

    @Override
    public BlockEntityChest create(BlockChest block) {
        return new BlockEntityChest(block);
    }

    @Override
    public BlockEntityChest deserializeDisk(Chunk chunk, NbtMap diskNBT) {
        Vector3i coordinates = Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"));
        BlockEntityChest chestEntity = new BlockEntityChest((BlockChest) chunk.getBlock(coordinates));

        List<NbtMap> itemNBTs = diskNBT.getList("Items", NbtType.COMPOUND);
        for (NbtMap itemNBT : itemNBTs) {
            int slot = itemNBT.getByte("slot");
            chestEntity.getInventory().setSlot(slot, ItemUtils.deserializeDiskNBTItem(itemNBT));
        }

        return chestEntity;
    }

    @Override
    public NbtMap serializeForDisk(BlockEntity<BlockChest> blockEntity) {
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
                .putInt("x", blockEntity.getBlock().getX())
                .putInt("y", blockEntity.getBlock().getY())
                .putInt("z", blockEntity.getBlock().getZ())
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

package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityChest;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BlockType;
import io.github.pizzaserver.api.block.types.BlockTypeID;
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
    public Set<BlockType> getBlockTypes() {
        return Collections.singleton(BlockRegistry.getInstance().getBlockType(BlockTypeID.CHEST));
    }

    @Override
    public BlockEntityChest create(Block block) {
        return new BlockEntityChest(block.getLocation().toVector3i());
    }

    @Override
    public BlockEntityChest deserialize(NbtMap diskNBT) {
        BlockEntityChest chestEntity = new BlockEntityChest(Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z")));

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
        for (ItemStack itemStack : blockEntityChest.getInventory().getSlots()) {
            if (!itemStack.isEmpty()) {
                itemNBTs.add(ItemUtils.serializeWithSlotForDisk(itemStack));
            }
        }

        return NbtMap.builder()
                .putString("id", this.getId())
                .putList("Items", NbtType.COMPOUND, itemNBTs)
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .build();
    }

    @Override
    public NbtMap serializeForNetwork(BlockEntity blockEntity) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putByte("isMovable", (byte) 1)
                .putInt("x", blockEntity.getPosition().getX())
                .putInt("y", blockEntity.getPosition().getY())
                .putInt("z", blockEntity.getPosition().getZ())
                .build();
    }

}

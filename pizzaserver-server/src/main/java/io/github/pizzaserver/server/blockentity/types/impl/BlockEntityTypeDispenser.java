package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockDispenser;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityDispenser;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockEntityTypeDispenser implements BlockEntityType<BlockDispenser, BlockEntityDispenser> {

    @Override
    public String getId() {
        return BlockEntityDispenser.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.DISPENSER);
    }

    @Override
    public BlockEntityDispenser create(BlockDispenser block) {
        return new BlockEntityDispenser(block.getLocation());
    }

    @Override
    public BlockEntityDispenser deserializeDisk(World world, NbtMap diskNBT) {
        BlockEntityDispenser dispenserEntity = new BlockEntityDispenser(new BlockLocation(world,
                Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"))));

        List<NbtMap> itemNBTs = diskNBT.getList("Items", NbtType.COMPOUND);
        for (NbtMap itemNBT : itemNBTs) {
            int slot = itemNBT.getByte("slot");
            dispenserEntity.getInventory().setSlot(slot, ItemUtils.deserializeDiskNBTItem(itemNBT));
        }

        return dispenserEntity;
    }

    @Override
    public NbtMap serializeForDisk(BlockEntityDispenser blockEntity) {
        List<NbtMap> itemNBTs = new ArrayList<>();
        for (Item item : blockEntity.getInventory().getSlots()) {
            if (!item.isEmpty()) {
                itemNBTs.add(ItemUtils.serializeWithSlotForDisk(item));
            }
        }

        return NbtMap.builder()
                .putString("id", this.getId())
                .putList("Items", NbtType.COMPOUND, itemNBTs)
                .putInt("x", blockEntity.getLocation().getX())
                .putInt("y", blockEntity.getLocation().getY())
                .putInt("z", blockEntity.getLocation().getZ())
                .build();
    }

    @Override
    public NbtMap serializeForNetwork(NbtMap diskNBT) {
        return NbtMap.builder()
                .putString("id", this.getId())
                .putInt("x", diskNBT.getInt("x"))
                .putInt("y", diskNBT.getInt("y"))
                .putInt("z", diskNBT.getInt("z"))
                .build();
    }

}

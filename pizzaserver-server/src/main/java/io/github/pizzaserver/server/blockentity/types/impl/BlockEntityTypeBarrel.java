package io.github.pizzaserver.server.blockentity.types.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBarrel;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityBarrel;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class BlockEntityTypeBarrel implements BlockEntityType<BlockBarrel, BlockEntityBarrel> {

    @Override
    public String getId() {
        return BlockEntityBarrel.ID;
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.BARREL);
    }

    @Override
    public BlockEntityBarrel create(BlockBarrel block) {
        return new BlockEntityBarrel(block.getLocation());
    }

    @Override
    public BlockEntityBarrel deserializeDisk(World world, NbtMap diskNBT) {
        BlockEntityBarrel barrel = new BlockEntityBarrel(new BlockLocation(world, Vector3i.from(diskNBT.getInt("x"), diskNBT.getInt("y"), diskNBT.getInt("z"))));

        List<NbtMap> itemNBTs = diskNBT.getList("Items", NbtType.COMPOUND);
        for (NbtMap itemNBT : itemNBTs) {
            int slot = itemNBT.getByte("slot");
            barrel.getInventory().setSlot(slot, ItemUtils.deserializeDiskNBTItem(itemNBT));
        }

        return barrel;
    }

    @Override
    public NbtMap serializeForDisk(BlockEntityBarrel blockEntity) {
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

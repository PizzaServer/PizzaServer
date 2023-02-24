package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCampfire;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityCampfire;
import io.github.pizzaserver.server.item.ItemUtils;

public class BlockEntityCampfireParser extends BaseBlockEntityParser<BlockEntityCampfire> {

    @Override
    public BlockEntityCampfire create(BlockLocation location) {
        return new ImplBlockEntityCampfire(location);
    }

    @Override
    public BlockEntityCampfire fromDiskNBT(World world, NbtMap nbt) {
        BlockEntityCampfire campfire = this.create(new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"))));

        for (int slot = 0; slot < 4; slot++) {
            String itemProperty = "Item" + (slot + 1);
            String timeProperty = "CookTime" + (slot + 1);

            if (nbt.containsKey(itemProperty)) {
                campfire.setItem(slot, ItemUtils.deserializeDiskNBTItem(nbt.getCompound(itemProperty)));
            }
            campfire.setCookTickProgressForSlot(slot, nbt.getInt(timeProperty, 0));
        }
        
        return campfire;
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityCampfire blockEntity) {
        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putCompound("Item1", ItemUtils.serializeForDisk(blockEntity.getItem(0)))
                .putCompound("Item2", ItemUtils.serializeForDisk(blockEntity.getItem(1)))
                .putCompound("Item3", ItemUtils.serializeForDisk(blockEntity.getItem(2)))
                .putCompound("Item4", ItemUtils.serializeForDisk(blockEntity.getItem(3)))
                .putInt("CookTime1", blockEntity.getCookTickProgressForSlot(0))
                .putInt("CookTime2", blockEntity.getCookTickProgressForSlot(1))
                .putInt("CookTime3", blockEntity.getCookTickProgressForSlot(2))
                .putInt("CookTime4", blockEntity.getCookTickProgressForSlot(3))
                .build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        return super.toNetworkNBT(diskNBT)
                .toBuilder()
                .putCompound("Item1", diskNBT.getCompound("Item1"))
                .putCompound("Item2", diskNBT.getCompound("Item2"))
                .putCompound("Item3", diskNBT.getCompound("Item3"))
                .putCompound("Item4", diskNBT.getCompound("Item4"))
                .build();
    }

}

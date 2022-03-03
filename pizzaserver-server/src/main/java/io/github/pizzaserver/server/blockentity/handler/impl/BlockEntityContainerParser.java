package io.github.pizzaserver.server.blockentity.handler.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import com.nukkitx.nbt.NbtType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockEntityContainerParser<T extends Block> extends BaseBlockEntityParser<BlockEntityContainer<T>> {

    @Override
    public BlockEntityContainer<T> fromDiskNBT(World world, NbtMap nbt) {
        BlockEntityContainer<T> containerEntity = this.create(new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"))));

        List<NbtMap> itemNBTs = nbt.getList("Items", NbtType.COMPOUND);
        for (NbtMap itemNBT : itemNBTs) {
            int slot = itemNBT.getByte("slot");
            containerEntity.getInventory().setSlot(slot, ItemUtils.deserializeDiskNBTItem(itemNBT));
        }

        return containerEntity;
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityContainer<T> blockEntity) {
        List<NbtMap> itemNBTs = new ArrayList<>();
        for (Item item : blockEntity.getInventory().getSlots()) {
            if (!item.isEmpty()) {
                itemNBTs.add(ItemUtils.serializeWithSlotForDisk(item));
            }
        }

        return super.toDiskNBT(blockEntity)
                .toBuilder()
                .putList("Items", NbtType.COMPOUND, itemNBTs)
                .build();
    }

}

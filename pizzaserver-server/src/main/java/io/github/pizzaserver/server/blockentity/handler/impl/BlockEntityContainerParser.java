package io.github.pizzaserver.server.blockentity.handler.impl;

import org.cloudburstmc.math.vector.Vector3i;
import org.cloudburstmc.nbt.NbtMap;
import org.cloudburstmc.nbt.NbtMapBuilder;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.handler.BaseBlockEntityParser;
import io.github.pizzaserver.server.item.ItemUtils;
import org.cloudburstmc.nbt.NbtType;

import java.util.ArrayList;
import java.util.List;

public abstract class BlockEntityContainerParser<T extends Block> extends BaseBlockEntityParser<BlockEntityContainer<T>> {

    @Override
    public BlockEntityContainer<T> fromDiskNBT(World world, NbtMap nbt) {
        BlockEntityContainer<T> containerEntity = this.create(new BlockLocation(world,
                Vector3i.from(nbt.getInt("x"), nbt.getInt("y"), nbt.getInt("z"))));

        List<NbtMap> itemNBTs = nbt.getList("Items", NbtType.COMPOUND);
        for (NbtMap itemNBT : itemNBTs) {
            int slot = itemNBT.getByte("Slot");
            containerEntity.getInventory().setSlot(slot, ItemUtils.deserializeDiskNBTItem(itemNBT));
        }

        containerEntity.setCustomName(nbt.getString("CustomName", null));

        return containerEntity;
    }

    @Override
    public NbtMap toDiskNBT(BlockEntityContainer<T> blockEntity) {
        List<NbtMap> itemNBTs = new ArrayList<>();
        for (int slot = 0; slot < blockEntity.getInventory().getSlots().length; slot++) {
            Item item = blockEntity.getInventory().getSlot(slot);

            if (!item.isEmpty()) {
                itemNBTs.add(ItemUtils.serializeWithSlotForDisk(item, slot));
            }
        }

        NbtMapBuilder diskNBTBuilder = super.toDiskNBT(blockEntity).toBuilder()
                .putList("Items", NbtType.COMPOUND, itemNBTs);

        blockEntity.getCustomName().ifPresent(name -> diskNBTBuilder.putString("CustomName", name));

        return diskNBTBuilder.build();
    }

    @Override
    public NbtMap toNetworkNBT(NbtMap diskNBT) {
        if (diskNBT.containsKey("CustomName")) {
            return super.toNetworkNBT(diskNBT).toBuilder()
                    .putString("CustomName", diskNBT.getString("CustomName"))
                    .build();
        } else {
            return super.toNetworkNBT(diskNBT);
        }
    }

}

package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.impl.BlockEntityTypeChest;
import io.github.pizzaserver.api.block.Block;

public class BlockEntityChest extends BaseBlockEntity {

    public BlockEntityChest(Block block) {
        super(block);
    }

    @Override
    public BlockEntityTypeChest getType() {
        return (BlockEntityTypeChest) BlockEntityRegistry.getInstance().getBlockEntityType(BlockEntityTypeChest.ID);
    }

    @Override
    public NbtMap getNetworkData() {
        return NbtMap.builder()
                .putString("id", this.getType().getId())
                .putByte("isMovable", (byte) 1)
                .putInt("x", this.getLocation().getX())
                .putInt("y", this.getLocation().getY())
                .putInt("z", this.getLocation().getZ())
                .build();
    }

    @Override
    public NbtMap getDiskData() {
        return NbtMap.EMPTY;
    }

}

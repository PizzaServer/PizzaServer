package io.github.pizzaserver.api.block.descriptors;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityContainer<T extends BlockEntity> extends Block {

    @SuppressWarnings("unchecked")
    default T getBlockEntity() {
        BlockEntity blockEntity = this.getWorld().getBlockEntity(this.getLocation().toVector3i()).orElse(null);
        if (blockEntity == null || !blockEntity.getType().getBlockIds().contains(this.getBlockId())) {
            return null;
        }
        return (T) blockEntity;
    }
}

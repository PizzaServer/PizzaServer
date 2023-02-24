package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public abstract class BlockBlockEntity<T extends BlockEntity<? extends Block>> extends BaseBlock {

    @SuppressWarnings("unchecked")
    public T getBlockEntity() {
        return (T) this.getWorld().getBlockEntity(this.getLocation().toVector3i()).orElse(null);
    }

}

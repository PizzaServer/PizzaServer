package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

<<<<<<< HEAD
public abstract class BlockBlockEntity<T extends BlockEntity<? extends Block>> extends BaseBlock {
=======
public abstract class BlockBlockEntity<T extends BlockEntity<? extends Block>> extends Block {
>>>>>>> 05e34e8a (remove block entity registry and abstract block entities more)

    @SuppressWarnings("unchecked")
    public T getBlockEntity() {
        return (T) this.getWorld().getBlockEntity(this.getLocation().toVector3i()).orElse(null);
    }

}

package io.github.pizzaserver.api.blockentity;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.utils.BlockLocation;

public interface BlockEntity<T extends Block> {

    String getId();

    BlockLocation getLocation();

    @SuppressWarnings("unchecked")
    default T getBlock() {
        return (T) this.getLocation().getBlock();
    }

}

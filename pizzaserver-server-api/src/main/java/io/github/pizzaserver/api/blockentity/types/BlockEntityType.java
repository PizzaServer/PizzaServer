package io.github.pizzaserver.api.blockentity.types;

import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.types.BlockType;

public interface BlockEntityType {

    String getId();

    /**
     * Retrieve the block type that this block entity is associated with.
     * @return block type
     */
    BlockType getBlockType();

    BlockEntity create(Block block);

}

package io.github.pizzaserver.api.blockentity;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;

import java.util.Optional;

public interface BlockEntityRegistry {

    BlockEntityType<? extends Block> getBlockEntityType(String blockEntityId);

    /**
     * Retrieves the block entity type equivalent of a block type if one exists.
     * @param block block to look for
     * @return block entity type if any exists
     */
    Optional<BlockEntityType<? extends Block>> getBlockEntityType(Block block);

    boolean hasBlockEntityType(String blockEntityId);

    static BlockEntityRegistry getInstance() {
        return Server.getInstance().getBlockEntityRegistry();
    }

}

package io.github.pizzaserver.api.block;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.types.BaseBlockType;

import java.util.*;

/**
 * Contains every single block retrievable via the API.
 */
public interface BlockRegistry {

    /**
     * Register a {@link BaseBlockType} to the server.
     * Custom blocks will need to register their {@link BaseBlockType} in order to be used and for the world to render correctly
     * @param blockType {@link BaseBlockType} that needs to be registered
     */
    void register(BaseBlockType blockType);

    /**
     * Retrieve a {@link BaseBlockType} by it's id (e.g. minecraft:air)
     * @param blockId The id of the block (e.g. minecraft:air)
     * @return {@link BaseBlockType}
     */
    BaseBlockType getBlockType(String blockId);

    /**
     * Check if a block id was registered.
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    boolean hasBlockType(String blockId);

    /**
     * Retrieve all non-Vanilla {@link BaseBlockType}s that are registered.
     * @return registered non-Vanilla {@link BaseBlockType}s
     */
    Set<BaseBlockType> getCustomTypes();

    Block getBlock(String blockId);

    Block getBlock(String blockId, int state);

    static BlockRegistry getInstance() {
        return Server.getInstance().getBlockRegistry();
    }

}

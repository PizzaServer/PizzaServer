package io.github.willqi.pizzaserver.api.world.blocks;

import io.github.willqi.pizzaserver.api.world.blocks.types.BlockType;

import java.util.Set;

/**
 * Contains every single block retrievable via the API
 */
public interface BlockRegistry {

    /**
     * Register a {@link BlockType} to the server
     * Custom blocks will need to register their {@link BlockType} in order to be used and for the world to render correctly
     * @param blockType {@link BlockType} that needs to be registered
     */
    void register(BlockType blockType);

    /**
     * Retrieve a {@link BlockType} by it's id (e.g. minecraft:air)
     * @param blockId The id of the block (e.g. minecraft:air)
     * @return {@link BlockType}
     */
    BlockType getBlockType(String blockId);

    /**
     * Check if a block id was registered
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    boolean hasBlockType(String blockId);

    /**
     * Retrieve all non-Vanilla {@link BlockType}s that are registered
     * @return registered non-Vanilla {@link BlockType}s
     */
    Set<BlockType> getCustomTypes();

}

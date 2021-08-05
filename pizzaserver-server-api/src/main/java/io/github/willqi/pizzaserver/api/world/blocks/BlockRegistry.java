package io.github.willqi.pizzaserver.api.world.blocks;

import io.github.willqi.pizzaserver.api.world.blocks.types.BaseBlockType;

import java.util.Set;

/**
 * Contains every single block retrievable via the API
 */
public interface BlockRegistry {

    /**
     * Register a {@link BaseBlockType} to the server
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
     * Check if a block id was registered
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    boolean hasBlockType(String blockId);

    /**
     * Retrieve all non-Vanilla {@link BaseBlockType}s that are registered
     * @return registered non-Vanilla {@link BaseBlockType}s
     */
    Set<BaseBlockType> getCustomTypes();

}

package io.github.willqi.pizzaserver.api.world.blocks;

import io.github.willqi.pizzaserver.api.world.blocks.types.APIBlockType;

import java.util.Set;

/**
 * Contains every single block retrievable via the API
 */
public interface APIBlockRegistry {

    /**
     * Register a {@link APIBlockType} to the server
     * Custom blocks will need to register their {@link APIBlockType} in order to be used and for the world to render correctly
     * @param blockType {@link APIBlockType} that needs to be registered
     */
    void register(APIBlockType blockType);

    /**
     * Retrieve a {@link APIBlockType} by it's id (e.g. minecraft:air)
     * @param blockId The id of the block (e.g. minecraft:air)
     * @return {@link APIBlockType}
     */
    APIBlockType getBlockType(String blockId);

    /**
     * Check if a block id was registered
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    boolean hasBlockType(String blockId);

    /**
     * Retrieve all non-Vanilla {@link APIBlockType}s that are registered
     * @return registered non-Vanilla {@link APIBlockType}s
     */
    Set<APIBlockType> getCustomTypes();

}

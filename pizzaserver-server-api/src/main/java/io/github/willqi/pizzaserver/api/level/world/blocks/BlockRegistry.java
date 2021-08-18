package io.github.willqi.pizzaserver.api.level.world.blocks;

import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;

import java.util.*;

/**
 * Contains every single block retrievable via the API
 */
public class BlockRegistry {

    // All registered block types
    private static final Map<String, BaseBlockType> types = new HashMap<>();

    // All registered CUSTOM block types
    private static final Set<BaseBlockType> customTypes = new HashSet<>();


    /**
     * Register a {@link BaseBlockType} to the server
     * Custom blocks will need to register their {@link BaseBlockType} in order to be used and for the world to render correctly
     * @param blockType {@link BaseBlockType} that needs to be registered
     */
    public static void register(BaseBlockType blockType) {
        if (types.containsKey(blockType.getBlockId())) {
            throw new IllegalArgumentException("Block id " + blockType.getBlockId() + " was already registered.");
        }

        if (!blockType.getBlockId().startsWith("minecraft:")) {
            customTypes.add(blockType);
        }
        types.put(blockType.getBlockId(), blockType);
    }

    /**
     * Retrieve a {@link BaseBlockType} by it's id (e.g. minecraft:air)
     * @param blockId The id of the block (e.g. minecraft:air)
     * @return {@link BaseBlockType}
     */
    public static BaseBlockType getBlockType(String blockId) {
        if (!types.containsKey(blockId)) {
            throw new NullPointerException("Could not find a block type by the id of " + blockId);
        }
        return types.get(blockId);
    }

    /**
     * Check if a block id was registered
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    public static boolean hasBlockType(String blockId) {
        return types.containsKey(blockId);
    }

    /**
     * Retrieve all non-Vanilla {@link BaseBlockType}s that are registered
     * @return registered non-Vanilla {@link BaseBlockType}s
     */
    public static Set<BaseBlockType> getCustomTypes() {
        return Collections.unmodifiableSet(customTypes);
    }

    public static Block getBlock(String blockId) {
        BaseBlockType blockType = getBlockType(blockId);
        return blockType.create();
    }

    public static Block getBlock(String blockId, int state) {
        BaseBlockType blockType = getBlockType(blockId);
        return blockType.create(state);
    }

}

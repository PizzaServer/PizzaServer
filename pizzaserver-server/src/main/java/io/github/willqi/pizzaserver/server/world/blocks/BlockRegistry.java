package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.server.world.blocks.types.*;

import java.util.*;

public class BlockRegistry {

    // All registered block types
    private final Map<String, BlockType> types = new HashMap<>();

    // All registered CUSTOM block types
    private final Map<String, BlockType> customTypes = new HashMap<>();


    public BlockRegistry() {
        this.registerVanillaBlocks();
    }

    public void register(BlockType blockType) {
        if (this.types.containsKey(blockType.getBlockId())) {
            throw new IllegalArgumentException("Block id " + blockType.getBlockId() + " was already registered.");
        }

        if (!blockType.getBlockId().startsWith("minecraft:")) {
            this.customTypes.put(blockType.getBlockId(), blockType);
        }
        this.types.put(blockType.getBlockId(), blockType);
    }

    public BlockType getBlockType(String blockId) {
        if (!this.types.containsKey(blockId)) {
            throw new NullPointerException("Could not find a block type by the id of " + blockId);
        }
        return this.types.get(blockId);
    }

    /**
     * Retrieve all non-Vanilla block types that are registered
     * @return registered non-Vanilla block types
     */
    public Set<BlockType> getCustomTypes() {
        return new HashSet<>(this.customTypes.values());
    }

    public boolean hasBlockType(String blockId) {
        return this.types.containsKey(blockId);
    }

    private void registerVanillaBlocks() {
        this.register(new BlockTypeAir());
        this.register(new BlockTypeDirt());
        this.register(new BlockTypeGrass());
        this.register(new BlockTypeStone());
    }



}

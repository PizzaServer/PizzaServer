package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.commons.data.id.Identifier;
import io.github.willqi.pizzaserver.commons.data.id.Namespace;
import io.github.willqi.pizzaserver.server.world.blocks.types.*;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeAir;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeDirt;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeGrass;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeStone;

import java.util.*;

/**
 * Registry that contains all blocks the server should recognize
 */
public class BlockRegistry {

    // All registered block types
    private final Map<Identifier, BlockType> types = new HashMap<>();

    // All registered CUSTOM block types
    private final Set<BlockType> customTypes = new HashSet<>();


    public BlockRegistry() {
        this.registerVanillaBlocks();
    }

    /**
     * Register a {@link BlockType} to the server
     * Custom blocks will need to register their {@link BlockType} in order to be used and for the world to render correctly
     * @param blockType {@link BlockType} that needs to be registered
     */
    public void register(BlockType blockType) {
        if (this.types.containsKey(blockType.getBlockId())) {
            throw new IllegalArgumentException("Block id " + blockType.getBlockId() + " was already registered.");
        }

        if (!blockType.getBlockId().getNamespace().equals("minecraft")) {
            this.customTypes.add(blockType);
        }
        this.types.put(blockType.getBlockId(), blockType);
    }

    /**
     * Retrieve a {@link BlockType} by it's id (e.g. minecraft:air)
     * @param blockId The id of the block (e.g. minecraft:air)
     * @return {@link BlockType}
     * @throws NullPointerException if the block does not exist
     */
    public BlockType getBlockType(Identifier blockId) {
        if (!this.types.containsKey(blockId)) {
            throw new NullPointerException("Could not find a block type by the id of " + blockId);
        }
        return this.types.get(blockId);
    }

    /**
     * Check if a block id was registered
     * @param blockId the id of the block (e.g. minecraft:air)
     * @return if the block was registered or not
     */
    public boolean hasBlockType(Identifier blockId) {
        return this.types.containsKey(blockId);
    }

    /**
     * Retrieve all non-Vanilla {@link BlockType}s that are registered
     * @return registered non-Vanilla {@link BlockType}s
     */
    public Set<BlockType> getCustomTypes() {
        return Collections.unmodifiableSet(this.customTypes);
    }

    private void registerVanillaBlocks() {
        this.register(new BlockTypeAir());
        this.register(new BlockTypeDirt());
        this.register(new BlockTypeGrass());
        this.register(new BlockTypeStone());
    }



}

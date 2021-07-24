package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.api.world.blocks.APIBlockRegistry;
import io.github.willqi.pizzaserver.api.world.blocks.types.APIBlockType;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeAir;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeDirt;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeGrass;
import io.github.willqi.pizzaserver.server.world.blocks.types.impl.BlockTypeStone;

import java.util.*;

/**
 * Registry that contains all blocks the server should recognize
 */
public class BlockRegistry implements APIBlockRegistry {

    // All registered block types
    private final Map<String, APIBlockType> types = new HashMap<>();

    // All registered CUSTOM block types
    private final Set<APIBlockType> customTypes = new HashSet<>();


    public BlockRegistry() {
        this.registerVanillaBlocks();
    }

    @Override
    public void register(APIBlockType blockType) {
        if (this.types.containsKey(blockType.getBlockId())) {
            throw new IllegalArgumentException("Block id " + blockType.getBlockId() + " was already registered.");
        }

        if (!blockType.getBlockId().startsWith("minecraft:")) {
            this.customTypes.add(blockType);
        }
        this.types.put(blockType.getBlockId(), blockType);
    }

    @Override
    public APIBlockType getBlockType(String blockId) {
        if (!this.types.containsKey(blockId)) {
            throw new NullPointerException("Could not find a block type by the id of " + blockId);
        }
        return this.types.get(blockId);
    }

    @Override
    public boolean hasBlockType(String blockId) {
        return this.types.containsKey(blockId);
    }

    @Override
    public Set<APIBlockType> getCustomTypes() {
        return Collections.unmodifiableSet(this.customTypes);
    }

    private void registerVanillaBlocks() {
        this.register(new BlockTypeAir());
        this.register(new BlockTypeDirt());
        this.register(new BlockTypeGrass());
        this.register(new BlockTypeStone());
    }



}

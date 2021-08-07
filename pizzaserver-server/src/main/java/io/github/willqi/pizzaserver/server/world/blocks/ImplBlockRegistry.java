package io.github.willqi.pizzaserver.server.world.blocks;

import io.github.willqi.pizzaserver.api.world.blocks.BlockRegistry;
import io.github.willqi.pizzaserver.api.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeAir;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeDirt;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeGrass;
import io.github.willqi.pizzaserver.server.world.blocks.types.BlockTypeStone;

import java.util.*;

/**
 * Registry that contains all blocks the server should recognize
 */
public class ImplBlockRegistry implements BlockRegistry {

    // All registered block types
    private final Map<String, BaseBlockType> types = new HashMap<>();

    // All registered CUSTOM block types
    private final Set<BaseBlockType> customTypes = new HashSet<>();


    public ImplBlockRegistry() {
        this.registerVanillaBlocks();
    }

    @Override
    public void register(BaseBlockType blockType) {
        if (this.types.containsKey(blockType.getBlockId())) {
            throw new IllegalArgumentException("Block id " + blockType.getBlockId() + " was already registered.");
        }

        if (!blockType.getBlockId().startsWith("minecraft:")) {
            this.customTypes.add(blockType);
        }
        this.types.put(blockType.getBlockId(), blockType);
    }

    @Override
    public BaseBlockType getBlockType(String blockId) {
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
    public Set<BaseBlockType> getCustomTypes() {
        return Collections.unmodifiableSet(this.customTypes);
    }

    private void registerVanillaBlocks() {
        this.register(new BlockTypeAir());
        this.register(new BlockTypeDirt());
        this.register(new BlockTypeGrass());
        this.register(new BlockTypeStone());
    }



}

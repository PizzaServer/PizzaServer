package io.github.pizzaserver.server.block;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.server.item.type.ImplBlockItemType;

import java.util.*;

public class ImplBlockRegistry implements BlockRegistry {

    // All registered block types
    private final BiMap<String, Block> blocks = HashBiMap.create();
    private final Map<Class<? extends Block>, BlockBehavior> behaviors = new HashMap<>();

    // All registered CUSTOM block types
    private final Set<Block> customTypes = new HashSet<>();

    @Override
    public void register(Block block, BlockBehavior behavior) {
        if (block.getBlockId().startsWith("minecraft:")) {
            this.customTypes.add(block);
        }

        this.blocks.put(block.getBlockId(), block);
        this.behaviors.put(block.getClass(), behavior);
        ItemRegistry.getInstance().register(new ImplBlockItemType(block));
    }

    @Override
    public boolean hasBlock(String blockId) {
        return this.blocks.containsKey(blockId);
    }

    @Override
    public Set<Block> getCustomBlocks() {
        return Collections.unmodifiableSet(this.customTypes);
    }

    @Override
    public Block getBlock(String blockId) {
        return this.getBlock(blockId, 0);
    }

    @Override
    public Block getBlock(String blockId, int state) {
        if (!this.blocks.containsKey(blockId)) {
            throw new NullPointerException("No registered block could be found by the id: " + blockId);
        }

        Block block = this.blocks.get(blockId).clone();
        block.setBlockState(state);
        return block;
    }

    @Override
    public BlockBehavior getBlockBehavior(Block block) {
        if (!this.behaviors.containsKey(block.getClass())) {
            throw new NullPointerException("There is no block behavior class for the provided class.");
        }

        return this.behaviors.get(block.getClass());
    }

}

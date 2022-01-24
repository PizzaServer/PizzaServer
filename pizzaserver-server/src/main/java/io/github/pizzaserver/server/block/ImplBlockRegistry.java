package io.github.pizzaserver.server.block;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.behavior.BlockBehavior;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.server.item.behavior.impl.ItemBlockBehavior;

import java.util.*;

public class ImplBlockRegistry implements BlockRegistry {

    // All registered block types
    private final Map<String, Block> blocks = new HashMap<>();
    private final Map<Class<? extends Block>, BlockBehavior<? extends Block>> behaviors = new HashMap<>();

    // All registered CUSTOM block types
    private final Set<Block> customTypes = new HashSet<>();

    @Override
    public <T extends Block> void register(T block, BlockBehavior<T> behavior) {
        Block registeredBlock = block.clone();
        if (!registeredBlock.getBlockId().startsWith("minecraft:")) {
            this.customTypes.add(registeredBlock);
        }

        this.blocks.put(registeredBlock.getBlockId(), registeredBlock);
        this.behaviors.put(registeredBlock.getClass(), behavior);
        ItemRegistry.getInstance().register(new ItemBlock(registeredBlock), new ItemBlockBehavior());
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
    @SuppressWarnings("unchecked")
    public <T extends Block> BlockBehavior<T> getBlockBehavior(T block) {
        if (!this.behaviors.containsKey(block.getClass())) {
            throw new NullPointerException("There is no block behavior class for the provided class. Was it registered?");
        }

        return (BlockBehavior<T>) this.behaviors.get(block.getClass());
    }

}

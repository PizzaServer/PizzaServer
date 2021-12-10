package io.github.pizzaserver.server.block;

import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.server.item.type.BaseBlockItemType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.types.BaseBlockType;

import java.util.*;

public class ImplBlockRegistry implements BlockRegistry {

    // All registered block types
    private final Map<String, BaseBlockType> types = new HashMap<>();

    // All registered CUSTOM block types
    private final Set<BaseBlockType> customTypes = new HashSet<>();

    @Override
    public void register(BaseBlockType blockType) {
        if (!blockType.getBlockId().startsWith("minecraft:")) {
            this.customTypes.add(blockType);
        }
        this.types.put(blockType.getBlockId(), blockType);

        ItemRegistry.getInstance().register(new BaseBlockItemType(blockType));    // Register the item representation of this block
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

    @Override
    public Block getBlock(String blockId) {
        BaseBlockType blockType = this.getBlockType(blockId);
        return blockType.create();
    }

    @Override
    public Block getBlock(String blockId, int state) {
        BaseBlockType blockType = this.getBlockType(blockId);
        return blockType.create(state);
    }

}

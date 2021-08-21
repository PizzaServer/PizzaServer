package io.github.willqi.pizzaserver.api.item.types;

import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;

/**
 * Any block item class is an instance of this class to prevent the need to create thousands of item classes for each block
 */
public class BlockItemType extends BaseItemType {

    private final BaseBlockType blockType;


    public BlockItemType(BaseBlockType blockType) {
        this.blockType = blockType;
    }

    public BaseBlockType getBlockType() {
        return this.blockType;
    }

    @Override
    public String getItemId() {
        return this.blockType.getBlockId();
    }

    @Override
    public String getName() {
        return this.blockType.getName();
    }

    @Override
    public String getIconName() {
        throw new UnsupportedOperationException("Unable to retrieve icon URL for a BlockItemType");
    }

}

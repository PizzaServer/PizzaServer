package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockPolishedBlackstoneButton extends BlockButton {

    public BlockPolishedBlackstoneButton() {
        super(BlockID.POLISHED_BLACKSTONE_BUTTON);
    }

    @Override
    public String getName() {
        return "Polished Blackstone Button";
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

}

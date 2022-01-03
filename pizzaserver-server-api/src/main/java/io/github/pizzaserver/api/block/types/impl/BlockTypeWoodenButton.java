package io.github.pizzaserver.api.block.types.impl;

import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.data.WoodType;

public class BlockTypeWoodenButton extends BlockTypeButton {

    protected final WoodType woodType;


    public BlockTypeWoodenButton(String buttonId, WoodType woodType) {
        super(buttonId);
        this.woodType = woodType;
    }

    @Override
    public String getName(int blockStateIndex) {
        return this.getWoodType().getName() + " Button";
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.AXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

}

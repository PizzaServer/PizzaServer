package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.data.PillarAxis;
import io.github.pizzaserver.api.block.data.StrippedType;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.trait.FlammableTrait;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public abstract class BlockStrippableWoodenLikeBlock extends BaseBlock implements FlammableTrait {

    protected WoodType woodType;
    protected boolean stripped;

    public BlockStrippableWoodenLikeBlock(WoodType woodType, StrippedType strippedType, PillarAxis axis) {
        this.setWoodType(woodType);
        this.setStripped(strippedType == StrippedType.STRIPPED);
        this.setPillarAxis(axis);
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }

    public boolean isStripped() {
        return this.stripped;
    }

    public void setStripped(boolean stripped) {
        this.stripped = stripped;
    }

    public abstract PillarAxis getPillarAxis();

    public abstract void setPillarAxis(PillarAxis axis);

    @Override
    public float getHardness() {
        return 2;
    }

    @Override
    public float getBlastResistance() {
        return 2;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
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
    public int getBurnOdds() {
        return 5;
    }

    @Override
    public int getFlameOdds() {
        return 5;
    }

}

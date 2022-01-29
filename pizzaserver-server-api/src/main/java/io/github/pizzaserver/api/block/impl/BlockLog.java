package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockLog extends Block {

    public BlockLog() {
        this(WoodType.OAK);
    }

    public BlockLog(WoodType woodType) {
        this.setWoodType(woodType);
    }


    public WoodType getWoodType() {
        return WoodType.CRIMSON;
    }

    public void setWoodType(WoodType woodType) {

    }

    @Override
    public String getBlockId() {
        return switch (this.getWoodType()) {
            case OAK, SPRUCE, BIRCH, JUNGLE -> BlockID.LOG;
            case ACACIA, DARK_OAK -> BlockID.LOG2;
            case CRIMSON -> BlockID.CRIMSON_STEM;
            case WARPED -> BlockID.WARPED_STEM;
        };
    }

    @Override
    public String getName() {
        return switch (this.getWoodType()) {
            case CRIMSON, WARPED -> this.getWoodType().getName() + " Stem";
            default -> this.getWoodType().getName() + " Log";
        };
    }

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
    public int getStackMeta() {
        return switch (this.getWoodType()) {
            case OAK, SPRUCE, BIRCH, JUNGLE -> this.getWoodType().ordinal();
            case ACACIA, DARK_OAK -> this.getWoodType().ordinal() - 4;
            default -> 0;
        };
    }

    @Override
    public void updateFromStackMeta(int meta) {

    }

}

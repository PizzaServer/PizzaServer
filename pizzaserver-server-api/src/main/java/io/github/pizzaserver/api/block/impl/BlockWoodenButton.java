package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public class BlockWoodenButton extends BlockButton {

    protected final WoodType woodType;


    public BlockWoodenButton() {
        this(WoodType.OAK);
    }

    public BlockWoodenButton(WoodType woodType) {
        super(resolveButtonIdByWoodType(woodType));
        this.woodType = woodType;
    }

    @Override
    public String getName() {
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


    private static String resolveButtonIdByWoodType(WoodType woodType) {
        return switch (woodType) {
            case OAK -> BlockID.OAK_BUTTON;
            case BIRCH -> BlockID.BIRCH_BUTTON;
            case ACACIA -> BlockID.ACACIA_BUTTON;
            case JUNGLE -> BlockID.JUNGLE_BUTTON;
            case SPRUCE -> BlockID.SPRUCE_BUTTON;
            case DARK_OAK -> BlockID.DARK_OAK_BUTTON;
            case WARPED -> BlockID.WARPED_BUTTON;
            case CRIMSON -> BlockID.CRIMSON_BUTTON;
        };
    }

}

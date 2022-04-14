package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.trait.WoodVariantTrait;
import io.github.pizzaserver.api.blockentity.type.BlockEntitySign;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;

public abstract class BlockSign extends BlockBlockEntity<BlockEntitySign> implements WoodVariantTrait {

    private WoodType woodType;
    

    public BlockSign() {
        this(WoodType.OAK);
    }

    public BlockSign(WoodType woodType) {
        this.setWoodType(woodType);
    }

    @Override
    public String getItemId() {
        return switch (this.getWoodType()) {
            case OAK -> ItemID.OAK_SIGN;
            case SPRUCE -> ItemID.SPRUCE_SIGN;
            case BIRCH -> ItemID.BIRCH_SIGN;
            case JUNGLE -> ItemID.JUNGLE_SIGN;
            case ACACIA -> ItemID.ACACIA_SIGN;
            case DARK_OAK -> ItemID.DARK_OAK_SIGN;
            case CRIMSON -> ItemID.CRIMSON_SIGN;
            case WARPED -> ItemID.WARPED_SIGN;
        };
    }

    @Override
    public String getName() {
        return this.getWoodType().getName() + " Sign";
    }

    @Override
    public float getHardness() {
        return 1;
    }

    @Override
    public float getBlastResistance() {
        return 1;
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

    @Override
    public boolean hasCollision() {
        return false;
    }

    @Override
    public int getMaxStackSize() {
        return 16;
    }

    @Override
    public int getFuelTicks() {
        return 200;
    }

    @Override
    public WoodType getWoodType() {
        return this.woodType;
    }

    @Override
    public void setWoodType(WoodType woodType) {
        this.woodType = woodType;
    }

}

package io.github.pizzaserver.api.block.impl;

import org.cloudburstmc.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConcretePowder extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = Arrays.stream(DyeColor.values())
            .map(color -> NbtMap.builder()
                    .putString("color", color.getId())
                    .build())
            .collect(Collectors.toList());


    public BlockConcretePowder() {
        this(DyeColor.WHITE);
    }

    public BlockConcretePowder(DyeColor dyeColor) {
        this.setBlockState(dyeColor.ordinal());
    }

    public DyeColor getColor() {
        return DyeColor.values()[this.getBlockState()];
    }

    public void setColor(DyeColor color) {
        this.setBlockState(color.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.CONCRETE_POWDER;
    }

    @Override
    public String getItemId() {
        return ItemID.CONCRETE_POWDER;
    }

    @Override
    public String getName() {
        return this.getColor().getDisplayName() + " Concrete Powder";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.5f;
    }

    @Override
    public float getBlastResistance() {
        return 0.5f;
    }

    @Override
    public boolean isAffectedByGravity() {
        return true;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHOVEL;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public int getStackMeta() {
        return this.getColor().ordinal();
    }

    @Override
    public void updateFromStackMeta(int meta) {
        if (meta >= 0 && meta < DyeColor.values().length) {
            this.setBlockState(meta);
        }
    }

}

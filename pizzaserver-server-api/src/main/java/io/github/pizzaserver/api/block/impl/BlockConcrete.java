package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockConcrete extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = Arrays.stream(DyeColor.values())
            .map(color -> NbtMap.builder()
                    .putString("color", color.getId())
                    .build())
            .collect(Collectors.toList());


    public BlockConcrete() {
        this(DyeColor.WHITE);
    }

    public BlockConcrete(DyeColor color) {
        this.setColor(color);
    }

    public DyeColor getColor() {
        return DyeColor.values()[this.getBlockState()];
    }

    public void setColor(DyeColor color) {
        this.setBlockState(color.ordinal());
    }

    @Override
    public String getBlockId() {
        return BlockID.CONCRETE;
    }

    @Override
    public String getName() {
        return "Concrete";
    }

    @Override
    public float getHardness() {
        return 1.8f;
    }

    @Override
    public float getBlastResistance() {
        return 1.8f;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.PICKAXE;
    }

    @Override
    public ToolTier getToolTierRequired() {
        return ToolTier.WOOD;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
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

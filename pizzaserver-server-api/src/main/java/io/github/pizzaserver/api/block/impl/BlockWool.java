package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.descriptors.Flammable;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class BlockWool extends Block implements Flammable {

    private static final List<NbtMap> BLOCK_STATES = Arrays.stream(DyeColor.values())
            .map(color -> NbtMap.builder()
                    .putString("color", color.getId())
                    .build())
            .collect(Collectors.toList());

    public BlockWool() {
        this(DyeColor.WHITE);
    }

    public BlockWool(DyeColor color) {
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
        return BlockID.WOOL;
    }

    @Override
    public String getName() {
        return this.getColor().getDisplayName() + " Wool";
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

    @Override
    public float getHardness() {
        return 0.8f;
    }

    @Override
    public float getBlastResistance() {
        return 0.8f;
    }

    @Override
    public boolean canBeMinedWithHand() {
        return true;
    }

    @Override
    public ToolType getToolTypeRequired() {
        return ToolType.SHEARS;
    }

    @Override
    public int getBurnOdds() {
        return 30;
    }

    @Override
    public int getFlameOdds() {
        return 60;
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

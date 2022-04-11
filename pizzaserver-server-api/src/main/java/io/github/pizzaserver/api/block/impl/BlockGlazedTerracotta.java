package io.github.pizzaserver.api.block.impl;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.BaseBlock;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.ArrayList;
import java.util.List;

public class BlockGlazedTerracotta extends BaseBlock {

    private static final List<NbtMap> BLOCK_STATES = new ArrayList<>() {
        {
            for (int i = 0; i < 6; i++) {
                this.add(NbtMap.builder()
                        .putInt("facing_direction", i)
                        .build());
            }
        }
    };

    private DyeColor dyeColor;

    public BlockGlazedTerracotta() {
        this(DyeColor.WHITE);
    }

    public BlockGlazedTerracotta(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    public DyeColor getDyeColor() {
        return this.dyeColor;
    }

    public void setDyeColor(DyeColor dyeColor) {
        this.dyeColor = dyeColor;
    }

    @Override
    public String getBlockId() {
        return switch (this.getDyeColor()) {
            case WHITE -> BlockID.WHITE_GLAZED_TERRACOTTA;
            case ORANGE -> BlockID.ORANGE_GLAZED_TERRACOTTA;
            case MAGENTA -> BlockID.MAGENTA_GLAZED_TERRACOTTA;
            case LIGHT_BLUE -> BlockID.LIGHT_BLUE_GLAZED_TERRACOTTA;
            case YELLOW -> BlockID.YELLOW_GLAZED_TERRACOTTA;
            case LIME -> BlockID.LIME_GLAZED_TERRACOTTA;
            case PINK -> BlockID.PINK_GLAZED_TERRACOTTA;
            case GRAY -> BlockID.GRAY_GLAZED_TERRACOTTA;
            case LIGHT_GRAY -> BlockID.LIGHT_GRAY_GLAZED_TERRACOTTA;
            case CYAN -> BlockID.CYAN_GLAZED_TERRACOTTA;
            case PURPLE -> BlockID.PURPLE_GLAZED_TERRACOTTA;
            case BLUE -> BlockID.BLUE_GLAZED_TERRACOTTA;
            case BROWN -> BlockID.BROWN_GLAZED_TERRACOTTA;
            case GREEN -> BlockID.GREEN_GLAZED_TERRACOTTA;
            case RED -> BlockID.RED_GLAZED_TERRACOTTA;
            case BLACK -> BlockID.BLACK_GLAZED_TERRACOTTA;
        };
    }

    @Override
    public String getName() {
        return this.getDyeColor().getDisplayName() + " Glazed Terracotta";
    }

    @Override
    public float getHardness() {
        return 1.4f;
    }

    @Override
    public float getBlastResistance() {
        return 1.4f;
    }

    @Override
    public List<NbtMap> getNBTStates() {
        return BLOCK_STATES;
    }

}

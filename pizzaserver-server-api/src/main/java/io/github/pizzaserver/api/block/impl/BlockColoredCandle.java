package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.utils.DyeColor;

public class BlockColoredCandle extends BlockCandle {

    private DyeColor color;

    public BlockColoredCandle() {
        this(DyeColor.WHITE);
    }

    public BlockColoredCandle(DyeColor color) {
        this.setColor(color);
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        this.color = color;
    }

    @Override
    public String getBlockId() {
        return switch (this.getColor()) {
            case WHITE -> BlockID.WHITE_CANDLE;
            case ORANGE -> BlockID.ORANGE_CANDLE;
            case MAGENTA -> BlockID.MAGENTA_CANDLE;
            case LIGHT_BLUE -> BlockID.LIGHT_BLUE_CANDLE;
            case YELLOW -> BlockID.YELLOW_CANDLE;
            case LIME -> BlockID.LIME_CANDLE;
            case PINK -> BlockID.PINK_CANDLE;
            case GRAY -> BlockID.GRAY_CANDLE;
            case LIGHT_GRAY -> BlockID.LIGHT_GRAY_CANDLE;
            case CYAN -> BlockID.CYAN_CANDLE;
            case PURPLE -> BlockID.PURPLE_CANDLE;
            case BLUE -> BlockID.BLUE_CANDLE;
            case BROWN -> BlockID.BROWN_CANDLE;
            case GREEN -> BlockID.GREEN_CANDLE;
            case RED -> BlockID.RED_CANDLE;
            case BLACK -> BlockID.BLACK_CANDLE;
        };
    }

    @Override
    public String getName() {
        return this.getColor().getDisplayName() + " Candle";
    }

}

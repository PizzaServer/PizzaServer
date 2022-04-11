package io.github.pizzaserver.api.block.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemBlock;
import io.github.pizzaserver.api.utils.DyeColor;

import java.util.Collections;
import java.util.Set;

public class BlockColoredCandleCake extends BlockCandleCake {

    private DyeColor color;

    public BlockColoredCandleCake() {
        this(DyeColor.WHITE);
    }

    public BlockColoredCandleCake(DyeColor color) {
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
            case WHITE -> BlockID.WHITE_CANDLE_CAKE;
            case ORANGE -> BlockID.ORANGE_CANDLE_CAKE;
            case MAGENTA -> BlockID.MAGENTA_CANDLE_CAKE;
            case LIGHT_BLUE -> BlockID.LIGHT_BLUE_CANDLE_CAKE;
            case YELLOW -> BlockID.YELLOW_CANDLE_CAKE;
            case LIME -> BlockID.LIME_CANDLE_CAKE;
            case PINK -> BlockID.PINK_CANDLE_CAKE;
            case GRAY -> BlockID.GRAY_CANDLE_CAKE;
            case LIGHT_GRAY -> BlockID.LIGHT_GRAY_CANDLE_CAKE;
            case CYAN -> BlockID.CYAN_CANDLE_CAKE;
            case PURPLE -> BlockID.PURPLE_CANDLE_CAKE;
            case BLUE -> BlockID.BLUE_CANDLE_CAKE;
            case BROWN -> BlockID.BROWN_CANDLE_CAKE;
            case GREEN -> BlockID.GREEN_CANDLE_CAKE;
            case RED -> BlockID.RED_CANDLE_CAKE;
            case BLACK -> BlockID.BLACK_CANDLE_CAKE;
        };
    }

    @Override
    public String getName() {
        return this.getColor().getDisplayName() + " " + super.getName();
    }

    @Override
    public Set<Item> getDrops(Entity entity) {
        return Collections.singleton(new ItemBlock(new BlockColoredCandle(this.getColor())));
    }

}

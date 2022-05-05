package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.BaseItem;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.utils.DyeColor;

public class ItemDye extends BaseItem {

    private final DyeColor dyeColor;


    public ItemDye(DyeColor color) {
        this(color, 1);
    }

    public ItemDye(DyeColor color, int count) {
        super(color.getId(), count, 0);
        this.dyeColor = color;
    }

    @Override
    public String getName() {
        return this.getColor().getDisplayName() + " Dye";
    }

    @Override
    public String getItemId() {
        return switch (this.getColor()) {
            case WHITE -> ItemID.WHITE_DYE;
            case ORANGE -> ItemID.ORANGE_DYE;
            case MAGENTA -> ItemID.MAGENTA_DYE;
            case LIGHT_BLUE -> ItemID.LIGHT_BLUE_DYE;
            case YELLOW -> ItemID.YELLOW_DYE;
            case LIME -> ItemID.LIME_DYE;
            case PINK -> ItemID.PINK_DYE;
            case GRAY -> ItemID.GRAY_DYE;
            case LIGHT_GRAY -> ItemID.LIGHT_GRAY_DYE;
            case CYAN -> ItemID.CYAN_DYE;
            case PURPLE -> ItemID.PURPLE_DYE;
            case BLUE -> ItemID.BLUE_DYE;
            case BROWN -> ItemID.BROWN_DYE;
            case GREEN -> ItemID.GREEN_DYE;
            case RED -> ItemID.RED_DYE;
            case BLACK -> ItemID.BLACK_DYE;
        };
    }

    public DyeColor getColor() {
        return this.dyeColor;
    }

}

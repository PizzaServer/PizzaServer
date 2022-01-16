package io.github.pizzaserver.api.blockentity.impl;

import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.blockentity.BlockEntityRegistry;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.api.utils.DyeColor;

public class BlockEntityBed extends BaseBlockEntity {

    public static final String ID = "Bed";

    protected DyeColor color = DyeColor.WHITE;


    public BlockEntityBed(BlockLocation blockPosition) {
        super(blockPosition);
    }

    public DyeColor getColor() {
        return this.color;
    }

    public void setColor(DyeColor color) {
        if (this.color != color) {
            this.color = color;
            this.update();
        }
    }

    @Override
    public BlockEntityType getType() {
        return BlockEntityRegistry.getInstance().getBlockEntityType(ID);
    }
}

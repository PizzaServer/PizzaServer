package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockBed;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.utils.DyeColor;

public interface BlockEntityBed extends BlockEntity<BlockBed> {

    String ID = "Bed";

    @Override
    default String getId() {
        return ID;
    }

    DyeColor getColor();

    void setColor(DyeColor color);

}

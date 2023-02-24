package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockBell;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public interface BlockEntityBell extends BlockEntity<BlockBell> {

    String ID = "Bell";

    @Override
    default String getId() {
        return ID;
    }

    void ring();

    boolean isRinging();

}

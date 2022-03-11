package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockBell;
import io.github.pizzaserver.api.blockentity.type.BlockEntityBell;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityBell extends BaseBlockEntity<BlockBell> implements BlockEntityBell {

    public ImplBlockEntityBell(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.BELL);
    }


    @Override
    public void ring() {

    }

    @Override
    public boolean isRinging() {
        return this.getBlock().isRinging();
    }

}

package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockEnderChest;
import io.github.pizzaserver.api.blockentity.type.BlockEntityEnderChest;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityEnderChest extends BaseBlockEntity<BlockEnderChest> implements BlockEntityEnderChest {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.ENDER_CHEST);

    public ImplBlockEntityEnderChest(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

}

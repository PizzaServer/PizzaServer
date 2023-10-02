package io.github.pizzaserver.server.blockentity.type.impl;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockDropper;
import io.github.pizzaserver.api.blockentity.type.BlockEntityDropper;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityDropper extends ImplBlockEntityContainer<BlockDropper> implements BlockEntityDropper {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.DROPPER);

    public ImplBlockEntityDropper(BlockLocation location) {
        super(location, ContainerType.DROPPER);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

}

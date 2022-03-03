package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockDispenser;
import io.github.pizzaserver.api.blockentity.type.BlockEntityDispenser;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityDispenser extends ImplBlockEntityContainer<BlockDispenser> implements BlockEntityDispenser {

    public ImplBlockEntityDispenser(BlockLocation location) {
        super(location, ContainerType.DISPENSER);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.DISPENSER);
    }

}

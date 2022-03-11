package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockHopper;
import io.github.pizzaserver.api.blockentity.type.BlockEntityHopper;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityHopper extends ImplBlockEntityContainer<BlockHopper> implements BlockEntityHopper {

    public ImplBlockEntityHopper(BlockLocation location) {
        super(location, ContainerType.HOPPER);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.HOPPER);
    }

}

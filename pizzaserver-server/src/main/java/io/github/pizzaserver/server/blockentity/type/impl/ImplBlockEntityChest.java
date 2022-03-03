package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.type.BlockEntityChest;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityChest extends ImplBlockEntityContainer<BlockChest> implements BlockEntityChest {

    public ImplBlockEntityChest(BlockLocation location) {
        super(location, ContainerType.CONTAINER);
    }

    @Override
    public Set<String> getBlockIds() {
        return Collections.singleton(BlockID.CHEST);
    }

}

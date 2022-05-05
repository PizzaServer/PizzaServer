package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;

public class ImplContainerInventory<B extends Block, E extends BlockEntity<B>> extends ImplBlockEntityInventory<B, E> {

    public ImplContainerInventory(E blockEntity, ContainerType containerType) {
        super(blockEntity, containerType);
    }

}

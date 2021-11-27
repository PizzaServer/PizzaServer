package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.entity.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.player.Player;

public abstract class BlockEntityContainer extends BaseBlockEntity {

    protected BlockEntityInventory inventory = Server.getInstance().createInventory(this, ContainerType.CONTAINER);


    public BlockEntityContainer(Block block) {
        super(block);
    }

    public BlockEntityInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean onInteract(Player player) {
        if (!player.isSneaking() && player.openInventory(this.inventory)) {
            this.getLocation().getWorld().addBlockEvent(this.getLocation().toVector3i(), 1, 1);
            return false;
        }
        return true;
    }

}

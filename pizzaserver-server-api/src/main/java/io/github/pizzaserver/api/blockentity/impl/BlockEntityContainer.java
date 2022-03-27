package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.inventory.BlockInventory;
import io.github.pizzaserver.api.event.type.inventory.InventoryOpenEvent;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public abstract class BlockEntityContainer<T extends Block> extends BaseBlockEntity<T> {

    protected BlockInventory<T> inventory = Server.getInstance().createInventory(this.getBlock(), ContainerType.CONTAINER);


    public BlockEntityContainer(T block) {
        super(block);
    }

    public BlockInventory<T> getInventory() {
        return this.inventory;
    }

    @Override
    public boolean onInteract(Player player) {
        if (this.getInventory().canBeOpenedBy(player) && !player.isSneaking()) {
            InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(player, this.getInventory());
            Server.getInstance().getEventManager().call(inventoryOpenEvent);
            if (!inventoryOpenEvent.isCancelled()) {
                player.openInventory(this.getInventory());
                player.getWorld().addBlockEvent(this.getBlock().getLocation().toVector3i(), 1, 1);
                return false;
            }
        }
        return true;
    }

}

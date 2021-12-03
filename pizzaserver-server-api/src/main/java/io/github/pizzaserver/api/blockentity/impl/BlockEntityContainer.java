package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.entity.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.event.type.inventory.InventoryOpenEvent;
import io.github.pizzaserver.api.player.Player;

public abstract class BlockEntityContainer extends BaseBlockEntity {

    protected BlockEntityInventory inventory = Server.getInstance().createInventory(this, ContainerType.CONTAINER);


    public BlockEntityContainer(Vector3i blockLocation) {
        super(blockLocation);
    }

    public BlockEntityInventory getInventory() {
        return this.inventory;
    }

    @Override
    public boolean onInteract(Player player) {
        if (this.inventory.canBeOpenedBy(player) && !player.isSneaking()) {
            InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(player, this.inventory);
            Server.getInstance().getEventManager().call(inventoryOpenEvent);
            if (!inventoryOpenEvent.isCancelled()) {
                player.openInventory(this.inventory);
                player.getWorld().addBlockEvent(this.getPosition(), 1, 1);
                return false;
            }
        }
        return true;
    }

}

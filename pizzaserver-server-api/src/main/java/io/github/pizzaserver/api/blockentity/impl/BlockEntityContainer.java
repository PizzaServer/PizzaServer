package io.github.pizzaserver.api.blockentity.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.blockentity.BaseBlockEntity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.event.type.inventory.InventoryOpenEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public abstract class BlockEntityContainer extends BaseBlockEntity {

    protected BlockEntityInventory inventory;


    public BlockEntityContainer(BlockLocation blockLocation, ContainerType containerType) {
        super(blockLocation);
        this.inventory = Server.getInstance().createInventory(this, containerType);
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
                this.showOpenAnimation();
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBreak(Player player) {
        for (Player viewer : this.inventory.getViewers()) {
            viewer.closeOpenInventory();
        }

        for (Item item : this.getInventory().getSlots()) {
            if (!item.isEmpty()) {
                player.getWorld().addItemEntity(item, this.blockPosition.toVector3f().add(0.5f, 0.5f, 0.5f), EntityItem.getRandomMotion());
            }
        }
    }

    public void showOpenAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showOpenAnimation(player);
        }
    }

    public void showOpenAnimation(Player player) {
        player.getWorld().addBlockEvent(player, this.getLocation().toVector3i(), 1, 1);
    }

    public void showCloseAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showCloseAnimation(player);
        }
    }

    public void showCloseAnimation(Player player) {
        player.getWorld().addBlockEvent(player, this.getLocation().toVector3i(), 1, 0);
    }

}

package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.event.type.inventory.InventoryOpenEvent;
import io.github.pizzaserver.api.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

public abstract class ImplBlockEntityContainer<T extends Block> extends BaseBlockEntity<T> implements BlockEntityContainer<T> {

    protected BlockEntityInventory<? extends BlockEntity<T>> inventory;


    public ImplBlockEntityContainer(BlockLocation location, ContainerType containerType) {
        super(location);
        this.inventory = Server.getInstance().createInventory(this, containerType);
    }

    @Override
    public BlockEntityInventory<? extends BlockEntity<T>> getInventory() {
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
                player.getWorld().addItemEntity(item, this.blockLocation.toVector3f().add(0.5f, 0.5f, 0.5f), EntityItem.getRandomMotion());
            }
        }
    }

    @Override
    public void showOpenAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showOpenAnimation(player);
        }
    }

    @Override
    public void showOpenAnimation(Player player) {
        player.getWorld().addBlockEvent(player, this.getLocation().toVector3i(), 1, 1);
    }

    @Override
    public void showCloseAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showCloseAnimation(player);
        }
    }

    @Override
    public void showCloseAnimation(Player player) {
        player.getWorld().addBlockEvent(player, this.getLocation().toVector3i(), 1, 0);
    }


}

package io.github.pizzaserver.server.blockentity.type.impl;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.trait.BlockEntityOpenableTrait;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.event.type.inventory.InventoryOpenEvent;
import io.github.pizzaserver.api.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;
import io.github.pizzaserver.server.inventory.ImplContainerInventory;

import java.util.Optional;

public abstract class ImplBlockEntityContainer<T extends Block> extends BaseBlockEntity<T> implements BlockEntityContainer<T> {

    protected String customName;

    protected BlockEntityInventory<T, ? extends BlockEntity<T>> inventory;


    public ImplBlockEntityContainer(BlockLocation location, ContainerType containerType) {
        super(location);
        this.inventory = new ImplContainerInventory<>(this, containerType);
    }

    @Override
    public BlockEntityInventory<T, ? extends BlockEntity<T>> getInventory() {
        return this.inventory;
    }

    @Override
    public boolean onInteract(Player player) {
        if (this.inventory.canBeOpenedBy(player) && !player.isSneaking()) {
            InventoryOpenEvent inventoryOpenEvent = new InventoryOpenEvent(player, this.inventory);
            Server.getInstance().getEventManager().call(inventoryOpenEvent);

            if (!inventoryOpenEvent.isCancelled()) {
                player.openInventory(this.inventory);

                if (this instanceof BlockEntityOpenableTrait openableBlockEntity) {
                    openableBlockEntity.showOpenAnimation();
                }
                return false;
            }
        }
        return true;
    }

    @Override
    public void onBreak(Entity entity) {
        for (Player viewer : this.inventory.getViewers()) {
            viewer.closeOpenInventory();
        }

        for (Item item : this.getInventory().getSlots()) {
            if (!item.isEmpty()) {
                entity.getWorld().addItemEntity(item, this.blockLocation.toVector3f().add(0.5f, 0.5f, 0.5f), EntityItem.getRandomMotion());
            }
        }
    }

    @Override
    public Optional<String> getCustomName() {
        return Optional.ofNullable(this.customName);
    }

    @Override
    public void setCustomName(String name) {
        this.customName = name;
        this.update();
    }

}

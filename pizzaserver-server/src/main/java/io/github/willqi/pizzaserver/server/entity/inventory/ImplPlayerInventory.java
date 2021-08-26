package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerOpenPacket;

import java.util.Optional;

public class ImplPlayerInventory extends ImplLivingEntityInventory implements PlayerInventory {

    private int selectedSlot;
    private ItemStack cursor = ItemRegistry.getItem(BlockTypeID.AIR);


    public ImplPlayerInventory(Player player) {
        super(player, 36, 0);
    }

    @Override
    public Player getEntity() {
        return (Player)super.getEntity();
    }

    @Override
    public boolean setHelmet(ItemStack helmet) {
        if (super.setHelmet(helmet)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setChestplate(ItemStack chestplate) {
        if (super.setChestplate(chestplate)) {
            // TODO: update slot packet (also check if a mob equipment packet will remove the need for it)
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setLeggings(ItemStack leggings) {
        if (super.setLeggings(leggings)) {
            // TODO: update slot packet (also check if a mob equipment packet will remove the need for it)
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean setBoots(ItemStack boots) {
        if (super.setBoots(boots)) {
            // TODO: update slot packet (also check if a mob equipment packet will remove the need for it)
            return true;
        } else {
            return false;
        }
    }

    @Override
    public int getSelectedSlot() {
        return this.selectedSlot;
    }

    @Override
    public boolean setSelectedSlot(int slot) {
        // TODO: validate slot is in hotbar
        if (this.selectedSlot != slot) {
            this.selectedSlot = slot;
            // TODO: Send packet
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getHeldItem() {
        return this.getSlot(this.getSelectedSlot());
    }

    @Override
    public boolean setHeldItem(ItemStack mainHand) {
        return this.setSlot(this.getSelectedSlot(), mainHand);
    }

    @Override
    public boolean setOffhandItem(ItemStack offHand) {
        if (super.setOffhandItem(offHand)) {
            // TODO: send packet
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getCursor() {
        return Optional.ofNullable(this.cursor).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
    }

    @Override
    public boolean setCursor(ItemStack item) {
        if (isDifferentItems(this.cursor, item)) {
            this.cursor = item;
            // TODO: send cursor packet
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setEntityRuntimeId(this.getEntity().getId());
        containerOpenPacket.setInventoryId(this.getId());
        containerOpenPacket.setInventoryType(-1);   // TODO: get rid of magic number and replace with enum
        containerOpenPacket.setCoordinates(this.getEntity().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);
    }
}

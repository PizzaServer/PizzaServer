package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.event.type.inventory.InventoryCloseEvent;
import io.github.pizzaserver.api.inventory.OpenableInventory;
import io.github.pizzaserver.api.player.Player;

public abstract class ImplOpenableInventory extends BaseInventory implements OpenableInventory {

    public ImplOpenableInventory(ContainerType containerType, int size) {
        super(containerType, size);
    }

    public ImplOpenableInventory(ContainerType containerType, int size, int id) {
        super(containerType, size, id);
    }

    protected abstract void sendContainerOpenPacket(Player player);

    /**
     * Close this inventory for a player.
     * @param player the player to close this inventory for
     * @return if the inventory was closed
     */
    public boolean closeFor(Player player) {
        if (this.viewers.contains(player)) {
            this.viewers.remove(player);

            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setId((byte) this.getId());
            player.sendPacket(containerClosePacket);

            InventoryCloseEvent inventoryCloseEvent = new InventoryCloseEvent(player, this);
            Server.getInstance().getEventManager().call(inventoryCloseEvent);
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldBeClosedFor(Player player) {
        return this.getViewers().contains(player);
    }

    /**
     * Tries to open the inventory.
     * @param player the player to open this inventory to
     * @return if the inventory was opened
     */
    public boolean openFor(Player player) {
        if (this.canBeOpenedBy(player)) {
            this.viewers.add(player);
            this.sendContainerOpenPacket(player);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return !this.viewers.contains(player);
    }

}

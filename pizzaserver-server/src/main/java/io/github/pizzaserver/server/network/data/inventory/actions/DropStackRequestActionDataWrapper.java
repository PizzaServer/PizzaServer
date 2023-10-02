package io.github.pizzaserver.server.network.data.inventory.actions;

import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DropAction;

public class DropStackRequestActionDataWrapper extends StackRequestActionWrapper<DropAction> {

    private final int amount;
    private final InventorySlotContainer source;

    public DropStackRequestActionDataWrapper(ImplPlayer player, DropAction action) {
        super(player);
        this.amount = action.getCount();
        this.source = new InventorySlotContainer(player, action.getSource().getContainer(), action.getSource().getSlot());
    }

    public int getCountRequested() {
        return this.amount;
    }

    public InventorySlotContainer getSource() {
        return this.source;
    }

}

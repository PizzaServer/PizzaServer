package io.github.pizzaserver.server.network.data.inventory.actions;

import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.SwapAction;

public class SwapStackRequestActionDataWrapper extends StackRequestActionWrapper<SwapAction> {

    private final InventorySlotContainer source;
    private final InventorySlotContainer destination;

    public SwapStackRequestActionDataWrapper(ImplPlayer player, SwapAction action) {
        super(player);
        this.source = new InventorySlotContainer(player, action.getSource().getContainer(), action.getSource().getSlot());
        this.destination = new InventorySlotContainer(player, action.getDestination().getContainer(), action.getDestination().getSlot());
    }

    public InventorySlotContainer getSource() {
        return this.source;
    }

    public InventorySlotContainer getDestination() {
        return this.destination;
    }

}

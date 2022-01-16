package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.SwapStackRequestActionData;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;

public class SwapStackRequestActionDataWrapper extends StackRequestActionWrapper<SwapStackRequestActionData> {

    private final InventorySlotContainer source;
    private final InventorySlotContainer destination;

    public SwapStackRequestActionDataWrapper(ImplPlayer player, SwapStackRequestActionData action) {
        super(player);
        this.source = new InventorySlotContainer(player,
                                                 action.getSource().getContainer(),
                                                 action.getSource().getSlot());
        this.destination = new InventorySlotContainer(player,
                                                      action.getDestination().getContainer(),
                                                      action.getDestination().getSlot());
    }

    public InventorySlotContainer getSource() {
        return this.source;
    }

    public InventorySlotContainer getDestination() {
        return this.destination;
    }
}

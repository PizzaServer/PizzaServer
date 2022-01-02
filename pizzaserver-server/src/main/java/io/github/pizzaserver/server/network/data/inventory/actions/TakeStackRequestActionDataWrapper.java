package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.TakeStackRequestActionData;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;

public class TakeStackRequestActionDataWrapper extends StackRequestActionWrapper<TakeStackRequestActionData> {

    private final int amount;
    private final InventorySlotContainer source;
    private final InventorySlotContainer destination;

    public TakeStackRequestActionDataWrapper(ImplPlayer player, TakeStackRequestActionData action) {
        super(player);
        this.amount = action.getCount();
        this.source = new InventorySlotContainer(player, action.getSource().getContainer(), action.getSource().getSlot());
        this.destination = new InventorySlotContainer(player, action.getDestination().getContainer(), action.getDestination().getSlot());
    }

    public int getCountRequested() {
        return this.amount;
    }

    public InventorySlotContainer getSource() {
        return this.source;
    }

    public InventorySlotContainer getDestination() {
        return this.destination;
    }

}

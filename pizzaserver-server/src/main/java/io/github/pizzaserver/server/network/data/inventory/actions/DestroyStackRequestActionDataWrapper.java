package io.github.pizzaserver.server.network.data.inventory.actions;

import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.DestroyAction;

public class DestroyStackRequestActionDataWrapper extends StackRequestActionWrapper<DestroyAction> {

    private final int amount;
    private final InventorySlotContainer source;

    public DestroyStackRequestActionDataWrapper(ImplPlayer player, DestroyAction action) {
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

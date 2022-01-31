package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.DropStackRequestActionData;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;

public class DropStackRequestActionDataWrapper extends StackRequestActionWrapper<DropStackRequestActionData> {

    private final int amount;
    private final InventorySlotContainer source;

    public DropStackRequestActionDataWrapper(ImplPlayer player, DropStackRequestActionData action) {
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

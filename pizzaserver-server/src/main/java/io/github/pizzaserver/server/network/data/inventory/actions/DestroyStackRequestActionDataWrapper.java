package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.DestroyStackRequestActionData;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;

public class DestroyStackRequestActionDataWrapper extends StackRequestActionWrapper<DestroyStackRequestActionData> {

    private final int amount;
    private final InventorySlotContainer source;

    public DestroyStackRequestActionDataWrapper(ImplPlayer player, DestroyStackRequestActionData action) {
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

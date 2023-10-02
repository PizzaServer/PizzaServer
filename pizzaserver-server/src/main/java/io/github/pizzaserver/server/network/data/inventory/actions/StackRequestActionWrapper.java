package io.github.pizzaserver.server.network.data.inventory.actions;

import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.ItemStackRequestAction;

public abstract class StackRequestActionWrapper<T extends ItemStackRequestAction> {

    protected final ImplPlayer player;

    public StackRequestActionWrapper(ImplPlayer player) {
        this.player = player;
    }

}

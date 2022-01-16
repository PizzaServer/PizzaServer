package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionData;
import io.github.pizzaserver.server.player.ImplPlayer;

public abstract class StackRequestActionWrapper<T extends StackRequestActionData> {

    protected final ImplPlayer player;

    public StackRequestActionWrapper(ImplPlayer player) {
        this.player = player;
    }
}

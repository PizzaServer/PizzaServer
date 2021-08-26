package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerClosePacket;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;

public class InventoryPacketHandler extends BaseBedrockPacketHandler {

    private final ImplPlayer player;


    public InventoryPacketHandler(ImplPlayer player) {
        this.player = player;
    }

    @Override
    public void onPacket(ContainerClosePacket packet) {

    }

}

package io.github.willqi.pizzaserver.network;

import io.github.willqi.pizzaserver.network.protocol.packets.*;

public abstract class BedrockPacketHandler {

    /**
     * Called when any packet is processed
     * @param packet
     */
    public void onPacket(BedrockPacket packet) {

    }

    public void onPacket(LoginPacket packet) {

    }

}

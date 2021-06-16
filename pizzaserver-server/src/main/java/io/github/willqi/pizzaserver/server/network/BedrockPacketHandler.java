package io.github.willqi.pizzaserver.server.network;

import io.github.willqi.pizzaserver.server.network.protocol.packets.*;

public abstract class BedrockPacketHandler {

    /**
     * Called when any packet is processed
     * @param packet
     */
    public void onPacket(BedrockPacket packet) {}

    public void onPacket(LoginPacket packet) {}

    public void onPacket(ResourcePackResponsePacket packet) {}
    public void onPacket(ResourcePackChunkRequestPacket packet) {}

    public void onPacket(ClientCacheStatusPacket packet) {}
    public void onPacket(RequestChunkRadiusPacket packet) {}

    public void onPacket(MovePlayerPacket packet) {}

    public void onPacket(ViolationPacket packet) {}

}

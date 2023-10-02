package io.github.pizzaserver.api.network.protocol;

import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;

public interface PacketHandlerPipeline {

    PacketHandlerPipeline addFirst(BedrockPacketHandler...handlers);

    PacketHandlerPipeline addLast(BedrockPacketHandler ...handlers);

    PacketHandlerPipeline remove(BedrockPacketHandler ...handlers);

}

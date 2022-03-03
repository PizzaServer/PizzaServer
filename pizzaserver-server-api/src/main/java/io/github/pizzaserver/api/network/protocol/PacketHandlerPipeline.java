package io.github.pizzaserver.api.network.protocol;

import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;

public interface PacketHandlerPipeline {

    PacketHandlerPipeline addFirst(BedrockPacketHandler ...handlers);

    PacketHandlerPipeline addLast(BedrockPacketHandler ...handlers);

    PacketHandlerPipeline remove(BedrockPacketHandler ...handlers);

}

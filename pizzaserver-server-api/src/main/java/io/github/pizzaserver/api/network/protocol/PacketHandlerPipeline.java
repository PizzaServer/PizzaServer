package io.github.pizzaserver.api.network.protocol;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;

import java.util.function.Consumer;

public interface PacketHandlerPipeline extends Consumer<BedrockPacket> {

    PacketHandlerPipeline addFirst(BedrockPacketHandler ...handlers);

    PacketHandlerPipeline addLast(BedrockPacketHandler ...handlers);

    PacketHandlerPipeline remove(BedrockPacketHandler ...handlers);

}

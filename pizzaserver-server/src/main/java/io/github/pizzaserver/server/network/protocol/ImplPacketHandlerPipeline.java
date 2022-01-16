package io.github.pizzaserver.server.network.protocol;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import io.github.pizzaserver.api.network.protocol.PacketHandlerPipeline;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ImplPacketHandlerPipeline implements PacketHandlerPipeline {

    private final List<BedrockPacketHandler> handlers = new ArrayList<>();

    @Override
    public PacketHandlerPipeline addFirst(BedrockPacketHandler... handlers) {
        this.handlers.addAll(0, Arrays.asList(handlers));
        return this;
    }

    @Override
    public PacketHandlerPipeline addLast(BedrockPacketHandler... handlers) {
        this.handlers.addAll(Arrays.asList(handlers));
        return this;
    }

    @Override
    public PacketHandlerPipeline remove(BedrockPacketHandler... handlers) {
        for (BedrockPacketHandler handler : handlers) {
            this.handlers.remove(handler);
        }
        return this;
    }

    @Override
    public void accept(BedrockPacket packet) {
        List<BedrockPacketHandler> currentHandlers = new ArrayList<>(this.handlers);

        for (BedrockPacketHandler handler : currentHandlers) {
            boolean shouldIgnoreOtherPacketHandlers = packet.handle(handler);

            if (shouldIgnoreOtherPacketHandlers) {
                break;
            }
        }
    }
}

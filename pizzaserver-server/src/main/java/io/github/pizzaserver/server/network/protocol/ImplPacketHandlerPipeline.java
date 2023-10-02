package io.github.pizzaserver.server.network.protocol;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.network.protocol.PacketHandlerPipeline;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacket;
import org.cloudburstmc.protocol.bedrock.packet.BedrockPacketHandler;
import org.cloudburstmc.protocol.common.PacketSignal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class ImplPacketHandlerPipeline implements PacketHandlerPipeline, Consumer<BedrockPacket> {

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
            PacketSignal shouldIgnoreOtherPacketHandlers = packet.handle(handler);
            Server.getInstance().getLogger().info("Logging packets through handlers insdiidfkasmlj nslk ");
            Server.getInstance().getLogger().info("Packet SIgnal: " + (shouldIgnoreOtherPacketHandlers == PacketSignal.HANDLED));
            // TODO: DO some things and stuff here maybe?
/*            if (shouldIgnoreOtherPacketHandlers == PacketSignal.HANDLED) {
                break;
            }*/
        }
    }

}

package io.github.willqi.pizzaserver.network.handlers;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import io.github.willqi.pizzaserver.Server;

/**
 * Handles preparing/authenticating a client to ensure a proper {@link io.github.willqi.pizzaserver.Player}
 */
public class PlayerInitializationPacketHandler implements BedrockPacketHandler {

    private Server server;
    private BedrockServerSession session;

    public PlayerInitializationPacketHandler(BedrockServerSession session, Server server) {
        this.session = session;
        this.server = server;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        return true;
    }

}

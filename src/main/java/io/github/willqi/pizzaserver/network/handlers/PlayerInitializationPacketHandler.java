package io.github.willqi.pizzaserver.network.handlers;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.LoginPacket;
import com.nukkitx.protocol.bedrock.packet.PlayStatusPacket;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.network.ServerProtocol;
import io.github.willqi.pizzaserver.player.Player;
import io.github.willqi.pizzaserver.player.data.LoginData;

/**
 * Handles preparing/authenticating a client to ensure a proper {@link Player}
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
        if (ServerProtocol.SUPPORTED_PROTOCOL_CODEC.containsKey(packet.getProtocolVersion())) {
            this.session.setPacketCodec(ServerProtocol.SUPPORTED_PROTOCOL_CODEC.get(packet.getProtocolVersion()));
            LoginData loginData = new LoginData(packet.getProtocolVersion(), packet.getChainData(), packet.getSkinData());
            Player player = new Player(this.server, this.session, loginData);
        } else {
            PlayStatusPacket loginFailPacket = new PlayStatusPacket();
            if (packet.getProtocolVersion() > ServerProtocol.LATEST_SUPPORTED_PROTOCOL) {
                loginFailPacket.setStatus(PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD);
            } else {
                loginFailPacket.setStatus(PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD);
            }
            this.server.getNetwork().queueClientboundPacket(this.session, loginFailPacket);
        }
        return true;
    }

}

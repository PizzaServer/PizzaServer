package io.github.pizzaserver.server.network.handlers;

import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.server.network.protocol.versions.BaseMinecraftVersion;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.network.utils.LoginData;

import java.util.*;

/**
 * Handles login and handshake flow of players.
 */
public class LoginHandshakePacketHandler implements BedrockPacketHandler {

    private final ImplServer server;
    private final PlayerSession session;


    public LoginHandshakePacketHandler(ImplServer server, PlayerSession session) {
        this.server = server;
        this.session = session;
    }

    @Override
    public boolean handle(LoginPacket loginPacket) {
        // Check if the protocol version is supported
        Optional<BaseMinecraftVersion> version = ServerProtocol.getProtocol(loginPacket.getProtocolVersion());

        if (!version.isPresent()) {
            PlayStatusPacket loginFailPacket = new PlayStatusPacket();
            if (loginPacket.getProtocolVersion() > ServerProtocol.LATEST_PROTOCOL_VERISON) {
                loginFailPacket.setStatus(PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD);
            } else {
                loginFailPacket.setStatus(PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD);
            }
            this.session.getConnection().sendPacketImmediately(loginFailPacket);
            this.session.getConnection().disconnect();
            return true;
        }
        this.session.getConnection().setPacketCodec(version.get().getPacketCodec());
        this.session.setVersion(version.get());

        Optional<LoginData> loginData = LoginData.extract(loginPacket.getChainData(), loginPacket.getChainData());
        if (!loginData.isPresent() || (!loginData.get().isAuthenticated() && this.server.getConfig().isOnlineMode())) {
            this.session.getConnection().disconnect();
            return true;
        }

        if (this.server.getPlayerCount() >= this.server.getMaximumPlayerCount()) {
            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.Status.FAILED_SERVER_FULL_SUB_CLIENT);
            this.session.getConnection().sendPacket(playStatusPacket);
            return true;
        }

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        this.session.getConnection().sendPacket(playStatusPacket);

        // Initialization successful.
        this.session.setPacketHandler(new ResourcePackPacketHandler(this.server, this.session, loginData.get()));
        return true;
    }

    @Override
    public boolean handle(PacketViolationWarningPacket packet) {
        this.server.getLogger().error("Packet violation for " + packet.getPacketType() + ": " + packet.getContext());
        return true;
    }

    @Override
    public boolean handle(ClientToServerHandshakePacket packet) {
        return false;   // TODO
    }

}

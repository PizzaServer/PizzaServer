package io.github.pizzaserver.server.network.handlers;

import com.nimbusds.jose.JWSObject;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import com.nukkitx.protocol.bedrock.util.EncryptionUtils;
import io.github.pizzaserver.server.network.protocol.version.BaseMinecraftVersion;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.network.data.LoginData;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.*;

/**
 * Handles login and handshake flow of players.
 */
public class LoginHandshakePacketHandler implements BedrockPacketHandler {

    private final ImplServer server;
    private final PlayerSession session;

    private LoginData loginData;


    public LoginHandshakePacketHandler(ImplServer server, PlayerSession session) {
        this.server = server;
        this.session = session;
    }

    @Override
    public boolean handle(LoginPacket loginPacket) {
        // Check if the protocol version is supported
        Optional<BaseMinecraftVersion> version = ServerProtocol.getProtocol(loginPacket.getProtocolVersion());

        if (version.isEmpty()) {
            PlayStatusPacket loginFailPacket = new PlayStatusPacket();
            if (loginPacket.getProtocolVersion() > ServerProtocol.LATEST_PROTOCOL_VERSION) {
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

        Optional<LoginData> loginData = LoginData.extract(loginPacket.getChainData(), loginPacket.getSkinData());
        if (loginData.isEmpty() || (!loginData.get().isAuthenticated() && this.server.getConfig().isOnlineMode())) {
            this.session.getConnection().disconnect();
            return true;
        }
        this.loginData = loginData.get();

        if (!EncryptionUtils.canUseEncryption()) {
            this.server.getLogger().error("Packet encryption is not supported.");
            this.session.getConnection().disconnect();
            return true;
        }

        JWSObject encryptionJWT;
        SecretKey encryptionSecretKey;
        try {
            PublicKey clientKey = EncryptionUtils.generateKey(loginData.get().getIdentityPublicKey());

            KeyPair encryptionKeyPair = EncryptionUtils.createKeyPair();
            byte[] encryptionToken = EncryptionUtils.generateRandomToken();
            encryptionSecretKey = EncryptionUtils.getSecretKey(encryptionKeyPair.getPrivate(), clientKey, encryptionToken);

            encryptionJWT = EncryptionUtils.createHandshakeJwt(encryptionKeyPair, encryptionToken);
        } catch (Exception exception) {
            this.server.getLogger().debug("Failed to initialize packet encryption.", exception);
            this.session.getConnection().disconnect();
            return true;
        }

        this.session.getConnection().enableEncryption(encryptionSecretKey);

        ServerToClientHandshakePacket handshakePacket = new ServerToClientHandshakePacket();
        handshakePacket.setJwt(encryptionJWT.serialize());
        this.session.getConnection().sendPacket(handshakePacket);
        return true;
    }

    @Override
    public boolean handle(ClientToServerHandshakePacket packet) {
        if (this.session.getConnection().isEncrypted()) {
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
            this.session.addPacketHandler(new ResourcePackPacketHandler(this.server, this.session, this.loginData));
            this.session.removePacketHandler(this);
            return true;
        }
        return true;
    }

    @Override
    public boolean handle(PacketViolationWarningPacket packet) {
        this.server.getLogger().debug("Packet violation for " + packet.getPacketType() + ": " + packet.getContext());
        return true;
    }

}

package io.github.pizzaserver.server.player.handlers;

import com.nimbusds.jose.JWSObject;
import org.cloudburstmc.protocol.bedrock.netty.codec.encryption.BedrockEncryptionDecoder;
import org.cloudburstmc.protocol.bedrock.netty.codec.encryption.BedrockEncryptionEncoder;
import org.cloudburstmc.protocol.bedrock.packet.*;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.data.LoginData;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.pizzaserver.server.network.protocol.version.BaseMinecraftVersion;
import org.cloudburstmc.protocol.bedrock.util.EncryptionUtils;
import org.cloudburstmc.protocol.common.PacketSignal;

import javax.crypto.SecretKey;
import java.security.KeyPair;
import java.security.PublicKey;
import java.util.Optional;

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
    public PacketSignal handle(LoginPacket loginPacket) {
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
            return PacketSignal.HANDLED;
        }
        this.session.getConnection().setCodec(version.get().getPacketCodec());
        this.session.setVersion(version.get());

        Optional<LoginData> loginData = LoginData.extract(loginPacket.getChain(), loginPacket.getExtra());
        if (loginData.isEmpty() || (!loginData.get().isAuthenticated().signed() && this.server.getConfig().isOnlineMode())) {
            this.session.getConnection().disconnect();
            return PacketSignal.HANDLED;
        }
        this.loginData = loginData.get();

        if (!this.server.getConfig().isEncryptionEnabled()) {
            this.completeLogin();
            return PacketSignal.HANDLED;
        }

        // Looks like there's no excuse to not use encryption now in V3
/*        if (!EncryptionUtils.canUseEncryption()) {
            this.server.getLogger().error("Packet encryption is not supported on this machine.");
            this.session.getConnection().disconnect();
            return true;
        }*/

        JWSObject encryptionJWT;
        SecretKey encryptionSecretKey;
        try {
            PublicKey clientKey = EncryptionUtils.parseKey(loginData.get().getIdentityPublicKey());

            KeyPair encryptionKeyPair = EncryptionUtils.createKeyPair();
            byte[] encryptionToken = EncryptionUtils.generateRandomToken();
            encryptionSecretKey = EncryptionUtils.getSecretKey(encryptionKeyPair.getPrivate(), clientKey, encryptionToken);

            encryptionJWT = JWSObject.parse(EncryptionUtils.createHandshakeJwt(encryptionKeyPair, encryptionToken));
        } catch (Exception exception) {
            this.server.getLogger().debug("Failed to initialize packet encryption.", exception);
            this.session.getConnection().disconnect();
            return PacketSignal.HANDLED;
        }

        this.session.getConnection().enableEncryption(encryptionSecretKey);

        ServerToClientHandshakePacket handshakePacket = new ServerToClientHandshakePacket();
        handshakePacket.setJwt(encryptionJWT.serialize());
        this.session.getConnection().sendPacket(handshakePacket);
        return PacketSignal.HANDLED;
    }

    @Override
    public PacketSignal handle(ClientToServerHandshakePacket packet) {
        // This is how it's checked in V3
        if (this.session.getConnection().getPeer().getChannel().pipeline().get(BedrockEncryptionEncoder.class) != null ||
                this.session.getConnection().getPeer().getChannel().pipeline().get(BedrockEncryptionDecoder.class) != null) {
            this.completeLogin();
            return PacketSignal.HANDLED;
        }
        return PacketSignal.HANDLED;
    }

    @Override
    public PacketSignal handle(PacketViolationWarningPacket packet) {
        this.server.getLogger().debug("Packet violation for " + packet.getPacketType() + ": " + packet.getContext());
        return PacketSignal.HANDLED;
    }

    private void completeLogin() {
        if (this.server.getPlayerCount() >= this.server.getMaximumPlayerCount()) {
            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.Status.FAILED_SERVER_FULL_SUB_CLIENT);
            this.session.getConnection().sendPacket(playStatusPacket);
        } else {
            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
            this.session.getConnection().sendPacket(playStatusPacket);

            // Initialization successful.
            this.session.getPacketHandlerPipeline().addLast(new ResourcePackPacketHandler(this.server, this.session, this.loginData));
            this.session.getPacketHandlerPipeline().remove(this);
        }
    }

}

package io.github.willqi.pizzaserver.network.handlers;

import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.network.ServerProtocol;
import io.github.willqi.pizzaserver.player.Player;
import io.github.willqi.pizzaserver.player.data.LoginData;
import io.github.willqi.pizzaserver.events.player.PreLoginEvent;
import io.github.willqi.pizzaserver.resourcepacks.ResourcePack;

import java.util.stream.Collectors;

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

            if (loginData.isAuthenticated()) {

                PreLoginEvent event = new PreLoginEvent(player);
                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    this.session.disconnect("Failed to connect to server!");
                } else {

                    // We do not use/have the means to use encryption, so skip it
                    PlayStatusPacket playStatusPacket = new PlayStatusPacket();
                    playStatusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
                    this.server.getNetwork().queueClientboundPacket(this.session, playStatusPacket);

                    ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
                    resourcePacksInfoPacket.setForcedToAccept(this.server.getResourcePackManager().isPacksRequired());
                    resourcePacksInfoPacket.getResourcePackInfos().addAll(
                            this.server.getResourcePackManager()
                                    .getPacks()
                                    .stream()
                                    .map(ResourcePack::getEntry)
                                    .collect(Collectors.toSet())
                    );
                    this.server.getNetwork().queueServerboundPacket(this.session, resourcePacksInfoPacket);

                }

            } else {
                this.session.disconnect("Not Authenticated");
            }

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

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        return false;
    }

    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        return false;
    }

}

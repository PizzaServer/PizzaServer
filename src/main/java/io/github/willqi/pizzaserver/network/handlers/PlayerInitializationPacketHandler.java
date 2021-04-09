package io.github.willqi.pizzaserver.network.handlers;

import com.google.gson.JsonParseException;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.ResourcePackType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.network.ServerProtocol;
import io.github.willqi.pizzaserver.player.Player;
import io.github.willqi.pizzaserver.player.data.LoginData;
import io.github.willqi.pizzaserver.events.player.PreLoginEvent;
import io.github.willqi.pizzaserver.resourcepacks.ResourcePack;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Handles preparing/authenticating a client to ensure a proper {@link Player}
 */
public class PlayerInitializationPacketHandler implements BedrockPacketHandler {

    private Server server;
    private BedrockServerSession session;
    private Player player;

    public PlayerInitializationPacketHandler(BedrockServerSession session, Server server) {
        this.session = session;
        this.server = server;
        this.player = null;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        if (ServerProtocol.SUPPORTED_PROTOCOL_CODEC.containsKey(packet.getProtocolVersion())) {
            this.session.setPacketCodec(ServerProtocol.SUPPORTED_PROTOCOL_CODEC.get(packet.getProtocolVersion()));
            LoginData loginData;
            try {
                loginData = new LoginData(packet.getProtocolVersion(), packet.getChainData(), packet.getSkinData());
            } catch (JsonParseException exception) {
                this.server.getLogger().error("Failed to parse LoginData while handling LoginPacket");
                this.session.disconnect();
                return true;
            }
            Player player = new Player(this.server, this.session, loginData);
            this.player = player;

            if (loginData.isAuthenticated()) {

                PreLoginEvent event = new PreLoginEvent(player);
                this.server.getPluginManager().callEvent(event);

                if (event.isCancelled()) {
                    this.session.disconnect("Failed to connect to server!");
                } else {
                    // Connection valid. See if there's player space
                    if (this.server.getPlayerCount() < this.server.getMaximumPlayerCount()) {
                        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
                        playStatusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
                        this.server.getNetwork().queueClientboundPacket(this.session, playStatusPacket);

                        ResourcePacksInfoPacket resourcePacksInfoPacket = getResourcesPacksInfoPacket();
                        this.server.getNetwork().queueClientboundPacket(this.session, resourcePacksInfoPacket);
                    } else {
                        this.session.disconnect("The Server is Full!");
                    }
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
        if (this.player != null) {
            switch (packet.getStatus()) {
                case COMPLETED:

                    break;
                case HAVE_ALL_PACKS:
                    // Confirm they have all the packs
                    ResourcePacksInfoPacket resourcePacksInfoPacket = this.getResourcesPacksInfoPacket();
                    this.server.getNetwork().queueClientboundPacket(this.session, resourcePacksInfoPacket);
                    break;
                case SEND_PACKS:
                    for (String packIdAndVersion : packet.getPackIds()) {
                        String[] packIdData = packIdAndVersion.split("_");  // Index 0 is the uuid and 1 is the version
                        if (packIdData.length > 0) {
                            ResourcePackDataInfoPacket resourcePackDataInfoPacket = this.getResourcePackDataInfoPacket(UUID.fromString(packIdData[0]));
                            if (resourcePackDataInfoPacket != null) {
                                this.server.getNetwork().queueClientboundPacket(this.session, resourcePackDataInfoPacket);
                            } else {
                                this.server.getLogger().error("Invalid resource pack UUID specified while sending packs in ResourcePackClientResponsePacket.");
                                this.session.disconnect();
                                break;
                            }
                        } else {
                            this.server.getLogger().error("Failed to parse resource pack id while sending packs in ResorucePackClientResponsePacket.");
                            this.session.disconnect();
                            break;
                        }
                    }
                    break;
                case REFUSED:

                    break;
                case NONE:

                    break;
            }
        } else {
            this.server.getLogger().error("Client requested resource packs before player object was created.");
            this.session.disconnect();
        }
        return true;
    }

    // Sent after we send the ResourcePackDataInfo packet
    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        if (this.player != null) {
            if (this.server.getResourcePackManager().getPacks().containsKey(packet.getPackId())) {

                ResourcePack pack = this.server.getResourcePackManager().getPacks().get(packet.getPackId());
                if (packet.getChunkIndex() >= 0 && packet.getChunkIndex() < pack.getChunkCount()) {
                    ResourcePackChunkDataPacket chunkDataPacket = new ResourcePackChunkDataPacket();
                    chunkDataPacket.setPackId(pack.getUuid());
                    chunkDataPacket.setPackVersion(pack.getVersion());
                    chunkDataPacket.setChunkIndex(packet.getChunkIndex());
                    chunkDataPacket.setProgress((long)packet.getChunkIndex() * ResourcePack.CHUNK_LENGTH);  // Where to continue the download process from
                    chunkDataPacket.setData(pack.getChunk(packet.getChunkIndex()));
                    this.server.getNetwork().queueClientboundPacket(this.session, chunkDataPacket);

                } else {
                    this.server.getLogger().error("Invalid chunk requested while handling ResourcePackChunkRequestPacket");
                    this.session.disconnect();
                }

            } else {
                this.server.getLogger().error("Invalid resource pack UUID specified while handling ResourcePackChunkRequestPacket.");
                this.session.disconnect();
            }
        } else {
            this.server.getLogger().error("Client requested resource pack chunk before player object was created.");
            this.session.disconnect();
        }
        return true;
    }

    // Sent by server when ResourcePackClientResponse packet requests some packs to be downloaded
    private ResourcePackDataInfoPacket getResourcePackDataInfoPacket(UUID uuid) {
        if (this.server.getResourcePackManager().getPacks().containsKey(uuid)) {
            ResourcePack pack = this.server.getResourcePackManager().getPacks().get(uuid);
            ResourcePackDataInfoPacket packet = new ResourcePackDataInfoPacket();
            packet.setPackId(uuid);
            packet.setHash(pack.getHash());
            packet.setPackVersion(pack.getVersion());
            packet.setType(ResourcePackType.RESOURCE);
            packet.setChunkCount(pack.getChunkCount() - 1);
            packet.setCompressedPackSize(pack.getDataLength());
            packet.setMaxChunkSize(ResourcePack.CHUNK_LENGTH);
            return packet;
        } else {
            return null;
        }
    }

    // Sent by server on login and after client says it downloaded all the packs
    private ResourcePacksInfoPacket getResourcesPacksInfoPacket() {
        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        resourcePacksInfoPacket.getResourcePackInfos().addAll(
                this.server.getResourcePackManager()
                        .getPacks()
                        .values()
                        .stream()
                        .map(pack -> new ResourcePacksInfoPacket.Entry(pack.getUuid().toString(), pack.getVersion(), pack.getDataLength(), "", "", "", false, false))
                        .collect(Collectors.toSet())
        );
        return resourcePacksInfoPacket;
    }

}

package io.github.pizzaserver.server.network.handlers;

import com.nukkitx.protocol.bedrock.data.ExperimentData;
import com.nukkitx.protocol.bedrock.data.ResourcePackType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.network.data.LoginData;
import io.github.pizzaserver.api.packs.ResourcePack;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.*;

public class ResourcePackPacketHandler implements BedrockPacketHandler {

    private final ImplServer server;
    private final PlayerSession session;
    private final LoginData loginData;

    public ResourcePackPacketHandler(ImplServer server, PlayerSession session, LoginData loginData) {
        this.server = server;
        this.session = session;
        this.loginData = loginData;

        // Notify client about all resource packs on the server
        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        for (ResourcePack pack : this.server.getResourcePackManager().getPacks().values()) {
            resourcePacksInfoPacket.getResourcePackInfos().add(new ResourcePacksInfoPacket.Entry(pack.getUuid().toString(),
                    pack.getVersion(),
                    pack.getDataLength(),
                    "",
                    "",
                    "",
                    false,
                    pack.isRayTracingEnabled()));
        }
        session.getConnection().sendPacket(resourcePacksInfoPacket);
    }


    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        switch (packet.getStatus()) {
            case SEND_PACKS:
                // Send all pack info of the packs the client does not have
                for (String packId : packet.getPackIds()) {
                    UUID uuid = UUID.fromString(packId.split("_")[0]);

                    ResourcePack pack;
                    ResourcePackDataInfoPacket resourcePackDataInfoPacket = new ResourcePackDataInfoPacket();
                    if (this.server.getResourcePackManager().getPacks().containsKey(uuid)) {
                        pack = this.server.getResourcePackManager().getPacks().get(uuid);
                    } else {
                        this.server.getLogger().debug("Client requested invalid pack.");
                        this.session.getConnection().disconnect();
                        return true;
                    }
                    resourcePackDataInfoPacket.setType(ResourcePackType.RESOURCE);
                    resourcePackDataInfoPacket.setPackId(pack.getUuid());
                    resourcePackDataInfoPacket.setHash(pack.getHash());
                    resourcePackDataInfoPacket.setPackVersion(pack.getVersion());
                    resourcePackDataInfoPacket.setChunkCount(pack.getChunkCount());
                    resourcePackDataInfoPacket.setCompressedPackSize(pack.getDataLength());
                    resourcePackDataInfoPacket.setMaxChunkSize(pack.getMaxChunkLength());
                    this.session.getConnection().sendPacket(resourcePackDataInfoPacket);
                }
                break;

            case HAVE_ALL_PACKS:
                // Confirm resource packs again
                ResourcePackStackPacket stackPacket = new ResourcePackStackPacket();
                stackPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
                for (ResourcePack pack : this.server.getResourcePackManager().getPacks().values()) {
                    stackPacket.getResourcePacks().add(new ResourcePackStackPacket.Entry(pack.getUuid().toString(), pack.getVersion(), ""));
                }
                stackPacket.setGameVersion(this.session.getVersion().getVersion());
                stackPacket.getExperiments().add(new ExperimentData("data_driven_items", true));
                this.session.getConnection().sendPacket(stackPacket);
                break;
            case REFUSED:
                this.session.getConnection().disconnect();
                break;
            case COMPLETED:
                this.session.setPacketHandler(null);
                ImplPlayer player = new ImplPlayer(this.server, this.session, this.loginData);
                player.initialize();
                break;
        }
        return true;
    }

    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        ResourcePack pack = this.server.getResourcePackManager().getPacks().getOrDefault(packet.getPackId(), null);
        if (pack == null) {
            this.server.getLogger().debug("Invalid resource pack UUID specified while handling ResourcePackChunkRequestPacket.");
            this.session.getConnection().disconnect();
            return true;
        }

        if (packet.getChunkIndex() < 0 || packet.getChunkIndex() >= pack.getChunkCount()) {
            this.server.getLogger().debug("Invalid chunk requested while handling ResourcePackChunkRequestPacket");
            this.session.getConnection().disconnect();
            return true;
        }

        // Send the resource pack chunk requested
        ResourcePackChunkDataPacket chunkDataPacket = new ResourcePackChunkDataPacket();
        chunkDataPacket.setPackId(pack.getUuid());
        chunkDataPacket.setChunkIndex(packet.getChunkIndex());
        chunkDataPacket.setProgress((long) packet.getChunkIndex() * pack.getMaxChunkLength());
        chunkDataPacket.setData(pack.getChunk(packet.getChunkIndex()));
        this.session.getConnection().sendPacket(chunkDataPacket);
        return true;
    }

}

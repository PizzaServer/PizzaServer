package io.github.pizzaserver.server.player.handlers;

import com.nukkitx.protocol.bedrock.data.ExperimentData;
import com.nukkitx.protocol.bedrock.data.ResourcePackType;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.pizzaserver.api.packs.ResourcePack;
import io.github.pizzaserver.server.ImplServer;
import io.github.pizzaserver.server.network.data.LoginData;
import io.github.pizzaserver.server.network.protocol.PlayerSession;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.UUID;

public class ResourcePackPacketHandler implements BedrockPacketHandler {

    private final ImplServer server;
    private final PlayerSession session;
    private final LoginData loginData;

    private final Deque<DownloadingPack> packDownloadQueue = new ArrayDeque<>();

    public ResourcePackPacketHandler(ImplServer server, PlayerSession session, LoginData loginData) {
        this.server = server;
        this.session = session;
        this.loginData = loginData;

        // Notify client about all resource packs on the server
        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        for (ResourcePack pack : this.server.getResourcePackManager().getPacks().values()) {
            resourcePacksInfoPacket.getResourcePackInfos().add(
                    new ResourcePacksInfoPacket.Entry(
                            pack.getUuid().toString(),
                            pack.getVersion(),
                            pack.getDataLength(),
                            "", "", "", false,
                            pack.isRayTracingEnabled()));
        }
        session.getConnection().sendPacket(resourcePacksInfoPacket);
    }


    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        switch (packet.getStatus()) {
            case SEND_PACKS:
                // Create list of all packs' info of the packs the client does not have.
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

                    this.packDownloadQueue.addLast(new DownloadingPack(resourcePackDataInfoPacket));
                }

                // Send only the first pack to ensure client caches packs in different folders because of time-based naming (client bug)
                DownloadingPack first = this.packDownloadQueue.peekFirst();
                if (first != null) {
                    this.session.getConnection().sendPacket(first.dataInfoPacket);
                }

                break;
            case HAVE_ALL_PACKS:
                // this.packDownloadQueue.size() should be 0
                this.packDownloadQueue.clear();

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
                this.session.removePacketHandler(this);
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

        DownloadingPack currentPack = this.packDownloadQueue.peekFirst();

        if (packet.getChunkIndex() < 0 || packet.getChunkIndex() >= pack.getChunkCount() || currentPack == null) {
            this.server.getLogger().debug("Invalid chunk requested while handling ResourcePackChunkRequestPacket");
            this.session.getConnection().disconnect();
            return true;
        }

        // ensure that the client is requesting chunk indices increasing by 1, so that our download queue logic works out
        if (packet.getChunkIndex() != currentPack.currentChunkIndex) {
            this.server.getLogger().debug("Invalid chunk order requested while handling ResourcePackChunkRequestPacket");
            this.packDownloadQueue.clear();
            return true;
        } else {
            currentPack.currentChunkIndex++;
        }

        // Send the resource pack chunk requested
        ResourcePackChunkDataPacket chunkDataPacket = new ResourcePackChunkDataPacket();
        chunkDataPacket.setPackId(pack.getUuid());
        chunkDataPacket.setPackVersion(pack.getVersion());
        chunkDataPacket.setChunkIndex(packet.getChunkIndex());
        chunkDataPacket.setProgress((long) packet.getChunkIndex() * pack.getMaxChunkLength());
        chunkDataPacket.setData(pack.getChunk(packet.getChunkIndex()));
        this.session.getConnection().sendPacket(chunkDataPacket);

        if ((chunkDataPacket.getChunkIndex() + 1) == currentPack.dataInfoPacket.getChunkCount()) {
            // finished with this pack, send next one's info
            this.packDownloadQueue.removeFirst();

            DownloadingPack nextPack = this.packDownloadQueue.peekFirst();
            if (nextPack != null) {
                this.session.getConnection().sendPacket(nextPack.dataInfoPacket);
            }
        }

        return true;
    }

    @Override
    public boolean handle(PacketViolationWarningPacket packet) {
        this.server.getLogger().debug("Packet violation for " + packet.getPacketType() + ": " + packet.getContext());
        return true;
    }

    static class DownloadingPack {

        public int currentChunkIndex = 0;

        private final ResourcePackDataInfoPacket dataInfoPacket;

        DownloadingPack(ResourcePackDataInfoPacket dataInfoPacket) {
            this.dataInfoPacket = dataInfoPacket;
        }
    }
}

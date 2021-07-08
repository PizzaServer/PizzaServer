package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.server.data.ServerOrigin;
import io.github.willqi.pizzaserver.server.events.player.PreLoginEvent;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.BedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerMovementType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.packs.DataPack;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.server.player.data.PermissionLevel;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.utils.Vector2;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.server.world.World;
import io.github.willqi.pizzaserver.server.world.data.Dimension;
import io.github.willqi.pizzaserver.commons.world.WorldType;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * Handles preparing/authenticating a client to becoming a valid player
 * Includes Login > Packs > Starting Packets > PlayStatus - Player Spawn
 */
public class LoginPacketHandler extends BedrockPacketHandler {

    private final Server server;
    private final BedrockClientSession session;
    private Player player;


    public LoginPacketHandler(Server server, BedrockClientSession session) {
        this.server = server;
        this.session = session;
    }

    @Override
    public void onPacket(LoginPacket loginPacket) {

        if (this.player != null) {
            this.server.getLogger().info("Client tried to login again.");
            this.session.disconnect();
            return;
        }

        // Do we support the version
        if (!ServerProtocol.VERSIONS.containsKey(loginPacket.getProtocol())) {
            PlayStatusPacket loginFailPacket = new PlayStatusPacket();
            if (loginPacket.getProtocol() > ServerProtocol.LATEST_PROTOCOL_VERISON) {
                loginFailPacket.setStatus(PlayStatusPacket.PlayStatus.OUTDATED_SERVER);
            } else {
                loginFailPacket.setStatus(PlayStatusPacket.PlayStatus.OUTDATED_CLIENT);
            }
            this.session.queueSendPacket(loginFailPacket);
            return;
        }

        if (!loginPacket.isAuthenticated()) {
            this.session.disconnect();
            return;
        }

        Player player = new Player(this.server, this.session, loginPacket);
        this.player = player;

        PreLoginEvent event = new PreLoginEvent(player);
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            this.session.disconnect();
            return;
        }

        if (this.server.getPlayerCount() >= this.server.getMaximumPlayerCount()) {
            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.SERVER_FULL);
            player.sendPacket(playStatusPacket);
            return;
        }

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.LOGIN_SUCCESS);
        player.sendPacket(playStatusPacket);

        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        resourcePacksInfoPacket.setResourcePacks(
                this.server.getResourcePackManager()
                        .getResourcePacks()
                        .values().toArray(new DataPack[0])
        );
        resourcePacksInfoPacket.setBehaviorPacks(
                this.server.getResourcePackManager()
                    .getBehaviorPacks()
                    .values().toArray(new DataPack[0])
        );
        player.sendPacket(resourcePacksInfoPacket);
    }

    @Override
    public void onPacket(ViolationPacket packet) {
        throw new AssertionError("ViolationPacket for packet id " + packet.getPacketId() + " " + packet.getMessage());
    }


    @Override
    public void onPacket(ResourcePackResponsePacket packet) {

        if (this.player == null) {
            this.server.getLogger().error("Client requested packs before player object was created.");
            this.session.disconnect();
            return;
        }

        switch (packet.getStatus()) {
            case SEND_PACKS:
                // Send all pack info of the packs the client does not have
                for (PackInfo packInfo : packet.getPacksRequested()) {

                    if (this.server.getResourcePackManager().getResourcePacks().containsKey(packInfo.getUuid())) {
                        DataPack pack = this.server.getResourcePackManager().getResourcePacks().get(packInfo.getUuid());
                        ResourcePackDataInfoPacket resourcePackDataInfoPacket = new ResourcePackDataInfoPacket();
                        resourcePackDataInfoPacket.setPackId(pack.getUuid());
                        resourcePackDataInfoPacket.setHash(pack.getHash());
                        resourcePackDataInfoPacket.setVersion(pack.getVersion());
                        resourcePackDataInfoPacket.setType(ResourcePackDataInfoPacket.PackType.RESOURCE_PACK);
                        resourcePackDataInfoPacket.setChunkCount(pack.getChunkCount());
                        resourcePackDataInfoPacket.setCompressedPackageSize(pack.getDataLength());
                        resourcePackDataInfoPacket.setMaxChunkSize(DataPack.CHUNK_LENGTH);
                        this.player.sendPacket(resourcePackDataInfoPacket);
                    } else {
                        this.server.getLogger().error("Client requested invalid pack.");
                        this.session.disconnect();
                        break;
                    }

                }
                break;

            case HAVE_ALL_PACKS:
                // Confirm resource packs
                this.sendResourcePackStackPacket();
                break;
            case REFUSED:
                if (this.server.getResourcePackManager().arePacksRequired()) {
                    this.session.disconnect();
                } else {
                    // Confirm resource packs
                    this.sendResourcePackStackPacket();
                }
                break;
            case COMPLETED:
                this.sendGameLoginPackets();
                break;
        }
    }

    @Override
    public void onPacket(RequestChunkRadiusPacket packet) {
        ChunkRadiusUpdatedPacket chunkRadiusUpdatedPacket = new ChunkRadiusUpdatedPacket();
        chunkRadiusUpdatedPacket.setRadius(packet.getChunkRadiusRequested());
        this.player.sendPacket(chunkRadiusUpdatedPacket);
        // TODO: Send proper chunk radius
    }

    @Override
    public void onPacket(ResourcePackChunkRequestPacket packet) {
        if (this.player == null) {
            this.server.getLogger().error("Client requested resource pack chunk before player object was created.");
            this.session.disconnect();
            return;
        }

        if (!this.server.getResourcePackManager().getResourcePacks().containsKey(packet.getPackInfo().getUuid()) && !this.server.getResourcePackManager().getBehaviorPacks().containsKey(packet.getPackInfo().getUuid())) {
            this.server.getLogger().error("Invalid resource pack UUID specified while handling ResourcePackChunkRequestPacket.");
            this.session.disconnect();
            return;
        }

        DataPack pack = this.server.getResourcePackManager().getResourcePacks().getOrDefault(
                packet.getPackInfo().getUuid(),
                this.server.getResourcePackManager().getBehaviorPacks().get(packet.getPackInfo().getUuid()));

        if (packet.getChunkIndex() < 0 || packet.getChunkIndex() >= pack.getChunkCount()) {
            this.server.getLogger().error("Invalid chunk requested while handling ResourcePackChunkRequestPacket");
            this.session.disconnect();
            return;
        }

        // Send the resource pack chunk requested
        ResourcePackChunkDataPacket chunkDataPacket = new ResourcePackChunkDataPacket();
        chunkDataPacket.setId(pack.getUuid());
        chunkDataPacket.setVersion(pack.getVersion());
        chunkDataPacket.setChunkIndex(packet.getChunkIndex());
        chunkDataPacket.setChunkProgress((long)packet.getChunkIndex() * DataPack.CHUNK_LENGTH);  // Where to continue the download process from
        chunkDataPacket.setData(pack.getChunk(packet.getChunkIndex()));
        this.player.sendPacket(chunkDataPacket);
    }

    /**
     * Called when the client confirms they have all the packs
     */
    private void sendResourcePackStackPacket() {
        ResourcePackStackPacket stackPacket = new ResourcePackStackPacket();
        stackPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        stackPacket.setResourcePacks(this.server.getResourcePackManager().getResourcePacks().values().toArray(new DataPack[0]));
        stackPacket.setBehaviourPacks(this.server.getResourcePackManager().getBehaviorPacks().values().toArray(new DataPack[0]));
        stackPacket.setGameVersion(this.player.getVersion().getVersionString());
        this.player.sendPacket(stackPacket);
    }

    /**
     * Called when the player has passed the resource packs stage and is ready to start the game login process.
     */
    private void sendGameLoginPackets() {

        StartGamePacket startGamePacket = new StartGamePacket();

        // Entity specific
        startGamePacket.setDimension(Dimension.OVERWORLD);
        startGamePacket.setEntityId(this.player.getId());
        startGamePacket.setPlayerGamemode(Gamemode.SURVIVAL);
        startGamePacket.setPlayerPermissionLevel(PermissionLevel.MEMBER);
        startGamePacket.setRuntimeEntityId(this.player.getId());
        startGamePacket.setPlayerRotation(new Vector2(0, 0));
        startGamePacket.setPlayerSpawn(new Vector3(3344, 70, 28));

        // Server
        startGamePacket.setChunkTickRange(4);    // TODO: modify once you get chunks ticking
        startGamePacket.setCommandsEnabled(true);
        // packet.setCurrentTick(0);       // TODO: get actual tick count
        startGamePacket.setDefaultGamemode(Gamemode.SURVIVAL);
        startGamePacket.setDifficulty(Difficulty.PEACEFUL);
        // packet.setEnchantmentSeed(0);   // TODO: find actual seed
        startGamePacket.setGameVersion(ServerProtocol.GAME_VERSION);
        startGamePacket.setServerName("Testing");
        startGamePacket.setMovementType(PlayerMovementType.CLIENT_AUTHORITATIVE);
        startGamePacket.setServerAuthoritativeBlockBreaking(true);
        startGamePacket.setServerAuthoritativeInventory(true);
        startGamePacket.setResourcePacksRequired(this.server.getResourcePackManager().arePacksRequired());
        startGamePacket.setServerOrigin(ServerOrigin.NONE);
        startGamePacket.setItemStates(this.player.getVersion().getItemStates());

        // World
        startGamePacket.setWorldSpawn(new Vector3i(0, 0, 0));
        startGamePacket.setWorldId(Base64.getEncoder().encodeToString(startGamePacket.getServerName().getBytes(StandardCharsets.UTF_8)));
        startGamePacket.setWorldType(WorldType.INFINITE);


        CreativeContentPacket creativeContentPacket = new CreativeContentPacket();
        // TODO: Add creative contents to prevent mobile clients from crashing

        BiomeDefinitionPacket biomeDefinitionPacket = new BiomeDefinitionPacket();
        biomeDefinitionPacket.setTag(this.player.getVersion().getBiomeDefinitions());

        this.player.sendPacket(startGamePacket);
        this.player.sendPacket(creativeContentPacket);
        this.player.sendPacket(biomeDefinitionPacket);

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
        this.player.sendPacket(playStatusPacket);

    }

}

package io.github.willqi.pizzaserver.network.handlers;

import com.google.gson.JsonParseException;
import com.nukkitx.math.vector.Vector2f;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.data.*;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.willqi.pizzaserver.Server;
import io.github.willqi.pizzaserver.item.ItemPalette;
import io.github.willqi.pizzaserver.network.ServerProtocol;
import io.github.willqi.pizzaserver.player.Player;
import io.github.willqi.pizzaserver.player.data.LoginData;
import io.github.willqi.pizzaserver.events.player.PreLoginEvent;
import io.github.willqi.pizzaserver.resourcepacks.ResourcePack;
import io.github.willqi.pizzaserver.world.WorldPalette;

import java.util.Arrays;
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

        if (this.player != null) {
            this.server.getLogger().info("Client tried to login again.");
            this.session.disconnect();
            return true;
        }

        if (!ServerProtocol.SUPPORTED_PROTOCOL_CODEC.containsKey(packet.getProtocolVersion())) {
            PlayStatusPacket loginFailPacket = new PlayStatusPacket();
            if (packet.getProtocolVersion() > ServerProtocol.LATEST_SUPPORTED_PROTOCOL) {
                loginFailPacket.setStatus(PlayStatusPacket.Status.LOGIN_FAILED_SERVER_OLD);
            } else {
                loginFailPacket.setStatus(PlayStatusPacket.Status.LOGIN_FAILED_CLIENT_OLD);
            }
            this.server.getNetwork().queueClientboundPacket(this.session, loginFailPacket);
            return true;
        }

        this.session.setPacketCodec(ServerProtocol.SUPPORTED_PROTOCOL_CODEC.get(packet.getProtocolVersion()));
        LoginData loginData;
        try {
            loginData = new LoginData(packet.getProtocolVersion(), packet.getChainData(), packet.getSkinData());
        } catch (JsonParseException exception) {
            this.server.getLogger().error("Failed to parse LoginData while handling LoginPacket");
            this.session.disconnect();
            return true;
        }

        if (!loginData.isAuthenticated()) {
            this.session.disconnect("Not Authenticated");
            return true;
        }

        Player player = new Player(this.server, this.session, loginData);
        this.player = player;

        PreLoginEvent event = new PreLoginEvent(player);
        this.server.getPluginManager().callEvent(event);
        if (event.isCancelled()) {
            this.session.disconnect("Failed to connect to server!");
            return true;
        }

        if (this.server.getPlayerCount() >= this.server.getMaximumPlayerCount()) {
            PlayStatusPacket playStatusPacket = new PlayStatusPacket();
            playStatusPacket.setStatus(PlayStatusPacket.Status.FAILED_SERVER_FULL_SUB_CLIENT);
            player.sendPacket(playStatusPacket);
            return true;
        }

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.Status.LOGIN_SUCCESS);
        player.sendPacket(playStatusPacket);

        player.sendPacket(this.getResourcesPacksInfoPacket());
        return true;
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

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {

        if (this.player == null) {
            this.server.getLogger().error("Client requested resource packs before player object was created.");
            this.session.disconnect();
            return true;
        }

        switch (packet.getStatus()) {
            case HAVE_ALL_PACKS:
                // Send required starting packets to client now that they have all resource packs.
                this.sendGameLoginPackets();
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
                if (this.server.getResourcePackManager().arePacksRequired()) {
                    this.session.disconnect(null, true);
                } else {
                    this.sendGameLoginPackets();
                }
                break;
        }
        return true;
    }

    /**
     * Called by server when ResourcePackClientResponse packet requests some packs to be downloaded
     * @param uuid
     * @return
     */
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

    /**
     * Called when the player has passed the resource packs stage and is ready to start the game login process.
     */
    private void sendGameLoginPackets() {
        this.player.sendPacket(this.getStartGamePacket());
        this.player.sendPacket(this.getCreativeContentPacket());
        this.player.sendPacket(this.getBiomesPacket());
    }

    private StartGamePacket getStartGamePacket() {

        SyncedPlayerMovementSettings movementSettings = new SyncedPlayerMovementSettings();
        movementSettings.setMovementMode(AuthoritativeMovementMode.CLIENT);

        StartGamePacket packet = new StartGamePacket();
        packet.setUniqueEntityId(this.player.getId());
        packet.setRuntimeEntityId(this.player.getId());
        packet.setPlayerGameType(GameType.SURVIVAL);
        packet.setPlayerPosition(Vector3f.from(0, 0, 0));
        packet.setRotation(Vector2f.from(0, 0));
        packet.setSeed(0);
        packet.setDimensionId(0);
        packet.setLevelGameType(GameType.SURVIVAL);
        packet.setDifficulty(1);
        packet.setDefaultSpawn(Vector3i.from(0, 0, 0));
        packet.setAchievementsDisabled(true);
        packet.setMultiplayerGame(true);
        packet.setCommandsEnabled(true);
        packet.setTexturePacksRequired(this.server.getResourcePackManager().arePacksRequired());
        packet.setDefaultPlayerPermission(PlayerPermission.MEMBER);
        packet.setVanillaVersion(ServerProtocol.GAME_VERSION);
        packet.setLevelId("");  // TODO: Does this cause any noticeable impact?
        packet.setLevelName(this.server.getMotd());
        packet.setPremiumWorldTemplateId("");
        packet.setPlayerMovementSettings(movementSettings);
        // packet.setEnchantmentSeed(); TODO: Figure out how enchantments are calculated
        packet.setBlockPalette(WorldPalette.getBlockPaletteNbt(player.getLoginData().getProtocolVersion()));
        packet.setItemEntries(Arrays.asList(ItemPalette.getItemStates(player.getLoginData().getProtocolVersion())));
        packet.setMultiplayerCorrelationId(UUID.randomUUID().toString());
        packet.setInventoriesServerAuthoritative(true);

        return packet;
    }

    /**
     * This requires the player to be authenticated before calling the method
     * @return CreativeContentPacket for the player's target protocol version
     */
    private CreativeContentPacket getCreativeContentPacket() {
        CreativeContentPacket packet = new CreativeContentPacket();
        packet.setContents(ItemPalette.getCreativeContents(this.player.getLoginData().getProtocolVersion()));
        return packet;
    }

    /**
     * This requires the player to be authenticated before calling the method
     * @return BiomeDefinitionListPacket for the player's target protocol version
     */
    private BiomeDefinitionListPacket getBiomesPacket() {
        BiomeDefinitionListPacket packet = new BiomeDefinitionListPacket();
        packet.setDefinitions(WorldPalette.getBiomesPaletteNbt(this.player.getLoginData().getProtocolVersion()));
        return packet;
    }

    // Sent after we send the ResourcePackDataInfo packet
    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        if (this.player == null) {
            this.server.getLogger().error("Client requested resource pack chunk before player object was created.");
            this.session.disconnect();
            return true;
        }

        if (!this.server.getResourcePackManager().getPacks().containsKey(packet.getPackId())) {
            this.server.getLogger().error("Invalid resource pack UUID specified while handling ResourcePackChunkRequestPacket.");
            this.session.disconnect();
            return true;
        }

        ResourcePack pack = this.server.getResourcePackManager().getPacks().get(packet.getPackId());
        if (packet.getChunkIndex() < 0 || packet.getChunkIndex() >= pack.getChunkCount()) {
            this.server.getLogger().error("Invalid chunk requested while handling ResourcePackChunkRequestPacket");
            this.session.disconnect();
            return true;
        }

        this.player.sendPacket(this.getResourcePackChunkData(pack, packet.getChunkIndex()));
        return true;
    }

    private ResourcePackChunkDataPacket getResourcePackChunkData(ResourcePack pack, int index) {
        ResourcePackChunkDataPacket chunkDataPacket = new ResourcePackChunkDataPacket();
        chunkDataPacket.setPackId(pack.getUuid());
        chunkDataPacket.setPackVersion(pack.getVersion());
        chunkDataPacket.setChunkIndex(index);
        chunkDataPacket.setProgress((long)index * ResourcePack.CHUNK_LENGTH);  // Where to continue the download process from
        chunkDataPacket.setData(pack.getChunk(index));
        return chunkDataPacket;
    }

}

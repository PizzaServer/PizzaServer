package io.github.willqi.pizzaserver.server.network.handlers;

import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.world.Dimension;
import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.commons.server.Difficulty;
import io.github.willqi.pizzaserver.api.data.ServerOrigin;
import io.github.willqi.pizzaserver.server.level.world.ImplWorld;
import io.github.willqi.pizzaserver.server.network.protocol.data.Experiment;
import io.github.willqi.pizzaserver.api.event.type.player.PlayerPreLoginEvent;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.BaseBedrockPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerMovementType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;
import io.github.willqi.pizzaserver.server.player.ImplPlayer;
import io.github.willqi.pizzaserver.commons.server.Gamemode;
import io.github.willqi.pizzaserver.api.player.data.PermissionLevel;
import io.github.willqi.pizzaserver.commons.utils.Vector2;
import io.github.willqi.pizzaserver.commons.world.WorldType;
import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;

/**
 * Handles preparing/authenticating a client to becoming a valid player
 * Includes Login > Packs > Starting Packets > PlayStatus - Player Spawn
 */
public class LoginPacketHandler extends BaseBedrockPacketHandler {

    private final ImplServer server;
    private final BedrockClientSession session;
    private ImplPlayer player;


    public LoginPacketHandler(ImplServer server, BedrockClientSession session) {
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

        // Check if the protocol version is supported
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

        ImplPlayer player = new ImplPlayer(this.server, this.session, loginPacket);
        this.player = player;

        PlayerPreLoginEvent event = new PlayerPreLoginEvent(player);
        this.server.getEventManager().call(event);
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
        this.session.setPlayer(player);

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.LOGIN_SUCCESS);
        player.sendPacket(playStatusPacket);

        this.server.getNetwork().updatePong();  // Update player count

        ResourcePacksInfoPacket resourcePacksInfoPacket = new ResourcePacksInfoPacket();
        resourcePacksInfoPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        resourcePacksInfoPacket.setResourcePacks(
                new HashSet<>(this.server.getResourcePackManager()
                        .getPacks()
                        .values())
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

                    ResourcePack pack;
                    ResourcePackDataInfoPacket resourcePackDataInfoPacket = new ResourcePackDataInfoPacket();
                    if (this.server.getResourcePackManager().getPacks().containsKey(packInfo.getUuid())) {
                        pack = this.server.getResourcePackManager().getPacks().get(packInfo.getUuid());
                    } else {
                        this.server.getLogger().error("Client requested invalid pack.");
                        this.session.disconnect();
                        return;
                    }
                    resourcePackDataInfoPacket.setType(ResourcePackDataInfoPacket.PackType.RESOURCE_PACK);
                    resourcePackDataInfoPacket.setUUID(pack.getUuid());
                    resourcePackDataInfoPacket.setHash(pack.getHash());
                    resourcePackDataInfoPacket.setVersion(pack.getVersion());
                    resourcePackDataInfoPacket.setChunkCount(pack.getChunkCount());
                    resourcePackDataInfoPacket.setCompressedPackageSize(pack.getDataLength());
                    resourcePackDataInfoPacket.setMaxChunkSize(pack.getMaxChunkLength());
                    this.player.sendPacket(resourcePackDataInfoPacket);

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
                this.player.getServer().getScheduler()
                        .prepareTask(this::sendGameLoginPackets)
                        .setAsynchronous(true)
                        .schedule();
                break;
        }
    }

    @Override
    public void onPacket(ResourcePackChunkRequestPacket packet) {
        if (this.player == null) {
            this.server.getLogger().error("Client requested resource pack chunk before player object was created.");
            this.session.disconnect();
            return;
        }

        ResourcePack pack = this.server.getResourcePackManager().getPacks().getOrDefault(packet.getUUID(), null);
        if (pack == null) {
            this.server.getLogger().error("Invalid resource pack UUID specified while handling ResourcePackChunkRequestPacket.");
            this.session.disconnect();
            return;
        }

        if (packet.getChunkIndex() < 0 || packet.getChunkIndex() >= pack.getChunkCount()) {
            this.server.getLogger().error("Invalid chunk requested while handling ResourcePackChunkRequestPacket");
            this.session.disconnect();
            return;
        }

        // Send the resource pack chunk requested
        ResourcePackChunkDataPacket chunkDataPacket = new ResourcePackChunkDataPacket();
        chunkDataPacket.setUUID(pack.getUuid());
        chunkDataPacket.setChunkIndex(packet.getChunkIndex());
        chunkDataPacket.setChunkProgress((long)packet.getChunkIndex() * pack.getMaxChunkLength());  // Where to continue the download process from
        chunkDataPacket.setData(pack.getChunk(packet.getChunkIndex()));
        this.player.sendPacket(chunkDataPacket);
    }

    /**
     * Called when the client confirms they have all the packs
     */
    private void sendResourcePackStackPacket() {
        ResourcePackStackPacket stackPacket = new ResourcePackStackPacket();
        stackPacket.setForcedToAccept(this.server.getResourcePackManager().arePacksRequired());
        stackPacket.setResourcePacks(new HashSet<>(this.server.getResourcePackManager().getPacks().values()));
        stackPacket.setGameVersion(this.player.getVersion().getVersion());
        stackPacket.setExperiments(Collections.singleton(Experiment.DATA_DRIVEN_ITEMS));
        stackPacket.setExperimentsPreviouslyEnabled(true);
        this.player.sendPacket(stackPacket);
    }

    /**
     * Called when the player has passed the resource packs stage and is ready to start the game login process.
     * This is called asynchronously to load player data from disk without impacting server performance
     */
    private void sendGameLoginPackets() {
        String defaultWorldName = this.player.getServer().getConfig().getDefaultWorldName();
        ImplWorld defaultWorld = this.player.getServer().getLevelManager().getLevelDimension(defaultWorldName, Dimension.OVERWORLD);
        if (defaultWorld == null) {
            this.player.disconnect("Failed to find default world");
            this.player.getServer().getLogger().error("Failed to find a world by the name of " + defaultWorldName);
            return;
        }

        PlayerData data;
        try {
            data = this.getPlayerData(defaultWorld);
        } catch (IOException exception) {
            this.player.disconnect("Failed to retrieve player data");
            this.player.getServer().getLogger().error("Failed to retrieve data of " + this.player.getUUID(), exception);
            return;
        }

        // Apply player data and send remaining packets
        this.player.getServer().getScheduler()
                .prepareTask(() -> {
                    this.player.setPitch(data.getPitch());
                    this.player.setYaw(data.getYaw());
                    this.player.setHeadYaw(data.getYaw());

                    // Get the world they spawn in
                    World world = this.server.getLevelManager().getLevelDimension(data.getLevelName(), data.getDimension());
                    final Location location;
                    if (world == null) { // Was the world deleted? Set it to the default world if so
                        world = defaultWorld;
                        location = new Location(world, world.getSpawnCoordinates());
                    } else {
                        location = new Location(world, data.getPosition());
                    }

                    this.player.sendPacket(this.getStartGamePacket(world, location, new Vector2(data.getPitch(), data.getYaw())));

                    // TODO: Add creative contents to prevent mobile clients from crashing
                    CreativeContentPacket creativeContentPacket = new CreativeContentPacket();
                    this.player.sendPacket(creativeContentPacket);

                    BiomeDefinitionPacket biomeDefinitionPacket = new BiomeDefinitionPacket();
                    biomeDefinitionPacket.setTag(this.player.getVersion().getBiomeDefinitions());
                    this.player.sendPacket(biomeDefinitionPacket);

                    location.getWorld().addEntity(this.player, location);
                    this.session.setPacketHandler(new FullGamePacketHandler(this.player));
                }).schedule();
    }

    /**
     * Construct the StartGamePacket for the player
     * @param world The world the player is spawning in
     * @param position the position the player is spawning at
     * @param direction the direction the player is spawning with
     * @return the start game packet
     */
    private StartGamePacket getStartGamePacket(World world, Vector3 position, Vector2 direction) {
        StartGamePacket startGamePacket = new StartGamePacket();

        // Entity specific
        startGamePacket.setDimension(Dimension.OVERWORLD);
        startGamePacket.setEntityId(this.player.getId());
        startGamePacket.setPlayerGamemode(Gamemode.SURVIVAL);
        startGamePacket.setPlayerPermissionLevel(PermissionLevel.MEMBER);
        startGamePacket.setRuntimeEntityId(this.player.getId());
        startGamePacket.setPlayerRotation(direction);
        startGamePacket.setPlayerSpawn(position);

        // Server
        startGamePacket.setChunkTickRange(this.server.getConfig().getChunkRadius());
        startGamePacket.setCommandsEnabled(true);
        // packet.setCurrentTick(0);       // TODO: get actual tick count
        startGamePacket.setDefaultGamemode(Gamemode.SURVIVAL);
        startGamePacket.setDifficulty(Difficulty.PEACEFUL);
        // packet.setEnchantmentSeed(0);   // TODO: find actual seed
        startGamePacket.setGameVersion(ServerProtocol.GAME_VERSION);
        startGamePacket.setServerName(world.getLevel().getName());
        startGamePacket.setMovementType(PlayerMovementType.CLIENT_AUTHORITATIVE);
        startGamePacket.setServerAuthoritativeBlockBreaking(true);
        startGamePacket.setServerAuthoritativeInventory(true);
        startGamePacket.setResourcePacksRequired(this.server.getResourcePackManager().arePacksRequired());
        startGamePacket.setServerOrigin(ServerOrigin.NONE);
        startGamePacket.setExperiments(Collections.singleton(Experiment.DATA_DRIVEN_ITEMS));
        startGamePacket.setBlockProperties(this.server.getBlockRegistry().getCustomTypes());
        startGamePacket.setItemStates(this.player.getVersion().getItemStates());

        // World
        startGamePacket.setWorldSpawn(world.getSpawnCoordinates());
        startGamePacket.setWorldId(Base64.getEncoder().encodeToString(startGamePacket.getServerName().getBytes(StandardCharsets.UTF_8)));
        startGamePacket.setWorldType(WorldType.INFINITE);

        return startGamePacket;
    }

    /**
     * Retrieve the saved data of the player
     * @param defaultWorld default {@link World} players spawn in if no data is present
     * @return the {@link PlayerData} of the player
     * @throws IOException if an exception occurs while fetching data
     */
    private PlayerData getPlayerData(ImplWorld defaultWorld) throws IOException {
        // Fetch existing player data if present
        Optional<PlayerData> playerData = this.player.getData();

        if (!playerData.isPresent()) {
            playerData = Optional.of(
                    new PlayerData.Builder()
                            .setLevelName(defaultWorld.getLevel().getProvider().getFileName())
                            .setDimension(defaultWorld.getDimension())
                            .setPosition(defaultWorld.getSpawnCoordinates().add(0, 2, 0).toVector3())
                            .setYaw(defaultWorld.getServer().getConfig().getDefaultYaw())
                            .setPitch(defaultWorld.getServer().getConfig().getDefaultPitch())
                            .build()
            );
        }
        return playerData.get();
    }

}

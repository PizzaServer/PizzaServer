package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.network.BedrockClientSession;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.server.player.data.Device;
import io.github.willqi.pizzaserver.server.player.skin.Skin;
import io.github.willqi.pizzaserver.server.utils.Location;
import io.github.willqi.pizzaserver.server.world.chunks.Chunk;
import io.github.willqi.pizzaserver.server.world.chunks.ChunkManager;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class Player extends Entity {

    private final Server server;
    private final BedrockClientSession session;

    private final MinecraftVersion version;
    private final Device device;
    private final String xuid;
    private final UUID uuid;
    private final String username;
    private final String languageCode;
    private Skin skin;

    private int chunkRadius;


    public Player(Server server, BedrockClientSession session, LoginPacket loginPacket) {
        this.server = server;
        this.session = session;

        this.version = session.getVersion();
        this.device = loginPacket.getDevice();
        this.xuid = loginPacket.getXuid();
        this.uuid = loginPacket.getUuid();
        this.username = loginPacket.getUsername();
        this.languageCode = loginPacket.getLanguageCode();
        this.skin = loginPacket.getSkin();

        this.chunkRadius = server.getConfig().getChunkRadius();
    }

    public MinecraftVersion getVersion() {
        return this.version;
    }

    public Device getDevice() {
        return this.device;
    }

    public String getXuid() {
        return this.xuid;
    }

    public UUID getUuid() {
        return this.uuid;
    }

    public String getUsername() {
        return this.username;
    }

    public String getLanguageCode() {
        return this.languageCode;
    }

    public Skin getSkin() {
        return this.skin;
    }

    public void setSkin(Skin newSkin) {
        // TODO: packet level stuff for player skin updates
        this.skin = newSkin;
    }

    public int getChunkRadius() {
        return Math.min(this.chunkRadius, this.server.getConfig().getChunkRadius());
    }

    public void setChunkRadiusRequested(int radius) {
        int oldRadius = this.chunkRadius;
        this.chunkRadius = radius;
        if (this.hasSpawned()) {
            this.updateVisibleChunks(this.location, oldRadius);
        }
    }

    public Server getServer() {
        return this.server;
    }

    public void sendPacket(BedrockPacket packet) {
        this.session.queueSendPacket(packet);
    }

    public void disconnect() {
        this.session.disconnect();
    }

    public void disconnect(String reason) {
        DisconnectPacket disconnectPacket = new DisconnectPacket();
        disconnectPacket.setKickMessage(reason);
        this.sendPacket(disconnectPacket);
        this.session.disconnect();
    }

    /**
     * Called when the server registers that the player is disconnected.
     * It cleans up data for this player
     */
    public void onDisconnect() {
        if (this.hasSpawned()) {
            this.getLocation().getWorld().removeEntity(this);

            // Remove player from chunks they can observe
            for (int chunkX = this.getLocation().getChunkX() - this.getChunkRadius(); chunkX <= this.getLocation().getChunkX() + this.getChunkRadius(); chunkX++) {
                for (int chunkZ = this.getLocation().getChunkZ() - this.getChunkRadius(); chunkZ <= this.getLocation().getChunkZ() + this.getChunkRadius(); chunkZ++) {
                    Chunk chunk = this.getLocation().getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    chunk.despawnFrom(this);
                }
            }
        }
    }

    @Override
    public void setLocation(Location newLocation) {
        Location oldLocation = this.location;
        super.setLocation(newLocation);

        if (this.hasSpawned()) {    // Do we need to send new chunks?
            boolean shouldUpdateChunks = (oldLocation == null) || (oldLocation.getChunkX() != newLocation.getChunkX()) ||
                                            (oldLocation.getChunkZ() != newLocation.getChunkZ()) ||
                                            !(oldLocation.getWorld().equals(this.location.getWorld()));
            if (shouldUpdateChunks) {
                this.updateVisibleChunks(oldLocation, this.chunkRadius);
            }
        }
    }

    @Override
    public void onSpawned() {
        this.updateVisibleChunks(null, this.chunkRadius);

        PlayStatusPacket playStatusPacket = new PlayStatusPacket();
        playStatusPacket.setStatus(PlayStatusPacket.PlayStatus.PLAYER_SPAWN);
        this.sendPacket(playStatusPacket);
    }

    @Override
    public void spawnTo(Player player) {
        // TODO: implement in order for multiplayer to work properly
    }

    /**
     * Request a chunk in the player's world to be sent to the player.
     * This does not send it immediately, but rather requests the server to send the chunk.
     * @param x
     * @param z
     */
    public void sendChunk(int x, int z) {
        ChunkManager chunkManager = this.getLocation().getWorld().getChunkManager();
        if (chunkManager.isChunkLoaded(x, z)) {
            chunkManager.addChunkToPlayerQueue(this, chunkManager.getChunk(x, z));
        } else {
            chunkManager.fetchChunk(x, z).whenComplete((chunk, exception) -> {
                if (exception != null) {
                    Server.getInstance().getLogger().error("Failed to send chunk (" + x + ", " + z + ") to player " + this.getUsername(), exception);
                    return;
                }
                chunkManager.addChunkToPlayerQueue(this, chunk);
            });
        }
    }

    private void sendNetworkChunkPublisher() {
        NetworkChunkPublisherUpdatePacket packet = new NetworkChunkPublisherUpdatePacket();
        packet.setCoordinates(this.getLocation().toVector3i());
        packet.setRadius(this.getChunkRadius() * 16);
        this.sendPacket(packet);
    }

    /**
     * Sends and removes chunks the player can and cannot see
     */
    private void updateVisibleChunks(Location oldLocation, int oldChunkRadius) {
        Set<Chunk> chunksToRemove = new HashSet<>();

        if (oldLocation != null) {
            // What were our previous chunks loaded?
            for (int chunkX = oldLocation.getChunkX() - oldChunkRadius; chunkX <= oldLocation.getChunkX() + oldChunkRadius; chunkX++) {
                for (int chunkZ = oldLocation.getChunkZ() - oldChunkRadius; chunkZ <= oldLocation.getChunkZ() + oldChunkRadius; chunkZ++) {
                    if (oldLocation.getWorld().getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
                        Chunk chunk = oldLocation.getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                        chunksToRemove.add(chunk);
                    }
                }
            }
        }

        // What are our new chunks loaded?
        boolean requiresChunkPublisher = false;
        for (int chunkX = this.getLocation().getChunkX() - this.getChunkRadius(); chunkX <= this.getLocation().getChunkX() + this.getChunkRadius(); chunkX++) {
            for (int chunkZ = this.getLocation().getChunkZ() - this.getChunkRadius(); chunkZ <= this.getLocation().getChunkZ() + this.getChunkRadius(); chunkZ++) {
                if (this.location.getWorld().getChunkManager().isChunkLoaded(chunkX, chunkZ)) {
                    Chunk chunk = this.location.getWorld().getChunkManager().getChunk(chunkX, chunkZ);
                    if (chunksToRemove.remove(chunk)) {
                        continue;   // We don't need to send this chunk
                    }
                }
                requiresChunkPublisher = true;
                this.sendChunk(chunkX, chunkZ);
            }
        }

        // Remove each chunk we shouldn't get packets from
        for (Chunk chunk : chunksToRemove) {
            chunk.despawnFrom(this);
        }

        if (requiresChunkPublisher) {
            this.sendNetworkChunkPublisher();
        }
    }


}

package io.github.willqi.pizzaserver.server.player;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.commons.utils.Tuple;
import io.github.willqi.pizzaserver.server.entity.BaseEntity;
import io.github.willqi.pizzaserver.server.level.world.chunks.ImplChunk;
import io.github.willqi.pizzaserver.server.network.protocol.packets.NetworkChunkPublisherUpdatePacket;

import java.util.HashSet;
import java.util.Set;

public class PlayerChunkManager {

    private final ImplPlayer player;
    private int chunkRadius = 3;


    public PlayerChunkManager(ImplPlayer player) {
        this.player = player;
    }

    public int getChunkRadius() {
        return Math.min(this.chunkRadius, this.player.getServer().getConfig().getChunkRadius());
    }

    public void setChunkRadius(int newRadius) {
        int oldRadius = this.getChunkRadius();
        this.chunkRadius = newRadius;

        if (this.player.hasSpawned()) {
            this.updateVisibleChunks(this.player.getLocation(), oldRadius);
        }
    }

    public Set<Chunk> getVisibleChunks() {
        return null;
    }

    public void updateVisibleChunks(Location oldLocation, int oldRadius) {
        this.sendNetworkChunkPublisher();
        Set<Tuple<Integer, Integer>> chunksVisibleInOldLocation = new HashSet<>();
        if (oldLocation != null) {
            // What were our previous chunks loaded?
            int oldPlayerChunkX = oldLocation.getChunkX();
            int oldPlayerChunkZ = oldLocation.getChunkZ();
            for (int x = -oldRadius; x <= oldRadius; x++) {
                for (int z = -oldRadius; z <= oldRadius; z++) {
                    // Chunk radius is circular
                    int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                    if (oldRadius > distance) {
                        int chunkX = oldPlayerChunkX + x;
                        int chunkZ = oldPlayerChunkZ + z;
                        chunksVisibleInOldLocation.add(new Tuple<>(chunkX, chunkZ));

                        // Entity render distance is handled differently from chunk render distance.
                        // Therefore we have to check the entity render distance every single time we enter a new chunk.
                        // For each old chunk, check if the entities should be despawned from the player.
                        // If it is renderable with the old location, but it is not with the new location, despawn the entities.
                        int oldChunkLocationEntityDistance = (int) Math.round(Math.sqrt(Math.pow(oldLocation.getChunkX() - chunkX, 2) + Math.pow(oldLocation.getChunkZ() - chunkZ, 2)));
                        boolean oldChunkLocationEntitiesVisible = oldChunkLocationEntityDistance < this.player.getWorld().getServer().getConfig().getEntityChunkRenderDistance()
                                && oldChunkLocationEntityDistance < oldRadius;

                        int newChunkLocationEntityDistance = (int) Math.round(Math.sqrt(Math.pow(this.player.getLocation().getChunkX() - chunkX, 2)
                                + Math.pow(this.player.getLocation().getChunkZ() - chunkZ, 2)));
                        boolean newChunkLocationEntitiesVisible = newChunkLocationEntityDistance < this.player.getWorld().getServer().getConfig().getEntityChunkRenderDistance()
                                && newChunkLocationEntityDistance < this.getChunkRadius();

                        if (oldChunkLocationEntitiesVisible && !newChunkLocationEntitiesVisible) {
                            Chunk chunk = this.player.getWorld().getChunk(chunkX, chunkZ);
                            for (Entity entity : chunk.getEntities()) {
                                if (entity.hasSpawnedTo(this.player)) {
                                    entity.despawnFrom(this.player);
                                }
                            }
                        }
                    }
                }
            }
        }

        // What are our new chunks loaded?
        int currentPlayerChunkX = this.player.getLocation().getChunkX();
        int currentPlayerChunkZ = this.player.getLocation().getChunkZ();
        for (int x = -this.getChunkRadius(); x <= this.getChunkRadius(); x++) {
            for (int z = -this.getChunkRadius(); z <= this.getChunkRadius(); z++) {
                // Chunk radius is circular
                int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                if (this.getChunkRadius() > distance) {
                    // Ensure that this chunk is not already visible
                    int chunkX = currentPlayerChunkX + x;
                    int chunkZ = currentPlayerChunkZ + z;
                    if (!chunksVisibleInOldLocation.remove(new Tuple<>(chunkX, chunkZ))) {
                        // Sending a chunk also sends all the entities within render distance in that chunk
                        this.player.getLocation().getWorld().sendPlayerChunk(this.player, chunkX, chunkZ);
                    } else {
                        // This is a chunk already rendered to us. So we need to check the entity render distance for entities in that chunk.
                        // Entity render distance is handled differently from chunk render distance.
                        // Therefore we have to check the entity render distance every single time we enter a new chunk.
                        // For each chunk, check if the entities can now be rendered to this player.
                        // If it was not renderable with the old location, but it can be with the new location, spawn the entities.
                        boolean oldChunkLocationEntitiesVisible = false;
                        if (oldLocation != null) {
                            int oldChunkLocationEntityDistance = (int) Math.round(Math.sqrt(Math.pow(oldLocation.getChunkX() - chunkX, 2) + Math.pow(oldLocation.getChunkZ() - chunkZ, 2)));
                            oldChunkLocationEntitiesVisible = oldChunkLocationEntityDistance < this.player.getWorld().getServer().getConfig().getEntityChunkRenderDistance()
                                    && oldChunkLocationEntityDistance < oldRadius;
                        }

                        int newChunkLocationEntityDistance = (int) Math.round(Math.sqrt(Math.pow(this.player.getLocation().getChunkX() - chunkX, 2)
                                + Math.pow(this.player.getLocation().getChunkZ() - chunkZ, 2)));
                        boolean newChunkLocationEntitiesVisible = newChunkLocationEntityDistance < this.player.getWorld().getServer().getConfig().getEntityChunkRenderDistance()
                                && newChunkLocationEntityDistance < this.getChunkRadius();

                        if (!oldChunkLocationEntitiesVisible && newChunkLocationEntitiesVisible) {
                            Chunk chunk = this.player.getWorld().getChunk(chunkX, chunkZ);
                            for (Entity entity : chunk.getEntities()) {
                                if (((BaseEntity) entity).canBeSpawnedTo(this.player)) {
                                    entity.spawnTo(this.player);
                                }
                            }
                        }
                    }
                }
            }
        }

        // Remove each chunk we shouldn't get packets from
        for (Tuple<Integer, Integer> key : chunksVisibleInOldLocation) {
            ((ImplChunk) this.player.getLocation().getWorld().getChunk(key.getObjectA(), key.getObjectB())).despawnFrom(this.player);
        }
    }

    private void sendNetworkChunkPublisher() {
        NetworkChunkPublisherUpdatePacket packet = new NetworkChunkPublisherUpdatePacket();
        packet.setCoordinates(this.player.getLocation().toVector3i());
        packet.setRadius(this.getChunkRadius() * 16);
        this.player.sendPacket(packet);
    }

    public void onEnterNewChunk(Location oldLocation) {
        this.updateVisibleChunks(oldLocation, this.getChunkRadius());
    }

    public void onSpawned() {
        this.updateVisibleChunks(null, this.getChunkRadius());
    }

    public void onDisconnect() {
        // Remove player from chunks they can observe
        for (int x = -this.getChunkRadius(); x <= this.getChunkRadius(); x++) {
            for (int z = -this.getChunkRadius(); z <= this.getChunkRadius(); z++) {
                // Chunk radius is circular
                int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                if (this.getChunkRadius() > distance) {
                    ImplChunk chunk = (ImplChunk) this.player.getLocation().getWorld().getChunk(this.player.getLocation().getChunkX() + x, this.player.getLocation().getChunkZ() + z);
                    chunk.despawnFrom(this.player);
                }
            }
        }
    }

}

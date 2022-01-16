package io.github.pizzaserver.server.player.manager;

import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.protocol.bedrock.packet.NetworkChunkPublisherUpdatePacket;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.utils.Location;
import io.github.pizzaserver.server.entity.ImplEntity;
import io.github.pizzaserver.server.level.world.ImplWorld;
import io.github.pizzaserver.server.level.world.chunks.ImplChunk;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class PlayerChunkManager {

    private final ImplPlayer player;
    private int chunkRadius = 3;

    private ImplWorld managedWorld = null;
    private Set<Vector2i> currentVisibleChunkCoordinates = Collections.emptySet();


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
            this.updateChunks(this.player.getLocation(), oldRadius);
        }
    }

    public void onChunkChange(Location oldLocation) {
        this.updateChunks(oldLocation, this.getChunkRadius());
    }

    public void onSpawned() {
        this.managedWorld = this.player.getWorld();
        this.updateChunks(null, this.getChunkRadius());
    }

    public void onLocallyInitialized() {
        // Spawn entities in chunks that have been sent to us
        for (Vector2i chunkCoordinates : this.currentVisibleChunkCoordinates) {
            int chunkToPlayerDistance = (int) Math.round(Math.sqrt(
                    Math.pow(this.player.getLocation().getChunkX() - chunkCoordinates.getX(), 2) + Math.pow(
                            this.player.getLocation().getChunkZ() - chunkCoordinates.getY(), 2)));

            boolean isWithinEntityRenderDistance = chunkToPlayerDistance < this.player.getWorld()
                                                                                      .getServer()
                                                                                      .getConfig()
                                                                                      .getEntityChunkRenderDistance();
            boolean isChunkLoaded = this.player.getWorld()
                                               .isChunkLoaded(chunkCoordinates.getX(), chunkCoordinates.getY());

            if (isWithinEntityRenderDistance && isChunkLoaded) {
                for (Entity entity : this.player.getWorld()
                                                .getChunk(chunkCoordinates.getX(), chunkCoordinates.getY())
                                                .getEntities()) {
                    if (((ImplEntity) entity).canBeSpawnedTo(this.player)) {
                        entity.spawnTo(this.player);
                    }
                }
            }
        }
    }

    /**
     * Removes the player from all chunks they were a viewer to.
     */
    public void onDespawn() {
        this.clearVisibleChunks();
        this.managedWorld = null;
    }

    public void onDimensionTransfer() {
        this.clearVisibleChunks();
        this.managedWorld = this.player.getWorld();

        // The dimension transfer requires at MINIMUM the chunks around the new location. So we send those first (otherwise it's rather slow)
        this.sendNetworkChunkPublisher();
        for (int x = this.player.getLocation().getChunkX() - 1; x <= this.player.getLocation().getChunkX() + 1; x++) {
            for (int z = this.player.getLocation().getChunkZ() - 1;
                 z <= this.player.getLocation().getChunkZ() + 1; z++) {
                this.player.getWorld().sendChunk(this.player, x, z);
                this.currentVisibleChunkCoordinates.add(Vector2i.from(x, z));
            }
        }
    }

    public void onDimensionTransferComplete() {
        this.updateChunks(this.player.getLocation(), this.player.getChunkRadius());
    }

    /**
     * Remove the player as a viewer from all chunks this player can currently see.
     */
    private void clearVisibleChunks() {
        for (Vector2i chunkCoordinate : this.currentVisibleChunkCoordinates) {
            this.managedWorld.getChunk(chunkCoordinate.getX(), chunkCoordinate.getY()).despawnFrom(this.player);
        }
        this.currentVisibleChunkCoordinates.clear();
    }

    /**
     * Called when chunks need to be sent/removed or when entities need to be checked for their entity render distance.
     *
     * @param oldLocation old location before their current one
     * @param oldRadius   old chunk radius before their current one (or the existing if the chunk radius was not
     *                    modified)
     */
    private void updateChunks(Location oldLocation, int oldRadius) {
        this.sendNetworkChunkPublisher();

        if (oldLocation != null) {
            this.despawnEntitiesFromOldLocation(oldLocation, oldRadius);
        }
        this.updateVisibleChunks(oldLocation, oldRadius);
    }

    /**
     * Despawns all entities this player can no longer see.
     *
     * @param oldLocation old location where the entities were visible
     * @param oldRadius   old chunk radius or the existing one if it was not changed
     */
    private void despawnEntitiesFromOldLocation(Location oldLocation, int oldRadius) {
        for (Vector2i oldChunkCoordinate : this.currentVisibleChunkCoordinates) {
            int chunkX = oldChunkCoordinate.getX();
            int chunkZ = oldChunkCoordinate.getY();

            // Entity render distance is handled differently from chunk render distance.
            // For each old chunk, check if the entities should be despawned from the player.
            // If it is renderable with the old location, but it is not with the current location, despawn the entities.
            int oldChunkLocationEntityDistance = (int) Math.round(Math.sqrt(
                    Math.pow(oldLocation.getChunkX() - chunkX, 2) + Math.pow(oldLocation.getChunkZ() - chunkZ, 2)));
            boolean oldChunkLocationEntitiesVisible = oldChunkLocationEntityDistance < this.player.getWorld()
                                                                                                  .getServer()
                                                                                                  .getConfig()
                                                                                                  .getEntityChunkRenderDistance()
                    && oldChunkLocationEntityDistance < oldRadius;

            int newChunkLocationEntityDistance = (int) Math.round(Math.sqrt(
                    Math.pow(this.player.getLocation().getChunkX() - chunkX, 2) + Math.pow(
                            this.player.getLocation().getChunkZ() - chunkZ, 2)));
            boolean newChunkLocationEntitiesVisible = newChunkLocationEntityDistance < this.player.getWorld()
                                                                                                  .getServer()
                                                                                                  .getConfig()
                                                                                                  .getEntityChunkRenderDistance()
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

    /**
     * Sends new chunks to the player as needed and tries spawning entities for each visible chunk.
     *
     * @param oldLocation old location before the need for the chunk update
     * @param oldRadius   old radius if it was changed, otherwise the existing one
     */
    private void updateVisibleChunks(Location oldLocation, int oldRadius) {
        Set<Vector2i> previouslyVisibleChunks = new HashSet<>(this.currentVisibleChunkCoordinates);
        Set<Vector2i> visibleChunkCoordinates = new HashSet<>();

        // What are our new chunks loaded?
        int currentPlayerChunkX = this.player.getLocation().getChunkX();
        int currentPlayerChunkZ = this.player.getLocation().getChunkZ();

        for (int x = -this.getChunkRadius(); x <= this.getChunkRadius(); x++) {
            for (int z = -this.getChunkRadius(); z <= this.getChunkRadius(); z++) {
                // Chunk radius is circular
                int distance = (int) Math.round(Math.sqrt((x * x) + (z * z)));
                if (this.getChunkRadius() > distance) {
                    int chunkX = currentPlayerChunkX + x;
                    int chunkZ = currentPlayerChunkZ + z;

                    Vector2i chunkCoordinates = Vector2i.from(chunkX, chunkZ);
                    visibleChunkCoordinates.add(chunkCoordinates);

                    // Ensure that this chunk is not already visible
                    boolean isChunkVisibleToPlayer = previouslyVisibleChunks.remove(chunkCoordinates);
                    if (!isChunkVisibleToPlayer) {
                        this.player.getLocation().getWorld().sendChunk(this.player, chunkX, chunkZ);
                    } else {
                        this.trySpawningEntities(this.player.getWorld().getChunk(chunkX, chunkZ),
                                                 oldLocation,
                                                 oldRadius);
                    }
                }
            }
        }

        this.currentVisibleChunkCoordinates = visibleChunkCoordinates;

        // Remove entities in chunks this player can no longer see
        for (Vector2i key : previouslyVisibleChunks) {
            ((ImplChunk) this.player.getLocation()
                                    .getWorld()
                                    .getChunk(key.getX(), key.getY())).despawnFrom(this.player);
        }
    }

    /**
     * Attempts to spawn all entities in a chunk to the player if possible after moving to a new chunk.
     *
     * @param chunk       chunk to try spawning the entities of
     * @param oldLocation the old location they were in
     * @param oldRadius   old radius if it was changed, otherwise the existing one
     */
    private void trySpawningEntities(Chunk chunk, Location oldLocation, int oldRadius) {
        // For each chunk, check if the entities can now be rendered to this player.
        // If it was not renderable with the old location, but it can be with the new location, spawn the entities.
        boolean oldChunkLocationEntitiesVisible = false;
        if (oldLocation != null) {
            int oldChunkLocationEntityDistance = (int) Math.round(Math.sqrt(
                    Math.pow(oldLocation.getChunkX() - chunk.getX(), 2) + Math.pow(
                            oldLocation.getChunkZ() - chunk.getZ(), 2)));
            oldChunkLocationEntitiesVisible = oldChunkLocationEntityDistance < this.player.getWorld()
                                                                                          .getServer()
                                                                                          .getConfig()
                                                                                          .getEntityChunkRenderDistance()
                    && oldChunkLocationEntityDistance < oldRadius;
        }

        int newChunkLocationEntityDistance = (int) Math.round(Math.sqrt(
                Math.pow(this.player.getLocation().getChunkX() - chunk.getX(), 2) + Math.pow(
                        this.player.getLocation().getChunkZ() - chunk.getZ(), 2)));
        boolean newChunkLocationEntitiesVisible = newChunkLocationEntityDistance < this.player.getWorld()
                                                                                              .getServer()
                                                                                              .getConfig()
                                                                                              .getEntityChunkRenderDistance()
                && newChunkLocationEntityDistance < this.getChunkRadius();

        if (!oldChunkLocationEntitiesVisible && newChunkLocationEntitiesVisible && this.player.isLocallyInitialized()) {
            for (Entity entity : chunk.getEntities()) {
                if (((ImplEntity) entity).canBeSpawnedTo(this.player)) {
                    entity.spawnTo(this.player);
                }
            }
        }
    }

    private void sendNetworkChunkPublisher() {
        NetworkChunkPublisherUpdatePacket packet = new NetworkChunkPublisherUpdatePacket();
        packet.setPosition(this.player.getLocation().toVector3i());
        packet.setRadius(this.getChunkRadius() * 16);
        this.player.sendPacket(packet);
    }
}

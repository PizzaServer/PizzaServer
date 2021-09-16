package io.github.willqi.pizzaserver.api.level.world;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.api.level.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.api.level.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.commons.world.Dimension;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface World {

    Server getServer();

    Level getLevel();

    Dimension getDimension();

    Set<Player> getPlayers();

    ChunkManager getChunkManager();

    Vector3i getSpawnCoordinates();

    void setSpawnCoordinates(Vector3i coordinates);

    Chunk getChunk(int x, int z);

    /**
     * Get the highest block y coordinate at a chunk column
     * @param coordinates chunk column coordinates
     * @return y coordinate
     */
    int getHighestBlockAt(Vector2i coordinates);

    /**
     * Get the highest block y coordinate at a chunk column
     * @param x chunk column x coordinates
     * @param z chunk column z coordinates
     * @return y coordinate
     */
    int getHighestBlockAt(int x, int z);

    Block getBlock(Vector3i position);

    Block getBlock(int x, int y, int z);

    void setBlock(String blockId, Vector3i position);

    void setBlock(String blockId, int x, int y, int z);

    void setBlock(BaseBlockType blockType, Vector3i position);

    void setBlock(BaseBlockType blockType, int x, int y, int z);

    void setBlock(Block block, Vector3i position);

    void setBlock(Block block, int x, int y, int z);

    /**
     * Add a {@link Entity} to this world and spawn it
     * @param entity The {@link Entity} to spawn
     * @param position The position to spawn it in this world
     */
    void addEntity(Entity entity, Vector3 position);

    /**
     * Despawn a {@link Entity} from this world
     * @param entity the {@link Entity} to despawn
     */
    void removeEntity(Entity entity);

    /**
     * Retrieve all entities in this world
     * @return the entities in this world
     */
    Map<Long, Entity> getEntities();

    /**
     * Retrieve an entity in this world
     * @param id the entity id
     * @return the entity if it exists
     */
    Optional<Entity> getEntity(long id);

    default void playSound(WorldSound sound, Vector3 vector3) {
        playSound(sound, vector3, true);
    }

    default void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal) {
        playSound(sound, vector3, isGlobal, false, "");
    }

    default void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal, boolean isBaby, String entityType) {
        playSound(sound, vector3, isGlobal, isBaby, entityType, 0);
    }

    void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal, boolean isBaby, String entityType, int blockID);

}

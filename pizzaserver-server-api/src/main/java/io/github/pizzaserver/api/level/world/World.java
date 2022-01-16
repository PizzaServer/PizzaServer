package io.github.pizzaserver.api.level.world;

import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.SoundEvent;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.chunks.ChunkManager;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.Player;

import java.util.Map;
import java.util.Optional;
import java.util.Set;

public interface World extends ChunkManager {

    Server getServer();

    Level getLevel();

    Dimension getDimension();

    Set<Player> getPlayers();

    Vector3i getSpawnCoordinates();

    void setSpawnCoordinates(Vector3i coordinates);

    /**
     * Get the highest block y coordinate at a chunk column.
     * @param coordinates chunk column coordinates
     * @return y coordinate
     */
    default Block getHighestBlockAt(Vector2i coordinates) {
        return this.getHighestBlockAt(coordinates.getX(), coordinates.getY());
    }

    /**
     * Get the highest block y coordinate at a chunk column.
     * @param x chunk column x coordinates
     * @param z chunk column z coordinates
     * @return y coordinate
     */
    Block getHighestBlockAt(int x, int z);

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param blockCoordinates the chunk block coordinates
     * @return the {@link Block} at these coordinates
     */
    default Block getBlock(Vector3i blockCoordinates) {
        return this.getBlock(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param x x chunk coordinate
     * @param y y chunk coordinate
     * @param z z chunk coordinate
     * @return the {@link Block} at these coordinates
     */
    default Block getBlock(int x, int y, int z) {
        return this.getBlock(x, y, z, 0);
    }

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param blockCoordinates the chunk block coordinates
     * @param layer layer
     * @return the {@link Block} at these coordinates
     */
    default Block getBlock(Vector3i blockCoordinates, int layer) {
        return this.getBlock(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), layer);
    }

    Block getBlock(int x, int y, int z, int layer);

    default Optional<BlockEntity> getBlockEntity(Vector3i blockCoordinates) {
        return this.getBlockEntity(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    Optional<BlockEntity> getBlockEntity(int x, int y, int z);

    /**
     * Set a block in this chunk.
     * @param blockId block id of the block to set here
     * @param blockPosition block position
     */
    default void setBlock(String blockId, Vector3i blockPosition) {
        this.setBlock(blockId, blockPosition, 0);
    }

    /**
     * Set a block in this chunk.
     * @param blockId block id to set here
     * @param blockPosition block position
     * @param layer layer to place the block on
     */
    default void setBlock(String blockId, Vector3i blockPosition, int layer) {
        this.setBlock(blockId, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), layer);
    }

    /**
     * Set a block in a specific layer of this chunk.
     * @param blockId block id to set here
     * @param x block x
     * @param y block y
     * @param z block z
     * @param layer block layer
     */
    default void setBlock(String blockId, int x, int y, int z, int layer) {
        this.setBlock(blockId, 0, x, y, z, layer);
    }

    /**
     * Set a block in a specific layer of this chunk with a specific state.
     * @param blockId block id to set here
     * @param blockState block state
     * @param x block x
     * @param y block y
     * @param z block z
     * @param layer block layer
     */
    default void setBlock(String blockId, int blockState, int x, int y, int z, int layer) {
        this.setBlock(BlockRegistry.getInstance().getBlock(blockId, blockState), x, y, z, layer);
    }

    /**
     * Set a block in this chunk.
     * @param block the {@link Block} to be set here
     * @param blockPosition the chunk position of the block
     */
    default void setBlock(Block block, Vector3i blockPosition) {
        this.setBlock(block, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    /**
     * Set a block in this chunk.
     * @param block the {@link Block} to be set here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    default void setBlock(Block block, int x, int y, int z) {
        this.setBlock(block, x, y, z, 0);
    }

    /**
     * Set a block in a specific layer of this chunk.
     * @param block the {@link Block} to be set here
     * @param blockCoordinates block coordinates
     * @param layer layer
     */
    default void setBlock(Block block, Vector3i blockCoordinates, int layer) {
        this.setBlock(block, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), layer);
    }

    /**
     * Set a block in a specific layer of this chunk.
     * @param block the {@link Block} block to be set here
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param layer layer
     */
    void setBlock(Block block, int x, int y, int z, int layer);

    /**
     * Set a block in this chunk and schedule a block update.
     * @param blockId block id of the block to set here
     * @param blockPosition block position
     */
    default void setAndUpdateBlock(String blockId, Vector3i blockPosition) {
        this.setAndUpdateBlock(blockId, blockPosition, 0);
    }

    /**
     * Set a block in this chunk and schedule a block update.
     * @param blockId block id to set here
     * @param blockPosition block position
     * @param layer layer to place the block on
     */
    default void setAndUpdateBlock(String blockId, Vector3i blockPosition, int layer) {
        this.setAndUpdateBlock(blockId, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ(), layer);
    }

    /**
     * Set a block in this chunk and schedule a block update.
     * @param blockId block id to set here
     * @param x block x
     * @param y block y
     * @param z block z
     * @param layer block layer
     */
    default void setAndUpdateBlock(String blockId, int x, int y, int z, int layer) {
        this.setAndUpdateBlock(blockId, 0, x, y, z, layer);
    }

    /**
     * Set a block in this chunk and schedule a block update.
     * @param blockId block id to set here
     * @param blockState block state
     * @param x block x
     * @param y block y
     * @param z block z
     * @param layer block layer
     */
    default void setAndUpdateBlock(String blockId, int blockState, int x, int y, int z, int layer) {
        this.setAndUpdateBlock(BlockRegistry.getInstance().getBlock(blockId, blockState), x, y, z, layer);
    }

    /**
     * Set a block in this chunk and schedule a block update.
     * @param block the {@link Block} to be set here
     * @param blockPosition the chunk position of the block
     */
    default void setAndUpdateBlock(Block block, Vector3i blockPosition) {
        this.setAndUpdateBlock(block, blockPosition.getX(), blockPosition.getY(), blockPosition.getZ());
    }

    /**
     * Set a block in this chunk and schedule a block update.
     * @param block the {@link Block} to be set here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    default void setAndUpdateBlock(Block block, int x, int y, int z) {
        this.setAndUpdateBlock(block, x, y, z, 0);
    }

    /**
     * Set a block in a specific layer of this chunk.
     * @param block the {@link Block} to be set here
     * @param blockCoordinates block coordinates
     * @param layer layer
     */
    default void setAndUpdateBlock(Block block, Vector3i blockCoordinates, int layer) {
        this.setAndUpdateBlock(block, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), layer);
    }

    /**
     * Set a block in a specific layer of this chunk and schedule a block update.
     * @param block the {@link Block} block to be set here
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param layer layer
     */
    void setAndUpdateBlock(Block block, int x, int y, int z, int layer);

    /**
     * Request a block update on the next chunk tick.
     * @param type block update type
     * @param blockCoordinates coordinates
     * @param ticks minimum amount of ticks to wait before processing the update
     * @return if the block update was queued
     */
    default boolean requestBlockUpdate(BlockUpdateType type, Vector3i blockCoordinates, int ticks) {
        return this.requestBlockUpdate(type,
                                       blockCoordinates.getX(),
                                       blockCoordinates.getY(),
                                       blockCoordinates.getZ(),
                                       ticks);
    }

    /**
     * Request a block update on the next chunk tick.
     * @param type block update type
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param ticks minimum amount of ticks to wait before processing the update
     * @return if the block update was queued
     */
    boolean requestBlockUpdate(BlockUpdateType type, int x, int y, int z, int ticks);

    default void addBlockEvent(Vector3i blockCoordinates, int type, int data) {
        this.addBlockEvent(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), type, data);
    }

    void addBlockEvent(int x, int y, int z, int type, int data);

    /**
     * Add a {@link Item} to the world and spawn it.
     * @param itemStack {@link Item} to spawn as an entity
     * @param position The position to spawn it in this world
     */
    default void addItemEntity(Item itemStack, Vector3f position) {
        this.addItemEntity(itemStack, position, Vector3f.ZERO);
    }

    /**
     * Add a {@link Item} to the world and spawn it.
     * @param itemStack {@link Item} to spawn as an entity
     * @param position The position to spawn it in this world
     * @param velocity The velocity to spawn it with
     */
    void addItemEntity(Item itemStack, Vector3f position, Vector3f velocity);

    /**
     * Add a {@link Item} to the world and spawn it.
     * @param itemEntity {@link EntityItem} to spawn
     * @param position The position to spawn it in this world
     */
    void addItemEntity(EntityItem itemEntity, Vector3f position);

    /**
     * Add a {@link Item} to the world and spawn it.
     * @param itemEntity {@link EntityItem} to spawn
     * @param position The position to spawn it in this world
     * @param velocity The velocity to spawn it with
     */
    void addItemEntity(EntityItem itemEntity, Vector3f position, Vector3f velocity);

    /**
     * Add a {@link Entity} to this world and spawn it.
     * @param entity The {@link Entity} to spawn
     * @param position The position to spawn it in this world
     */
    void addEntity(Entity entity, Vector3f position);

    /**
     * Despawn a {@link Entity} from this world.
     * @param entity the {@link Entity} to despawn
     */
    void removeEntity(Entity entity);

    Map<Long, Entity> getEntities();

    Optional<Entity> getEntity(long id);

    Set<Entity> getEntitiesNear(Vector3f position, int maxDistance);

    void tick();

    boolean isDay();

    int getTime();

    void setTime(int time);

    default void playSound(SoundEvent sound, Vector3f vector3) {
        this.playSound(sound, vector3, false);
    }

    default void playSound(SoundEvent sound, Vector3f vector3, boolean relativeVolumeDisabled) {
        this.playSound(sound, vector3, relativeVolumeDisabled, false, "");
    }

    default void playSound(
            SoundEvent sound,
            Vector3f vector3,
            boolean relativeVolumeDisabled,
            boolean isBaby,
            String entityType) {
        this.playSound(sound, vector3, relativeVolumeDisabled, isBaby, entityType, null);
    }

    void playSound(
            SoundEvent sound,
            Vector3f vector3,
            boolean relativeVolumeDisabled,
            boolean isBaby,
            String entityType,
            Block block);
}

package io.github.pizzaserver.api.level.world.chunks;

import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.BlockRegistry;
import io.github.pizzaserver.api.block.data.BlockUpdateType;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.Watchable;

import java.util.Optional;
import java.util.Set;

/**
 * Represents a 16x16 chunk of land in the world.
 */
public interface Chunk extends Watchable {

    World getWorld();

    /**
     * Retrieve the chunk x.
     * This can be retrieved from a coordinate by dividing the x coordinate by 16
     * @return chunk x
     */
    int getX();

    /**
     * Retrieve the chunk z.
     * This can be retrieved from a coordinate by dividing the z coordinate by 16
     * @return chunk z
     */
    int getZ();

    /**
     * See if a player is within range to being able to see this Chunk.
     * @param player the player we are checking
     * @return if they should be able to see this chunk
     */
    boolean canBeVisibleTo(Player player);

    /**
     * Get the biome type at a specific x and z coordinate of the chunk.
     * @param x x coordinate
     * @param z z coordinate
     * @return biome at the specified x and z coordinate
     */
    byte getBiomeAt(int x, int z);

    /**
     * Retrieve all of the {@link Entity}s that exist in this chunk.
     * @return set of all entities in this chunk
     */
    Set<Entity> getEntities();

    /**
     * Retrieve the highest block coordinate in this chunk column.
     * @param position chunk column coordinates
     * @return y coordinate
     */
    default Block getHighestBlockAt(Vector2i position) {
        return this.getHighestBlockAt(position.getX(), position.getY());
    }

    /**
     * Retrieve the highest block coordinate in this chunk column.
     * @param x chunk column x
     * @param z chunk column z
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

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param x x chunk coordinate
     * @param y y chunk coordinate
     * @param z z chunk coordinate
     * @param layer layer
     * @return the {@link Block} at these coordinates
     */
    Block getBlock(int x, int y, int z, int layer);

    default Optional<BlockEntity<? extends Block>> getBlockEntity(Vector3i blockCoordinates) {
        return this.getBlockEntity(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    Optional<BlockEntity<? extends Block>> getBlockEntity(int x, int y, int z);

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

    default boolean requestBlockUpdate(BlockUpdateType type, Vector3i blockCoordinates, int ticks) {
        return this.requestBlockUpdate(type, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), ticks);
    }

    /**
     * Request a block update on the next chunk tick.
     * @param type block update type
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param ticks minimum ticks required to pass before block update is processed
     * @return if the block update was queued
     */
    boolean requestBlockUpdate(BlockUpdateType type, int x, int y, int z, int ticks);

    default void addBlockEvent(Vector3i blockCoordinates, int type, int data) {
        this.addBlockEvent(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), type, data);
    }

    void addBlockEvent(int x, int y, int z, int type, int data);

    default void addBlockEvent(Player player, Vector3i blockCoordinates, int type, int data) {
        this.addBlockEvent(player, blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), type, data);
    }

    void addBlockEvent(Player player, int x, int y, int z, int type, int data);

    /**
     * Check if this chunk can be closed.
     * @return if the chunk can be closed
     */
    boolean canBeClosed();

    /**
     * Stops operations of this chunk and saves it to the provider asynchronously.
     */
    void close();



}

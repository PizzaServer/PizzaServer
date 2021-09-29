package io.github.willqi.pizzaserver.api.level.world.chunks;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BaseBlockType;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.blocks.Block;
import io.github.willqi.pizzaserver.commons.utils.Vector2;
import io.github.willqi.pizzaserver.commons.utils.Vector2i;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

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
    int getHighestBlockAt(Vector2i position);

    /**
     * Retrieve the highest block coordinate in this chunk column.
     * @param x chunk column x
     * @param z chunk column z
     * @return y coordinate
     */
    int getHighestBlockAt(int x, int z);

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param blockCoordinates the chunk block coordinates
     * @return the {@link Block} at these coordinates
     */
    Block getBlock(Vector3i blockCoordinates);

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param x x chunk coordinate
     * @param y y chunk coordinate
     * @param z z chunk coordinate
     * @return the {@link Block} at these coordinates
     */
    Block getBlock(int x, int y, int z);

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param blockCoordinates the chunk block coordinates
     * @param layer layer
     * @return the {@link Block} at these coordinates
     */
    Block getBlock(Vector3i blockCoordinates, int layer);

    /**
     * Retrieve the {@link Block} at these chunk coordinates.
     * @param x x chunk coordinate
     * @param y y chunk coordinate
     * @param z z chunk coordinate
     * @param layer layer
     * @return the {@link Block} at these coordinates
     */
    Block getBlock(int x, int y, int z, int layer);

    /**
     * Set a block in this chunk.
     * @param blockType the {@link BaseBlockType} of the block that should be created here
     * @param blockPosition the chunk position of the block
     */
    void setBlock(BaseBlockType blockType, Vector3i blockPosition);

    /**
     * Set a block in this chunk.
     * @param block the {@link Block} to be set here
     * @param blockPosition the chunk position of the block
     */
    void setBlock(Block block, Vector3i blockPosition);

    /**
     * Set a block in this chunk.
     * @param blockType the {@link BaseBlockType} of the block that should be created here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    void setBlock(BaseBlockType blockType, int x, int y, int z);

    /**
     * Set a block in this chunk.
     * @param block the {@link Block} to be set here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    void setBlock(Block block, int x, int y, int z);

    /**
     * Set a block in a specific layer of this chunk.
     * @param blockType the {@link BaseBlockType} to be set here
     * @param blockCoordinates block coordinates
     * @param layer layer
     */
    void setBlock(BaseBlockType blockType, Vector3i blockCoordinates, int layer);

    /**
     * Set a block in a specific layer of this chunk.
     * @param blockType the {@link BaseBlockType} to be set here
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param layer layer
     */
    void setBlock(BaseBlockType blockType, int x, int y, int z, int layer);

    /**
     * Set a block in a specific layer of this chunk.
     * @param block the {@link Block} to be set here
     * @param blockCoordinates block coordinates
     * @param layer layer
     */
    void setBlock(Block block, Vector3i blockCoordinates, int layer);

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
     * Check if this chunk can be closed.
     * @return if the chunk can be closed
     */
    boolean canBeClosed();

    /**
     * Stops operations of this chunk and saves it to the provider asynchronously.
     */
    void close();



}

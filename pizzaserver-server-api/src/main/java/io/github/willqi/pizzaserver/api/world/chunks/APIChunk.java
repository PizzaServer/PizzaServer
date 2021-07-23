package io.github.willqi.pizzaserver.api.world.chunks;

import io.github.willqi.pizzaserver.api.entity.APIEntity;
import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.world.APIWorld;
import io.github.willqi.pizzaserver.api.world.blocks.APIBlock;
import io.github.willqi.pizzaserver.api.world.blocks.types.APIBlockType;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

import java.util.Set;

/**
 * Represents a 16x16 chunk of land in the world
 */
public interface APIChunk extends Watchable {

    APIWorld getWorld();

    /**
     * Retrieve the chunk x
     * This can be retrieved from a coordinate by dividing the x coordinate by 16
     * @return chunk x
     */
    int getX();

    /**
     * Retrieve the chunk z
     * This can be retrieved from a coordinate by dividing the z coordinate by 16
     * @return chunk z
     */
    int getZ();

    /**
     * See if a player is within range to being able to see this Chunk
     * @param player the player we are checking
     * @return if they should be able to see this chunk
     */
    boolean canBeVisibleTo(APIPlayer player);

    /**
     * Get the biome type at a specific x and z coordinate of the chunk
     * @param x x coordinate
     * @param z z coordinate
     * @return biome at the specified x and z coordinate
     */
    byte getBiomeAt(int x, int z);

    /**
     * Add a {@link APIEntity} to this chunk
     * @param entity the {@link APIEntity} that now exists in this chunk
     */
    void addEntity(APIEntity entity);

    /**
     * Remove a {@link APIEntity} from this chunk
     * @param entity the {@link APIEntity} that no longer exists in this chunk
     */
    void removeEntity(APIEntity entity);

    /**
     * Retrieve all of the {@link APIEntity}s that exist in this chunk
     * @return {@link Set<APIEntity>}s in this chunk
     */
    Set<APIEntity> getEntities();

    /**
     * Retrieve the {@link APIBlock} at these chunk coordinates
     * @param blockPosition the chunk block coordinates
     * @return the {@link APIBlock} at these coordinates
     */
    APIBlock getBlock(Vector3i blockPosition);

    /**
     * Retrieve the {@link APIBlock} at these chunk coordinates
     * @param x x chunk coordinate
     * @param y y chunk coordinate
     * @param z z chunk coordinate
     * @return the {@link APIBlock} at these coordinates
     */
    APIBlock getBlock(int x, int y, int z);

    /**
     * Set a block in this chunk
     * @param blockType the {@link APIBlockType} of the block that should be created here
     * @param blockPosition the chunk position of the block
     */
    void setBlock(APIBlockType blockType, Vector3i blockPosition);

    /**
     * Set a block in this chunk
     * @param blockType the {@link APIBlockType} of the block that should be created here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    void setBlock(APIBlockType blockType, int x, int y, int z);

    /**
     * Set a block in this chunk
     * @param block the {@link APIBlock} to be set here
     * @param blockPosition the chunk position of the block
     */
    void setBlock(APIBlock block, Vector3i blockPosition);

    /**
     * Set a block in this chunk
     * @param block the {@link APIBlock} to be set here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    void setBlock(APIBlock block, int x, int y, int z);

    /**
     * Stops operations of this chunk
     */
    void close();



}

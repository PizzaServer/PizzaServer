package io.github.pizzaserver.api.level.world;

import com.nukkitx.math.vector.Vector2i;
import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.protocol.bedrock.data.SoundEvent;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.block.types.BaseBlockType;
import io.github.pizzaserver.api.level.world.data.Dimension;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.level.world.chunks.ChunkManager;

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
     * @param blockType the {@link BaseBlockType} of the block that should be created here
     * @param blockPosition the chunk position of the block
     */
    default void setBlock(BaseBlockType blockType, Vector3i blockPosition) {
        this.setBlock(blockType.create(), blockPosition);
    }

    /**
     * Set a block in this chunk.
     * @param blockType the {@link BaseBlockType} of the block that should be created here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    default void setBlock(BaseBlockType blockType, int x, int y, int z) {
        this.setBlock(blockType.create(), x, y, z);
    }

    /**
     * Set a block in a specific layer of this chunk.
     * @param blockType the {@link BaseBlockType} to be set here
     * @param blockCoordinates block coordinates
     * @param layer layer
     */
    default void setBlock(BaseBlockType blockType, Vector3i blockCoordinates, int layer) {
        this.setBlock(blockType.create(), blockCoordinates, layer);
    }


    /**
     * Set a block in a specific layer of this chunk.
     * @param blockType the {@link BaseBlockType} to be set here
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param layer layer
     */
    default void setBlock(BaseBlockType blockType, int x, int y, int z, int layer) {
        this.setBlock(blockType.create(), x, y, z, layer);
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
     * @param blockType the {@link BaseBlockType} of the block that should be created here
     * @param blockPosition the chunk position of the block
     */
    default void setAndUpdateBlock(BaseBlockType blockType, Vector3i blockPosition) {
        this.setAndUpdateBlock(blockType.create(), blockPosition);
    }

    /**
     * Set a block in this chunk and schedule a block update.
     * @param blockType the {@link BaseBlockType} of the block that should be created here
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     */
    default void setAndUpdateBlock(BaseBlockType blockType, int x, int y, int z) {
        this.setAndUpdateBlock(blockType.create(), x, y, z);
    }

    /**
     * Set a block in a specific layer of this chunk and schedule a block update.
     * @param blockType the {@link BaseBlockType} to be set here
     * @param blockCoordinates block coordinates
     * @param layer layer
     */
    default void setAndUpdateBlock(BaseBlockType blockType, Vector3i blockCoordinates, int layer) {
        this.setAndUpdateBlock(blockType.create(), blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), layer);
    }

    /**
     * Set a block in a specific layer of this chunk and schedule a block update.
     * @param blockType the {@link BaseBlockType} to be set here
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param layer layer
     */
    default void setAndUpdateBlock(BaseBlockType blockType, int x, int y, int z, int layer) {
        this.setAndUpdateBlock(blockType.create(), x, y, z, layer);
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
     * @param blockCoordinates coordinates
     * @return if the block update was queued
     */
    default boolean requestBlockUpdate(Vector3i blockCoordinates) {
        return this.requestBlockUpdate(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ());
    }

    /**
     * Request a block update on the next chunk tick.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @return if the block update was queued
     */
    boolean requestBlockUpdate(int x, int y, int z);

    default void addBlockEvent(Vector3i blockCoordinates, int type, int data) {
        this.addBlockEvent(blockCoordinates.getX(), blockCoordinates.getY(), blockCoordinates.getZ(), type, data);
    }

    void addBlockEvent(int x, int y, int z, int type, int data);

    /**
     * Add a {@link ItemStack} to the world and spawn it.
     * @param itemStack {@link ItemStack} to spawn as an entity
     * @param position The position to spawn it in this world
     */
    void addItemEntity(ItemStack itemStack, Vector3f position);

    /**
     * Add a {@link ItemStack} to the world and spawn it.
     * @param itemStack {@link ItemStack} to spawn as an entity
     * @param position The position to spawn it in this world
     * @param velocity The velocity to spawn it with
     */
    void addItemEntity(ItemStack itemStack, Vector3f position, Vector3f velocity);

    /**
     * Add a {@link ItemStack} to the world and spawn it.
     * @param itemEntity {@link ItemEntity} to spawn
     * @param position The position to spawn it in this world
     */
    void addItemEntity(ItemEntity itemEntity, Vector3f position);

    /**
     * Add a {@link ItemStack} to the world and spawn it.
     * @param itemEntity {@link ItemEntity} to spawn
     * @param position The position to spawn it in this world
     * @param velocity The velocity to spawn it with
     */
    void addItemEntity(ItemEntity itemEntity, Vector3f position, Vector3f velocity);

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

    default void playSound(SoundEvent sound, Vector3f vector3, boolean relativeVolumeDisabled, boolean isBaby, String entityType) {
        this.playSound(sound, vector3, relativeVolumeDisabled, isBaby, entityType, null);
    }

    void playSound(SoundEvent sound, Vector3f vector3, boolean relativeVolumeDisabled, boolean isBaby, String entityType, Block block);

}
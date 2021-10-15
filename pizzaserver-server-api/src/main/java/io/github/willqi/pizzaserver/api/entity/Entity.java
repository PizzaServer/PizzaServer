package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.types.EntityType;
import io.github.willqi.pizzaserver.api.entity.types.behaviour.EntityBehaviour;
import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Represents an entity on Minecraft.
 */
public interface Entity extends Watchable {

    long getId();

    EntityType getEntityType();

    float getX();

    float getY();

    float getZ();

    int getFloorX();

    int getFloorY();

    int getFloorZ();

    /**
     * Retrieve the {@link Server} this entity is in.
     * @return {@link Server}
     */
    Server getServer();

    /**
     * Retrieve the {@link Level} this entity is in.
     * @return {@link Level}
     */
    Level getLevel();

    /**
     * Retrieve the {@link World} this entity is in.
     * @return {@link World}
     */
    World getWorld();

    /**
     * Retrieve the {@link Chunk} the entity is in.
     * @return the {@link Chunk}
     */
    Chunk getChunk();

    void teleport(float x, float y, float z);

    void teleport(World world, float x, float y, float z);

    /**
     * Retrieve the {@link Location} of the entity.
     * @return the {@link Location}
     */
    Location getLocation();

    float getHeight();

    float getWidth();

    float getEyeHeight();

    void setDisplayName(String name);

    String getDisplayName();

    /**
     * Retrieve the entity's current movement speed per tick.
     * This is used to determine how far this entity's input can move per tick
     * @return movement speed of an entity
     */
    float getMovementSpeed();

    /**
     * Change the entity's movement speed input per tick.
     * @param movementSpeed new movement speed
     */
    void setMovementSpeed(float movementSpeed);

    float getPitch();

    void setPitch(float pitch);

    float getYaw();

    void setYaw(float yaw);

    float getHeadYaw();

    void setHeadYaw(float headYaw);

    Vector3 getDirectionVector();

    EntityMetaData getMetaData();

    void setMetaData(EntityMetaData metaData);

    Inventory getInventory();

    EntityBehaviour getEntityBehaviour();

    void setEntityBehaviour(EntityBehaviour behaviour);

    /**
     * Called every server tick.
     */
    void tick();

    /**
     * This entity will be shown to the player when the player is within range.
     * If the player is already in range and has not seen the entity, it will be spawned for the player
     * @param player player to show the entity to
     */
    void showTo(Player player);

    /**
     * This entity will not be shown to the player when the player is within range.
     * If the player is already in range and sees the entity, it will be despawned from the player
     * @param player player to hide the entity from
     */
    void hideFrom(Player player);

    /**
     * Checks if this entity can be shown to a player.
     * @param player the player in question
     * @return if the entity is supposed to be hidden from the player
     */
    boolean isHiddenFrom(Player player);

    /**
     * Check if the entity has been spawned into a world yet.
     * @return if the entity has been spawned into a world
     */
    boolean hasSpawned();

    boolean hasSpawnedTo(Player player);

    /**
     * Spawns an entity to a player.
     * @param player the player the entity is being spawned to
     * @return if the entity was spawned
     */
    boolean spawnTo(Player player);

    /**
     * Despawns an entity from a player.
     * @param player the player the entity is being despawned from
     * @return if the entity was despawned
     */
    boolean despawnFrom(Player player);

    void despawn();

}

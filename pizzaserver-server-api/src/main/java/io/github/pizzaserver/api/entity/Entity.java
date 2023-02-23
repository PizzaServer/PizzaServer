package io.github.pizzaserver.api.entity;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.pizzaserver.api.inventory.EntityInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.keychain.EntityKeys;
import io.github.pizzaserver.api.level.Level;
import io.github.pizzaserver.api.level.world.World;
import io.github.pizzaserver.api.level.world.chunks.Chunk;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.*;
import io.github.pizzaserver.commons.data.store.DataStore;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Represents an entity on Minecraft.
 */
public interface Entity extends Watchable, DataStore {

    long getId();

    EntityDefinition getEntityDefinition();

    boolean addComponentGroup(String groupId);

    boolean addComponentGroup(EntityComponentGroup group);

    boolean removeComponentGroup(String groupId);

    boolean removeComponentGroup(EntityComponentGroup group);

    /**
     * Retrieves the most recent entity component from its component groups that matches the component requested.
     * If no component is found, the default component is returned.
     * @param componentClazz component class
     * @return entity component
     */
    <T extends EntityComponent> T getComponent(Class<T> componentClazz);

    /**
     * Check if this entity has any component group with this component defined.
     * @param componentClazz component class
     * @return if it exists
     */
    boolean hasComponent(Class<? extends EntityComponent> componentClazz);

    float getX();

    float getY();

    float getZ();

    int getFloorX();

    int getFloorY();

    int getFloorZ();

    boolean isOnGround();

    /**
     * Retrieve the blocks this entity's bounding box is currently colliding with.
     * @return the blocks this entity is colliding with.
     */
    Set<Block> getCollisionBlocks();

    /**
     * Retrieve the block that this entity's head is in.
     * @return the block the entity's head is in
     */
    Block getHeadBlock();

    void setHome(BlockLocation home);

    Optional<BlockLocation> getHome();

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
     * Retrieve the {@link Chunk} the entity is in.
     * @return the {@link Chunk}
     */
    Chunk getChunk();

    default void teleport(Vector3i position) {
        this.teleport(position, EntityHelper.getBasicRotationFor(this));
    }

    default void teleport(Vector3f position) {
        this.teleport(position.toFloat(), EntityHelper.getBasicRotationFor(this));
    }

    default void teleport(Vector3i position, Vector3f rotation) {
        this.teleport(position.toFloat(), rotation);
    }

    default void teleport(Vector3f position, Vector3f rotation) {
        this.teleport(position.getX(),
                position.getY(),
                position.getZ(),
                rotation.getX(),
                rotation.getY(),
                rotation.getZ());
    }

    default void teleport(float x, float y, float z) {
        this.teleport(this.expect(EntityKeys.WORLD), x, y, z);
    }

    default void teleport(float x, float y, float z, float pitch, float yaw, float headYaw) {
        this.teleport(this.expect(EntityKeys.WORLD), x, y, z, pitch, yaw, headYaw);
    }

    default void teleport(Location location) {
        this.teleport(location.getWorld(),
                location.getX(),
                location.getY(),
                location.getZ(),
                location.getPitch(),
                location.getYaw(),
                location.getHeadYaw());
    }

    default void teleport(World world, float x, float y, float z) {
        this.teleport(
                world, x, y, z,
                this.get(EntityKeys.ROTATION_PITCH).orElse(0f),
                this.get(EntityKeys.ROTATION_YAW).orElse(0f),
                this.get(EntityKeys.ROTATION_HEAD_YAW).orElse(0f)
        );
    }

    void teleport(World world, float x, float y, float z, float pitch, float yaw, float headYaw);

    /**
     * Retrieve the current velocity of the entity.
     * This will return an inaccurate velocity for player entities due to the nature of latency.
     * @return velocity of the entity
     */
    Vector3f getMotion();

    void setMotion(Vector3f velocity);

    /**
     * Retrieve the {@link Location} of the entity.
     * @return the {@link Location}
     */
    Location getLocation();

    BoundingBox getBoundingBox();

    float getEyeHeight();

    void setEyeHeight(float eyeHeight);

    float getBaseOffset();

    void setBaseOffset(float offset);

    /**
     * Retrieves the display name if present.
     * Otherwise it falls back to the name of the entity.
     * @return name of the entity
     */
    String getName();

    HorizontalDirection getHorizontalDirection();

    boolean isAlive();

    Vector3f getDirectionVector();

    boolean isPushable();

    void setPushable(boolean enabled);

    boolean isPistonPushable();

    void setPistonPushable(boolean enabled);

    int getFireTicks();

    void setFireTicks(int ticks);

    List<Item> getLoot();

    void setLoot(List<Item> loot);

    EntityInventory getInventory();

    Optional<BossBar> getBossBar();

    void setBossBar(BossBar bossBar);

    int getArmourPoints();

    int getNoHitTicks();

    void setNoHitTicks(int ticks);

    void hurt(float damage);

    void kill();

    void setDeathMessageEnabled(boolean enabled);

    boolean isDeathMessageEnabled();

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

    boolean canBeSpawnedTo(Player player);

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

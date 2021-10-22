package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.attributes.Attribute;
import io.github.willqi.pizzaserver.api.entity.attributes.AttributeType;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.entity.meta.EntityMetaData;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.level.Level;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.Watchable;
import io.github.willqi.pizzaserver.api.level.world.chunks.Chunk;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

import java.util.Set;

/**
 * Represents an entity on Minecraft.
 */
public interface Entity extends Watchable {

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

    void setHeight(float height);

    float getWidth();

    void setWidth(float width);

    float getEyeHeight();

    String getDisplayName();

    void setDisplayName(String name);

    /**
     * Checks if the entity is can be hurt.
     * @return if the entity can be hurt.
     */
    boolean isVulnerable();

    /**
     * Change the vulnerability status fo the entity.
     * @param vulnerable if the entity is vulnerable and can be hurt.
     */
    void setVulnerable(boolean vulnerable);

    Set<Attribute> getAttributes();

    Attribute getAttribute(AttributeType type);

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

    float getHealth();

    void setHealth(float health);

    float getMaxHealth();

    void setMaxHealth(float maxHealth);

    float getAbsorption();

    void setAbsorption(float absorption);

    float getMaxAbsorption();

    void setMaxAbsorption(float maxAbsorption);

    Vector3 getDirectionVector();

    EntityMetaData getMetaData();

    void setMetaData(EntityMetaData metaData);

    boolean hasAI();

    void setAI(boolean hasAI);

    float getScale();

    void setScale(float scale);

    EntityInventory getInventory();

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

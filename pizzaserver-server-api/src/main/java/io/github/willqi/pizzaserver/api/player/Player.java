package io.github.willqi.pizzaserver.api.player;

import io.github.willqi.pizzaserver.api.entity.HumanEntity;
import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.level.world.data.Dimension;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.utils.TextMessage;

import java.util.Optional;

/**
 * Represents a player on the Minecraft server.
 */
public interface Player extends HumanEntity {

    /**
     * Retrieve the Minecraft version this player is on.
     * @return {@link MinecraftVersion} of the player
     */
    MinecraftVersion getVersion();

    /**
     * Retrieve the language code of the player.
     * @return language code (e.g. en_US)
     */
    String getLanguageCode();

    boolean isLocallyInitialized();

    /**
     * Get the player list of a player.
     * @return player list of a player
     */
    PlayerList getPlayerList();

    PlayerInventory getInventory();

    /**
     * Get the current open inventory of the player if any exist.
     * @return the open inventory if the player has one open
     */
    Optional<Inventory> getOpenInventory();

    /**
     * Try to close our current open inventory.
     * If the inventory was not opened or cannot be closed it will return false
     * @return if the inventory was closed
     */
    boolean closeOpenInventory();

    /**
     * Try to open an inventory.
     * If the inventory cannot be opened (or is already opened) it will return false
     * @param inventory The inventory to open
     * @return if the inventory was opened
     */
    boolean openInventory(Inventory inventory);

    /**
     * Get the amount of food strength the player has.
     * @return value between 0 and 20
     */
    float getFoodLevel();

    /**
     * Set the amount of food strength the player has.
     * @param foodLevel value between 0 and 20
     */
    void setFoodLevel(float foodLevel);

    /**
     * Get the saturation amount of the player.
     * @return saturation of the player
     */
    float getSaturationLevel();

    /**
     * Send the saturation amount of the player.
     * @param saturationLevel saturation of the player
     */
    void setSaturationLevel(float saturationLevel);

    /**
     * Get the percentage a player has filled their experience bar.
     * @return value between 0 and 1
     */
    float getExperience();

    /**
     * Set the percentage a player has filled their experience bar.
     * @param experience value between 0 and1
     */
    void setExperience(float experience);

    /**
     * Get the experience level of the player.
     * @return experience level
     */
    int getExperienceLevel();

    /**
     * Set the experience level of the player.
     * @param experienceLevel experience level
     */
    void setExperienceLevel(int experienceLevel);

    /**
     * Teleport this entity to a position with a dimension transfer screen.
     * If this player is in a dimension of the desired transferDimension, it will not show the dimension transfer screen.
     * @param x x coordinate
     * @param y y coordinate
     * @param z z coordinate
     * @param transferDimension dimension transfer screen to use
     */
    void teleport(float x, float y, float z, Dimension transferDimension);

    /**
     * Teleport this entity to a position with a dimension transfer screen.
     * If this player is in a dimension of the desired transferDimension, it will not show the dimension transfer screen.
     * @param location location to teleport them to
     * @param transferDimension dimension transfer screen to use
     */
    void teleport(Location location, Dimension transferDimension);

    /**
     * Teleport this entity to a position with a dimension transfer screen.
     * If this player is in a dimension of the desired transferDimension, it will not show the dimension transfer screen.
     * @param world the world to teleport them to
     * @param x the x coordinate
     * @param y the y coordinate
     * @param z the z coordinate
     * @param transferDimension dimension transfer screen to use
     */
    void teleport(World world, float x, float y, float z, Dimension transferDimension);

    /**
     * Send a message to this player.
     * @param message the message to send
     */
    void sendMessage(TextMessage message);

    /**
     * Send a text message to this player.
     * @param message the message to send
     */
    void sendMessage(String message);

    /**
     * Send a message originating from another player.
     * @param sender the player who sent this message
     * @param message the message they sent
     */
    void sendPlayerMessage(Player sender, String message);

    /**
     * Retrieve the chunk radius of this player.
     * @return chunk radius
     */
    int getChunkRadius();

    /**
     * Set the chunk radius requested for this player.
     * @param radius chunk radius
     */
    void setChunkRadius(int radius);

    /**
     * If this session is still active.
     * @return if the player is still connected
     */
    boolean isConnected();

    /**
     * Returns the player ping.
     * @return player ping
     */
    long getPing();

    /**
     * Queue a packet to be sent to this player.
     * @param packet the packet to send
     */
    void sendPacket(BaseBedrockPacket packet);

    /**
     * Disconnect the player.
     */
    void disconnect();

    /**
     * Disconnect the player with a reason.
     * @param reason the reason they got disconnected
     */
    void disconnect(String reason);

    /**
     * Change if the player can be automatically saved.
     * @param allowSaving if the player should be saved automatically
     */
    void setAutoSave(boolean allowSaving);

    /**
     * Check if the player can be saved automatically.
     * @return if the player can be saved automatically
     */
    boolean canAutoSave();

    /**
     * Saves the player data to the player data provider.
     * @return if the save was successful
     */
    boolean save();

}

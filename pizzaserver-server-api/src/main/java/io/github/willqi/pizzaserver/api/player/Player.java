package io.github.willqi.pizzaserver.api.player;

import io.github.willqi.pizzaserver.api.entity.inventory.Inventory;
import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.Skin;

import java.util.Optional;
import java.util.UUID;

/**
 * Represents a player on the Minecraft server.
 */
public interface Player extends LivingEntity {

    /**
     * Retrieve the Minecraft version this player is on.
     * @return {@link MinecraftVersion} of the player
     */
    MinecraftVersion getVersion();

    /**
     * Retrieve the device the player is playing on.
     * @return {@link Device} of the player
     */
    Device getDevice();

    /**
     * Retrieve the xuid of the player.
     * This is unique to every player authenticated to Xbox Live
     * @return xuid
     */
    String getXUID();

    /**
     * Retrieve the UUID of the player.
     * @return {@link UUID} of the player
     */
    UUID getUUID();

    /**
     * Retrieve the username of the player.
     * @return username of the player
     */
    String getUsername();

    /**
     * Retrieve the language code of the player.
     * @return language code (e.g. en_US)
     */
    String getLanguageCode();

    /**
     * Retrieve the current {@link Skin} of the player.
     * @return {@link Skin} of the player
     */
    Skin getSkin();

    /**
     * Change the skin of the player with the provided {@link Skin}.
     * @param skin New skin
     */
    void setSkin(Skin skin);

    /**
     * Check if the player is sneaking.
     * @return sneaking status
     */
    boolean isSneaking();

    /**
     * Change if the player is sneaking.
     * @param sneaking sneaking status
     */
    void setSneaking(boolean sneaking);

    /**
     * Get the player list of a player.
     * @return player list of a player
     */
    PlayerList getPlayerList();

    // TODO: move to EntityHuman
    PlayerList.Entry getPlayerListEntry();

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
     * Get the player attributes of the player.
     * @return {@link PlayerAttributes}
     */
    PlayerAttributes getAttributes();

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
    void setChunkRadiusRequested(int radius);

    /**
     * Request the server to send a chunk to the player.
     * @param x chunk x coordinate
     * @param z chunk z coordinate
     */
    void requestSendChunk(int x, int z);

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

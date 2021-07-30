package io.github.willqi.pizzaserver.api.player;

import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.versions.MinecraftVersion;
import io.github.willqi.pizzaserver.api.player.attributes.PlayerAttributes;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.Skin;

import java.util.UUID;

/**
 * Represents a player on the Minecraft server
 */
public interface Player extends LivingEntity {

    /**
     * Retrieve the Minecraft version this player is on
     * @return {@link MinecraftVersion} of the player
     */
    MinecraftVersion getVersion();

    /**
     * Retrieve the device the player is playing on
     * @return {@link Device} of the player
     */
    Device getDevice();

    /**
     * Retrieve the xuid of the player
     * This is unique to every player authenticated to Xbox Live
     * @return xuid
     */
    String getXuid();

    /**
     * Retrieve the UUID of the player
     * @return {@link UUID} of the player
     */
    UUID getUUID();

    /**
     * Retrieve the username of the player
     * @return username of the player
     */
    String getUsername();

    /**
     * Retrieve the language code of the player
     * @return language code (e.g. en_US)
     */
    String getLanguageCode();

    /**
     * Send a text message to this player
     * @param message the message to send
     */
    void sendMessage(String message);

    /**
     * Retrieve the current {@link Skin} of the player
     * @return {@link Skin} of the player
     */
    Skin getSkin();

    /**
     * Change the skin of the player with the provided {@link Skin}
     * @param skin New skin
     */
    void setSkin(Skin skin);

    /**
     * Get the player attributes of the player
     * @return {@link PlayerAttributes}
     */
    PlayerAttributes getAttributes();

    /**
     * Get the amount of food strength the player has
     * @return value between 0 and 20
     */
    float getFoodLevel();

    /**
     * Set the amount of food strength the player has
     * @param foodLevel value between 0 and 20
     */
    void setFoodLevel(float foodLevel);

    /**
     * Get the saturation amount of the player
     * @return saturation of the player
     */
    float getSaturationLevel();

    /**
     * Send the saturation amount of the player
     * @param saturationLevel saturation of the player
     */
    void setSaturationLevel(float saturationLevel);

    /**
     * Get the percentage a player has filled their experience bar
     * @return value between 0 and 1
     */
    float getExperience();

    /**
     * Set the percentage a player has filled their experience bar
     * @param experience value between 0 and1
     */
    void setExperience(float experience);

    /**
     * Get the experience level of the player
     * @return experience level
     */
    int getExperienceLevel();

    /**
     * Set the experience level of the player
     * @param experienceLevel experience level
     */
    void setExperienceLevel(int experienceLevel);

    /**
     * Retrieve the chunk radius of this player
     * @return chunk radius
     */
    int getChunkRadius();

    /**
     * Set the chunk radius requested for this player
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
     * Send a message originating from another APIPlayer
     * @param sender the APIPlayer who sent this message
     * @param message the message they sent
     */
    void sendPlayerMessage(Player sender, String message);

    /**
     * Queue a packet to be sent to this player
     * @param packet the packet to send
     */
    void sendPacket(BedrockPacket packet);

    /**
     * Disconnect the player
     */
    void disconnect();

    /**
     * Disconnect the player with a reason
     * @param reason the reason they got disconnected
     */
    void disconnect(String reason);



}

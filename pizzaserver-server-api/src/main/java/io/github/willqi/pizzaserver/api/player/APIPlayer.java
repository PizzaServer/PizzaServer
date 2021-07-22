package io.github.willqi.pizzaserver.api.player;

import io.github.willqi.pizzaserver.api.network.protocol.versions.APIMinecraftVersion;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.APISkin;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunk;

import java.util.UUID;

/**
 * Represents a player on the Minecraft server
 */
public interface APIPlayer {

    /**
     * Retrieve the Minecraft version this player is on
     * @return {@link APIMinecraftVersion} of the player
     */
    APIMinecraftVersion getVersion();

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
     * Retrieve the current {@link APISkin} of the player
     * @return {@link APISkin} of the player
     */
    APISkin getSkin();

    /**
     * Change the skin of the player with the provided {@link APISkin}
     * @param skin New skin
     */
    void setSkin(APISkin skin);

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
     * Send a message originating from another APIPlayer
     * @param sender the APIPlayer who sent this message
     * @param message the message they sent
     */
    void sendPlayerMessage(APIPlayer sender, String message);



}

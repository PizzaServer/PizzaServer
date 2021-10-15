package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.api.player.data.Device;
import io.github.willqi.pizzaserver.api.player.skin.Skin;

import java.util.UUID;

public interface HumanEntity extends LivingEntity {

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

    PlayerList.Entry getPlayerListEntry();

}

package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.player.PlayerList;
import io.github.willqi.pizzaserver.api.player.skin.Skin;

public interface HumanEntity extends LivingEntity {

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

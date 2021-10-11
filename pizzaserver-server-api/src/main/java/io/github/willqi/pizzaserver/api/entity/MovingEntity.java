package io.github.willqi.pizzaserver.api.entity;

import io.github.willqi.pizzaserver.api.level.world.World;
import io.github.willqi.pizzaserver.api.player.Player;

/**
 * An entity which has the ability to move from its spawned location.
 */
public interface MovingEntity extends Entity {

    void moveTo(float x, float y, float z);

    void teleport(float x, float y, float z);

    void teleport(World world, float x, float y, float z);

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

}

package io.github.willqi.pizzaserver.server.player.playerdata.provider;

import io.github.willqi.pizzaserver.server.player.playerdata.PlayerData;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

public interface PlayerDataProvider {

    /**
     * Save {@link PlayerData} to a provider
     * @param uuid The {@link UUID} of the player we are saving the data of
     * @param data the {@link PlayerData} of the player
     */
    void save(UUID uuid, PlayerData data) throws IOException;

    /**
     * Load the data of a player's {@link UUID}
     * @param uuid the {@link UUID} of the player we are retrieving the data of
     * @return {@link Optional<PlayerData>} player data if any is present
     */
    Optional<PlayerData> load(UUID uuid) throws IOException;

}

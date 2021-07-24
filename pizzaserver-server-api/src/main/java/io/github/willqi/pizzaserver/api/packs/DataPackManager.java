package io.github.willqi.pizzaserver.api.packs;

import io.github.willqi.pizzaserver.api.Server;

import java.util.Map;
import java.util.UUID;

/**
 * Manages resource and behaviour packs
 */
public interface DataPackManager {

    /**
     * Check if data packs are required in order to play on the {@link Server}
     * @return if data packs are required
     */
    boolean arePacksRequired();

    /**
     * Retrieve all resource packs for this server
     * @return all resource packs
     */
    Map<UUID, DataPack> getResourcePacks();

    /**
     * Retrieve all behaviour packs for this server
     * @return all behaviour packs
     */
    Map<UUID, DataPack> getBehaviourPacks();

}

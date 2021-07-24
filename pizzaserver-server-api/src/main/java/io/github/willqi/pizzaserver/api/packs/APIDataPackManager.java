package io.github.willqi.pizzaserver.api.packs;

import java.util.Map;
import java.util.UUID;

/**
 * Manages resource and behaviour packs
 */
public interface APIDataPackManager {

    /**
     * Check if data packs are required in order to play on the {@link io.github.willqi.pizzaserver.api.APIServer}
     * @return if data packs are required
     */
    boolean arePacksRequired();

    /**
     * Retrieve all resource packs for this server
     * @return all resource packs
     */
    Map<UUID, APIDataPack> getResourcePacks();

    /**
     * Retrieve all behaviour packs for this server
     * @return all behaviour packs
     */
    Map<UUID, APIDataPack> getBehaviourPacks();

}

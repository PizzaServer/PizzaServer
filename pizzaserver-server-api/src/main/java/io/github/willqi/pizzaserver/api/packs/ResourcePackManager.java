package io.github.willqi.pizzaserver.api.packs;

import io.github.willqi.pizzaserver.api.Server;

import java.util.Map;
import java.util.UUID;

/**
 * Manages resource packs.
 */
public interface ResourcePackManager {

    /**
     * Check if data packs are required in order to play on the {@link Server}.
     * @return if resource packs are required
     */
    boolean arePacksRequired();

    /**
     * Retrieve all resource packs for this server.
     * @return all resource packs
     */
    Map<UUID, ResourcePack> getPacks();

}

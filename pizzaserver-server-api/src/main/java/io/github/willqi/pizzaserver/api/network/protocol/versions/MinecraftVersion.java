package io.github.willqi.pizzaserver.api.network.protocol.versions;

import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;

/**
 * Represents a specific Minecraft version
 */
public interface MinecraftVersion extends BlockRuntimeMapper {

    int getProtocol();

    /**
     * The game version
     * @return the game version
     */
    String getVersion();

}

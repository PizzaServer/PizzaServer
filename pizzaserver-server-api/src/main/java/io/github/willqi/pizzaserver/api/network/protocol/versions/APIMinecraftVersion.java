package io.github.willqi.pizzaserver.api.network.protocol.versions;

import io.github.willqi.pizzaserver.api.network.protocol.data.APIItemState;
import io.github.willqi.pizzaserver.format.BlockRuntimeMapper;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

import java.util.Set;

/**
 * Represents a specific Minecraft version
 */
public interface APIMinecraftVersion extends BlockRuntimeMapper {

    int getProtocol();

    /**
     * The game version
     * @return the game version
     */
    String getVersion();

    /**
     * All of the {@link APIItemState}s supported in this version
     * @return supported {@link APIItemState}s
     */
    Set<APIItemState> getItemStates();

    /**
     * Retrieve the biomes implemented in this version
     * @return {@link NBTCompound} containing all biomes
     */
    NBTCompound getBiomeDefinitions();

}

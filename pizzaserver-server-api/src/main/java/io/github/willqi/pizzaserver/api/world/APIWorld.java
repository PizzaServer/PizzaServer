package io.github.willqi.pizzaserver.api.world;

import io.github.willqi.pizzaserver.api.APIServer;
import io.github.willqi.pizzaserver.api.entity.APIEntity;
import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.api.world.blocks.APIBlock;
import io.github.willqi.pizzaserver.api.world.blocks.types.APIBlockType;
import io.github.willqi.pizzaserver.api.world.chunks.APIChunkManager;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

import java.util.Set;

public interface APIWorld {

    String getName();

    APIServer getServer();

    Set<APIPlayer> getPlayers();

    APIChunkManager getChunkManager();

    APIBlock getBlock(Vector3i position);

    APIBlock getBlock(int x, int y, int z);

    void setBlock(APIBlockType blockType, Vector3i position);

    void setBlock(APIBlockType blockType, int x, int y, int z);

    void setBlock(APIBlock block, Vector3i position);

    void setBlock(APIBlock block, int x, int y, int z);

    /**
     * Add a {@link APIEntity} to this world and spawn it
     * @param entity The {@link APIEntity} to spawn
     * @param position The position to spawn it in this world
     */
    void addEntity(APIEntity entity, Vector3 position);

    /**
     * Despawn a {@link APIEntity} from this world
     * @param entity the {@link APIEntity} to despawn
     */
    void removeEntity(APIEntity entity);

}

package io.github.willqi.pizzaserver.api.world;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.world.blocks.Block;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.api.world.chunks.ChunkManager;
import io.github.willqi.pizzaserver.api.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;

import java.util.Set;

public interface World {

    String getName();

    Server getServer();

    Set<Player> getPlayers();

    ChunkManager getChunkManager();

    Block getBlock(Vector3i position);

    Block getBlock(int x, int y, int z);

    void setBlock(BlockType blockType, Vector3i position);

    void setBlock(BlockType blockType, int x, int y, int z);

    void setBlock(Block block, Vector3i position);

    void setBlock(Block block, int x, int y, int z);

    /**
     * Add a {@link Entity} to this world and spawn it
     * @param entity The {@link Entity} to spawn
     * @param position The position to spawn it in this world
     */
    void addEntity(Entity entity, Vector3 position);

    /**
     * Despawn a {@link Entity} from this world
     * @param entity the {@link Entity} to despawn
     */
    void removeEntity(Entity entity);

    default void playSound(WorldSound sound, Vector3 vector3) {
        playSound(sound, vector3, true);
    }

    default void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal) {
        playSound(sound, vector3, isGlobal, false, "");
    }

    default void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal, boolean isBaby, String entityType) {
        playSound(sound, vector3, isGlobal, isBaby, entityType, 0);
    }

    void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal, boolean isBaby, String entityType, int blockID);

}

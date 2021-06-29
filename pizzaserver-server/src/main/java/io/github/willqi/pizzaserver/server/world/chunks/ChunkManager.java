package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.World;

public class ChunkManager {

    private final World world;

    private final ChunkSerializerThread serializer = new ChunkSerializerThread(this);


    public ChunkManager(World world) {
        this.world = world;
    }

    public boolean isChunkLoaded(int x, int z) {
        return false;
    }

    public Chunk getChunk(int x, int z) {
        return null;
    }

    public Chunk fetchChunk(int x, int z) {
        return null;
    }

    public void addChunkToPlayerQueue(Player player, int x, int z) {
        this.serializer.addChunkToPlayerQueue(player, x, z);
    }

}

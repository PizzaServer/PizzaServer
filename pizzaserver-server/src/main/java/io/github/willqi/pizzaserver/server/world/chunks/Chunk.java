package io.github.willqi.pizzaserver.server.world.chunks;

import io.github.willqi.pizzaserver.mcworld.world.chunks.subchunks.BedrockSubChunk;
import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.entity.Entity;
import io.github.willqi.pizzaserver.server.network.protocol.packets.LevelChunkPacket;
import io.github.willqi.pizzaserver.server.player.Player;
import io.github.willqi.pizzaserver.server.world.World;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Chunk {

    private final List<BedrockSubChunk> subChunks;
    private final byte[] biomeData;

    private final World world;
    private final int x;
    private final int z;

    private final Set<Entity> entities = new HashSet<>();
    private final Set<Player> spawnedTo = new HashSet<>();

    protected Chunk(World world, int x, int z, List<BedrockSubChunk> subChunks, byte[] biomeData) {
        this.world = world;
        this.x = x;
        this.z = z;
        this.subChunks = subChunks;
        this.biomeData = biomeData;
    }

    public World getWorld() {
        return this.world;
    }

    public int getX() {
        return this.x;
    }

    public int getZ() {
        return this.z;
    }

    public void addEntity(Entity entity) {
        this.entities.add(entity);
    }

    public void removeEntity(Entity entity) {
        this.entities.remove(entity);
    }

    public byte[] getBiomeData() {
        return this.biomeData;
    }

    /**
     * Send this chunk to a player
     * @param player
     */
    public void spawnTo(Player player) {
        // Find the lowest from the top empty subchunk
        int subChunkCount = this.subChunks.size() - 1;
        for (; subChunkCount >= 0; subChunkCount--) {
            BedrockSubChunk subChunk = this.subChunks.get(subChunkCount);
            if (subChunk.getLayers().size() > 0) {
                break;
            }
        }
        subChunkCount++;

        // Write all subchunks
        ByteBuf packetData = ByteBufAllocator.DEFAULT.buffer();
        for (int subChunkIndex = 0; subChunkIndex < subChunkCount; subChunkIndex++) {
            BedrockSubChunk subChunk = this.subChunks.get(subChunkIndex);
            try {
                byte[] subChunkSerialized = subChunk.serializeForNetwork(player.getVersion());
                packetData.writeBytes(subChunkSerialized);
            } catch (IOException exception) {
                Server.getInstance().getLogger().error("Failed to serialize subchunk (x: " + this.getX() + " z: " + this.getZ() + " index: " + subChunkCount + ")");
                return;
            }
        }
        packetData.writeBytes(this.getBiomeData());
        packetData.writeByte(0);    // edu feature or smth

        byte[] data = new byte[packetData.readableBytes()];
        packetData.readBytes(data);
        packetData.release();

        // TODO: Supposedly tile entities are also packaged here
        LevelChunkPacket levelChunkPacket = new LevelChunkPacket();
        levelChunkPacket.setX(this.getX());
        levelChunkPacket.setZ(this.getZ());
        levelChunkPacket.setSubChunkCount(subChunkCount);
        levelChunkPacket.setData(data);
        player.sendPacket(levelChunkPacket);

        this.spawnedTo.add(player);
    }

    public void despawnFrom(Player player) {
        // TODO: despawn all entities
        this.spawnedTo.remove(player);

        // Should we close this chunk
        if (this.spawnedTo.size() == 0) {
            this.getWorld().getChunkManager().unloadChunk(this.getX(), this.getZ());
        }
    }

    public void close() {}

    @Override
    public int hashCode() {
        return 7 + (37 * this.getX()) + (37 * this.getZ()) + (37 * this.getWorld().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Chunk) {
            Chunk otherChunk = (Chunk)obj;
            return (otherChunk.getX() == this.getX()) && (otherChunk.getZ() == this.getZ()) && (otherChunk.getWorld().equals(this.getWorld()));
        }
        return false;
    }


    public static class Builder {

        private List<BedrockSubChunk> subChunks = Collections.emptyList();
        private byte[] biomeData = new byte[256];

        private World world;
        private int x;
        private int z;


        public Builder setSubChunks(List<BedrockSubChunk> subChunks) {
            this.subChunks = subChunks;
            return this;
        }

        public Builder setBiomeData(byte[] biomeData) {
            this.biomeData = biomeData;
            return this;
        }

        public Builder setWorld(World world) {
            this.world = world;
            return this;
        }

        public Builder setX(int x) {
            this.x = x;
            return this;
        }

        public Builder setZ(int z) {
            this.z = z;
            return this;
        }

        public Chunk build() {
            return new Chunk(this.world, this.x, this.z, this.subChunks, this.biomeData);
        }


    }

}

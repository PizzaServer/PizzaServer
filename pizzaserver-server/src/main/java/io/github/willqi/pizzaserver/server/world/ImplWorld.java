package io.github.willqi.pizzaserver.server.world;

import io.github.willqi.pizzaserver.api.Server;
import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.Location;
import io.github.willqi.pizzaserver.api.world.World;
import io.github.willqi.pizzaserver.api.world.blocks.Block;
import io.github.willqi.pizzaserver.api.world.blocks.types.BlockType;
import io.github.willqi.pizzaserver.api.world.data.WorldSound;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.entity.BaseEntity;
import io.github.willqi.pizzaserver.server.event.type.world.WorldSoundEvent;
import io.github.willqi.pizzaserver.server.network.protocol.packets.WorldSoundEventPacket;
import io.github.willqi.pizzaserver.server.utils.ImplLocation;
import io.github.willqi.pizzaserver.server.world.blocks.ImplBlock;
import io.github.willqi.pizzaserver.server.world.chunks.ImplChunkManager;
import io.github.willqi.pizzaserver.server.world.providers.BaseWorldProvider;

import java.io.Closeable;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ImplWorld implements Closeable, World {

    private final Server server;

    private final ImplChunkManager chunkManager = new ImplChunkManager(this);
    private final BaseWorldProvider provider;

    private final Set<Player> players = new HashSet<>();


    public ImplWorld(Server server, BaseWorldProvider provider) throws IOException {
        this.server = server;
        this.provider = provider;
    }

    @Override
    public String getName() {
        return this.getProvider().getName();
    }

    @Override
    public Server getServer() {
        return this.server;
    }

    @Override
    public Set<Player> getPlayers() {
        return Collections.unmodifiableSet(this.players);
    }

    @Override
    public ImplChunkManager getChunkManager() {
        return this.chunkManager;
    }

    public BaseWorldProvider getProvider() {
        return this.provider;
    }

    @Override
    public Block getBlock(Vector3i position) {
        return this.getBlock(position.getX(), position.getY(), position.getZ());
    }

    @Override
    public Block getBlock(int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        return this.getChunkManager().getChunk(chunkX, chunkZ).getBlock(x % 16, y, z % 16);
    }

    @Override
    public void setBlock(BlockType blockType, Vector3i position) {
        this.setBlock(new ImplBlock(blockType), position);
    }

    @Override
    public void setBlock(BlockType blockType, int x, int y, int z) {
        this.setBlock(new ImplBlock(blockType), x, y, z);
    }

    @Override
    public void setBlock(Block block, Vector3i position) {
        this.setBlock(block, position.getX(), position.getY(), position.getZ());
    }

    @Override
    public void setBlock(Block block, int x, int y, int z) {
        int chunkX = x / 16;
        int chunkZ = z / 16;
        this.getChunkManager().getChunk(chunkX, chunkZ).setBlock(block, x % 16, y, z % 16);
    }

    @Override
    public void addEntity(Entity entity, Vector3 position) {
        if (entity.hasSpawned()) {
            throw new IllegalStateException("This entity has already been spawned");
        }

        Location location = new ImplLocation(this, position);
        entity.setLocation(location);
        if (entity instanceof Player) {
            this.players.add((Player)entity);
        }
        ((BaseEntity)entity).onSpawned();
    }

    @Override
    public void removeEntity(Entity entity) {
        if (!entity.hasSpawned()) {
            throw new IllegalStateException("This entity has not been spawned");
        }
        entity.getLocation().getChunk().removeEntity(entity);
        if (entity instanceof Player) {
            this.players.remove(entity);
        }
    }

    @Override
    public void playSound(WorldSound sound, Vector3 vector3, boolean isGlobal, boolean isBaby, String entityType, int blockID) {
        WorldSoundEventPacket packet = new WorldSoundEventPacket();
        packet.setSound(sound);
        packet.setVector3(vector3);
        packet.setGlobal(isGlobal);
        packet.setBaby(isBaby);
        packet.setEntityType(entityType);
        packet.setBlockID(blockID);
        WorldSoundEvent event = new WorldSoundEvent(this, packet);
        getServer().getEventManager().call(event);
        if(!event.isCancelled()) {
            for(Player player : getPlayers()) {
                player.sendPacket(packet);
            }
        }
    }

    @Override
    public void close() throws IOException {
        this.getChunkManager().close();
        this.getProvider().close();
    }

    @Override
    public int hashCode() {
        return 43 + (43 * this.getName().hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ImplWorld) {
            ImplWorld otherWorld = (ImplWorld)obj;
            return otherWorld.getName().equals(this.getName());
        }
        return false;
    }
}

package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public abstract class MinecraftVersion {

    public abstract int getProtocol();

    public abstract PacketRegistry getPacketRegistry();

    public NBTCompound getBiomeDefinitions() {
        return null;
    }

}

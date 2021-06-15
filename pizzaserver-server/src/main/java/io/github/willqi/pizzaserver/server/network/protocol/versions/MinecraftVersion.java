package io.github.willqi.pizzaserver.server.network.protocol.versions;

import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.streams.varint.VarIntDataInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import io.github.willqi.pizzaserver.server.Server;

import java.io.IOException;

public abstract class MinecraftVersion {

    private final NBTCompound biomesDefinitions;

    public MinecraftVersion() {

        // Parse biome_definitions.nbt
        try (NBTInputStream biomesNBTStream = new NBTInputStream(
                new VarIntDataInputStream(
                        Server.getInstance().getClass().getResourceAsStream("/protocol/v" + this.getProtocol() + "/biome_definitions.nbt")
                )
        )) {
            this.biomesDefinitions = biomesNBTStream.readCompound();
        } catch (IOException exception) {
            throw new RuntimeException("Failed to read v" + this.getProtocol() + "'s biome_definitions.nbt file", exception);
        }

    }


    public abstract int getProtocol();

    public abstract PacketRegistry getPacketRegistry();

    public NBTCompound getBiomeDefinitions() {
        return this.biomesDefinitions;
    }

}

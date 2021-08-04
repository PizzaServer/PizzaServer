package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

/**
 * Contains all the biomes that are possible on the server
 */
public class BiomeDefinitionPacket extends BaseBedrockPacket {

    public static final int ID = 0x7a;

    private NBTCompound tag;


    public BiomeDefinitionPacket() {
        super(ID);
    }

    public NBTCompound getTag() {
        return this.tag;
    }

    public void setTag(NBTCompound tag) {
        this.tag = tag;
    }

}

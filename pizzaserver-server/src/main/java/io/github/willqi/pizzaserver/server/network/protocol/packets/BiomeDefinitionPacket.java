package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;

public class BiomeDefinitionPacket extends BedrockPacket {

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

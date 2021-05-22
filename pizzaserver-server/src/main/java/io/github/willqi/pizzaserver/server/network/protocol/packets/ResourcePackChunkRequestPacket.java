package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;

public class ResourcePackChunkRequestPacket extends BedrockPacket {

    public static final int ID = 0x54;

    private PackInfo packInfo;
    private int chunkIndex;

    public ResourcePackChunkRequestPacket() {
        super(ID);
    }

    public PackInfo getPackInfo() {
        return this.packInfo;
    }

    public void setPackInfo(PackInfo packInfo) {
        this.packInfo = packInfo;
    }

    public int getChunkIndex() {
        return this.chunkIndex;
    }

    public void setChunkIndex(int index) {
        this.chunkIndex = index;
    }

}

package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;

public class ResourcePackResponsePacket extends BedrockPacket {

    public static final int ID = 0x08;

    private ResourcePackStatus status;
    private PackInfo[] packs;

    public ResourcePackResponsePacket() {
        super(ID);
    }

    public ResourcePackStatus getStatus() {
        return this.status;
    }

    public void setStatus(ResourcePackStatus status) {
        this.status = status;
    }

    public PackInfo[] getPacksRequested() {
        return this.packs;
    }

    public void setPacksRequested(PackInfo[] packs) {
        this.packs = packs;
    }


    public enum ResourcePackStatus {
        NONE,
        REFUSED,
        SEND_PACKS,
        HAVE_ALL_PACKS,
        COMPLETED
    }

}

package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;

import java.util.Collection;
import java.util.HashSet;

public class ResourcePackResponsePacket extends BedrockPacket {

    public static final int ID = 0x08;

    private ResourcePackStatus status;
    private Collection<PackInfo> packs = new HashSet<>();

    public ResourcePackResponsePacket() {
        super(ID);
    }

    public ResourcePackStatus getStatus() {
        return this.status;
    }

    public void setStatus(ResourcePackStatus status) {
        this.status = status;
    }

    public Collection<PackInfo> getPacksRequested() {
        return this.packs;
    }

    public void setPacksRequested(Collection<PackInfo> packs) {
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

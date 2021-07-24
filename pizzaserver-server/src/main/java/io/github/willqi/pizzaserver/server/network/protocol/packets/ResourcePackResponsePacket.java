package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;

import java.util.HashSet;
import java.util.Set;

public class ResourcePackResponsePacket extends BedrockNetworkPacket {

    public static final int ID = 0x08;

    private ResourcePackStatus status;
    private Set<PackInfo> packs = new HashSet<>();


    public ResourcePackResponsePacket() {
        super(ID);
    }

    public ResourcePackStatus getStatus() {
        return this.status;
    }

    public void setStatus(ResourcePackStatus status) {
        this.status = status;
    }

    public Set<PackInfo> getPacksRequested() {
        return this.packs;
    }

    public void setPacksRequested(Set<PackInfo> packs) {
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

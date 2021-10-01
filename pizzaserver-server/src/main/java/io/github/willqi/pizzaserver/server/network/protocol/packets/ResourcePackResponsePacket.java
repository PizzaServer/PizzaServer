package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.PackInfo;

import java.util.HashSet;
import java.util.Set;

/**
 * Sent by the client to notify the server as to its current status for resource packs.
 */
public class ResourcePackResponsePacket extends BaseBedrockPacket {

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

        NONE,   // No idea what this is

        /**
         * When the client refuses to accept the packs.
         */
        REFUSED,

        /**
         * Request the server to send resource packs it is missing.
         */
        SEND_PACKS,

        /**
         * Called when the client has all the resource packs.
         */
        HAVE_ALL_PACKS,

        /**
         * Called when the client is finished the resource pack process.
         */
        COMPLETED
    }

}

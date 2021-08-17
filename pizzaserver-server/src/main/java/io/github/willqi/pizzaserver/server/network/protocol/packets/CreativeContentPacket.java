package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;

import java.util.ArrayList;
import java.util.List;

/**
 * Sent with all of the creative items in the creative inventory
 */
public class CreativeContentPacket extends BaseBedrockPacket {

    public static final int ID = 0x91;

    private List<NetworkItemStackData> entries = new ArrayList<>();


    public CreativeContentPacket() {
        super(ID);
    }

    public List<NetworkItemStackData> getEntries() {
        return this.entries;
    }

    public void setEntries(List<NetworkItemStackData> entries) {
        this.entries = entries;
    }

}

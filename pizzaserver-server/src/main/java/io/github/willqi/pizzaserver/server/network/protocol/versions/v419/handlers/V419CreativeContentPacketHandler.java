package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.CreativeContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419CreativeContentPacketHandler extends BaseProtocolPacketHandler<CreativeContentPacket> {

    @Override
    public void encode(CreativeContentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarInt(packet.getEntries().size());
        for (NetworkItemStackData entry : packet.getEntries()) {
            buffer.writeUnsignedVarInt(entry.getRuntimeId());
            buffer.writeItem(entry);
        }
    }

}

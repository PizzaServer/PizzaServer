package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.packets.UpdateBlockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419MinecraftVersion;

import java.util.HashMap;
import java.util.Map;

public class V419UpdateBlockPacketHandler extends BaseProtocolPacketHandler<UpdateBlockPacket> {

    @Override
    public void encode(UpdateBlockPacket packet, BasePacketBuffer buffer) {
        buffer.writeVector3i(packet.getBlockCoordinates());

        int blockRuntimeId = buffer.getVersion()
                .getBlockRuntimeId(packet.getBlock().getBlockType().getBlockId(), packet.getBlock().getBlockState());
        buffer.writeUnsignedVarInt(blockRuntimeId);

        int flags = 0;
        for (UpdateBlockPacket.Flag flag : packet.getFlags()) {
            flags ^= 1 << flag.ordinal();
        }
        buffer.writeUnsignedVarInt(flags);

        buffer.writeUnsignedVarInt(packet.getLayer());
    }

}

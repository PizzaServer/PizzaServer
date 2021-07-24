package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.ServerProtocol;
import io.github.willqi.pizzaserver.server.network.protocol.packets.UpdateBlockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.V419MinecraftVersion;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class V419UpdateBlockPacketHandler extends BaseProtocolPacketHandler<UpdateBlockPacket> {

    protected final Map<UpdateBlockPacket.Flag, Integer> flagValues = new HashMap<UpdateBlockPacket.Flag, Integer>(){
        {
            this.put(UpdateBlockPacket.Flag.NEIGHBOURS, 0x01);
            this.put(UpdateBlockPacket.Flag.NETWORK, 0x02);
            this.put(UpdateBlockPacket.Flag.NO_GRAPHIC, 0x04);
            this.put(UpdateBlockPacket.Flag.PRIORITY, 0x08);
        }
    };

    @Override
    public void encode(UpdateBlockPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        VarInts.writeInt(buffer, packet.getBlockCoordinates().getX());
        VarInts.writeUnsignedInt(buffer, packet.getBlockCoordinates().getY());
        VarInts.writeInt(buffer, packet.getBlockCoordinates().getZ());

        int blockRuntimeId = ServerProtocol.VERSIONS
                .get(V419MinecraftVersion.PROTOCOL)
                .getBlockRuntimeId(packet.getBlock().getBlockType().getBlockId(), packet.getBlock().getBlockState());
        VarInts.writeUnsignedInt(buffer, blockRuntimeId);

        int flags = 0;
        for (UpdateBlockPacket.Flag flag : packet.getFlags()) {
            flags ^= 1 << this.flagValues.get(flag);
        }
        VarInts.writeUnsignedInt(buffer, flags);

        VarInts.writeUnsignedInt(buffer, packet.getLayer());
    }

}

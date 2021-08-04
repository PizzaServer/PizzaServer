package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackDataInfoPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.HashMap;
import java.util.Map;

public class V419ResourcePackDataInfoPacketHandler extends BaseProtocolPacketHandler<ResourcePackDataInfoPacket> {

    protected final Map<ResourcePackDataInfoPacket.PackType, Integer> packType = new HashMap<ResourcePackDataInfoPacket.PackType, Integer>(){
        {
            this.put(ResourcePackDataInfoPacket.PackType.INVALID, 0);
            this.put(ResourcePackDataInfoPacket.PackType.BEHAVIOR_PACK, 4);
            this.put(ResourcePackDataInfoPacket.PackType.RESOURCE_PACK, 6);
        }
    };

    @Override
    public void encode(ResourcePackDataInfoPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        helper.writeString(packet.getUUID().toString(), buffer);
        buffer.writeIntLE(packet.getMaxChunkSize());
        buffer.writeIntLE(packet.getChunkCount());
        buffer.writeLongLE(packet.getCompressedPackageSize());
        helper.writeByteArray(packet.getHash(), buffer);
        buffer.writeBoolean(packet.isPremium());
        buffer.writeByte(this.packType.getOrDefault(packet.getType(), this.packType.get(ResourcePackDataInfoPacket.PackType.INVALID)));
    }
}

package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.MobEquipmentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419MobEquipmentPacketHandler extends BaseProtocolPacketHandler<MobEquipmentPacket> {

    @Override
    public MobEquipmentPacket decode(BasePacketBuffer buffer) {
        MobEquipmentPacket mobEquipmentPacket = new MobEquipmentPacket();
        mobEquipmentPacket.setEntityRuntimeId(buffer.readUnsignedVarLong());
        mobEquipmentPacket.setNetworkItemStackData(buffer.readItem());
        mobEquipmentPacket.setSlot(buffer.readByte());
        mobEquipmentPacket.setHotbarSlot(buffer.readByte());
        mobEquipmentPacket.setInventoryId(buffer.readByte());
        return mobEquipmentPacket;
    }

    @Override
    public void encode(MobEquipmentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeItem(packet.getNetworkItemStackData());
        buffer.writeByte(packet.getSlot());
        buffer.writeByte(packet.getHotbarSlot());
        buffer.writeByte(packet.getInventoryId());
    }

}

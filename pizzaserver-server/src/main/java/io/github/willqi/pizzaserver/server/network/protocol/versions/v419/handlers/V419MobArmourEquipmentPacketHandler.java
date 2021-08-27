package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.packets.MobArmourEquipmentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

public class V419MobArmourEquipmentPacketHandler extends BaseProtocolPacketHandler<MobArmourEquipmentPacket> {

    @Override
    public void encode(MobArmourEquipmentPacket packet, BasePacketBuffer buffer) {
        buffer.writeUnsignedVarLong(packet.getEntityRuntimeId());
        buffer.writeItem(packet.getHelmet());
        buffer.writeItem(packet.getChestplate());
        buffer.writeItem(packet.getLeggings());
        buffer.writeItem(packet.getBoots());
    }

}

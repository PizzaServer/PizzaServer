package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.network.protocol.packets.ResourcePackStackPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;

public class V419ResourcePackStackPacketHandler extends BaseProtocolPacketHandler<ResourcePackStackPacket> {

    @Override
    public void encode(ResourcePackStackPacket packet, BasePacketBuffer buffer) {
        buffer.writeBoolean(packet.isForcedToAccept());

        buffer.writeUnsignedVarInt(packet.getBehaviourPacks().size());
        for (ResourcePack behaviourPack : packet.getBehaviourPacks()) {
            buffer.writeString(behaviourPack.getUuid().toString());
            buffer.writeString(behaviourPack.getVersion());
            buffer.writeString(""); // subpack name but it doesn't look like it effects anything?
        }

        buffer.writeUnsignedVarInt(packet.getResourcePacks().size());
        for (ResourcePack resourcePack : packet.getResourcePacks()) {
            buffer.writeString(resourcePack.getUuid().toString());
            buffer.writeString(resourcePack.getVersion());
            buffer.writeString(""); // subpack name but it doesn't look like it effects anything?
        }

        buffer.writeString(packet.getGameVersion());
        buffer.writeExperiments(packet.getExperiments());
        buffer.writeBoolean(packet.isExperimentsPreviouslyEnabled());
    }

}

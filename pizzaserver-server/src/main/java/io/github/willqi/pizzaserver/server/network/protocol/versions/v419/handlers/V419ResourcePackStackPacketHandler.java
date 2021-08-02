package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackStackPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.github.willqi.pizzaserver.api.packs.ResourcePack;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackStackPacketHandler extends BaseProtocolPacketHandler<ResourcePackStackPacket> {

    @Override
    public void encode(ResourcePackStackPacket packet, ByteBuf buffer, BasePacketHelper helper) {
        buffer.writeBoolean(packet.isForcedToAccept());

        VarInts.writeUnsignedInt(buffer, packet.getBehaviourPacks().size());
        for (ResourcePack behaviourPack : packet.getBehaviourPacks()) {
            helper.writeString(behaviourPack.getUuid().toString(), buffer);
            helper.writeString(behaviourPack.getVersion(), buffer);
            helper.writeString("", buffer); // subpack name but it doesn't look like it effects anything?
        }

        VarInts.writeUnsignedInt(buffer, packet.getResourcePacks().size());
        for (ResourcePack resourcePack : packet.getResourcePacks()) {
            helper.writeString(resourcePack.getUuid().toString(), buffer);
            helper.writeString(resourcePack.getVersion(), buffer);
            helper.writeString("", buffer); // subpack name but it doesn't look like it effects anything?
        }

        helper.writeString(packet.getGameVersion(), buffer);

        helper.writeExperiments(packet.getExperiments(), buffer);
        buffer.writeBoolean(packet.isExperimentsPreviouslyEnabled());


    }

}

package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ResourcePackStackPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.github.willqi.pizzaserver.server.packs.DataPack;
import io.netty.buffer.ByteBuf;

public class V419ResourcePackStackPacketHandler extends ProtocolPacketHandler<ResourcePackStackPacket> {

    @Override
    public void encode(ResourcePackStackPacket packet, ByteBuf buffer, PacketHelper helper) {
        buffer.writeBoolean(packet.isForcedToAccept());

        VarInts.writeUnsignedInt(buffer, packet.getBehaviourPacks().length);
        for (DataPack behaviourPack : packet.getBehaviourPacks()) {
            helper.writeString(behaviourPack.getUuid().toString(), buffer);
            helper.writeString(behaviourPack.getVersion(), buffer);
            helper.writeString("", buffer); // subpack name but it doesn't look like it effects anything?
        }

        VarInts.writeUnsignedInt(buffer, packet.getResourcePacks().length);
        for (DataPack resourcePack : packet.getResourcePacks()) {
            helper.writeString(resourcePack.getUuid().toString(), buffer);
            helper.writeString(resourcePack.getVersion(), buffer);
            helper.writeString("", buffer); // subpack name but it doesn't look like it effects anything?
        }

        helper.writeString(packet.getGameVersion(), buffer);
        buffer.writeIntLE(0);   // No experiments
        buffer.writeBoolean(false); // Experiments toggled?


    }

}

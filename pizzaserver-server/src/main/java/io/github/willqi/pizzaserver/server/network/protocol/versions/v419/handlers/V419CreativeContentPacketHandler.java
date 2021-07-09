package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.server.item.Item;
import io.github.willqi.pizzaserver.server.network.protocol.packets.CreativeContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419CreativeContentPacketHandler extends ProtocolPacketHandler<CreativeContentPacket> {

    @Override
    public void encode(CreativeContentPacket packet, ByteBuf buffer, PacketHelper helper) {
        VarInts.writeUnsignedInt(buffer, packet.getItems().size());
        for (Item item : packet.getItems()) {
            VarInts.writeUnsignedInt(buffer, item.getId().ordinal());
            helper.writeItem(item, buffer);
        }
    }

}

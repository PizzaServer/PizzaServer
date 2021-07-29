package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.CommandRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

public class V419CommandRequestPacketHandler extends BaseProtocolPacketHandler<CommandRequestPacket> {
    @Override
    public CommandRequestPacket decode(ByteBuf buffer, BasePacketHelper helper) {
        CommandRequestPacket packet = new CommandRequestPacket();
        packet.setCommand(helper.readString(buffer));
        packet.setCommandType(VarInts.readInt(buffer));
        packet.setUuid(helper.readUUID(buffer));
        packet.setRequestID(helper.readString(buffer)); //TODO: I'm worried about this being empty most of the time, do some more research later
        packet.setUnknownBoolean(buffer.readBoolean());
        return new CommandRequestPacket();
    }
}

package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import com.google.errorprone.annotations.Var;
import io.github.willqi.pizzaserver.format.mcworld.utils.VarInts;
import io.github.willqi.pizzaserver.server.network.protocol.packets.AvailableCommandsPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.function.ObjIntConsumer;

public class V419AvailableCommandsPacket extends BaseProtocolPacketHandler<AvailableCommandsPacket> {

    public static final int ARG_FLAG_VALID = 0x100000;
    public static final int ARG_FLAG_ENUM = 0x200000;
    public static final int ARG_FLAG_POSTFIX = 0x1000000;

    private static final ObjIntConsumer<ByteBuf> WRITE_BYTE = (s, v) -> s.writeByte((byte) v);
    private static final ObjIntConsumer<ByteBuf> WRITE_SHORT = ByteBuf::writeShortLE;
    private static final ObjIntConsumer<ByteBuf> WRITE_INT = ByteBuf::writeIntLE;

    @Override
    public void encode(AvailableCommandsPacket packet, ByteBuf buffer, BasePacketHelper helper) {
    }
}

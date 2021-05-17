package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import com.nukkitx.network.VarInts;
import io.github.willqi.pizzaserver.network.protocol.packets.ViolationPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.ProtocolPacketHandler;
import io.netty.buffer.ByteBuf;

import java.nio.charset.StandardCharsets;

public class V419ViolationPacketHandler implements ProtocolPacketHandler<ViolationPacket> {

    @Override
    public ViolationPacket decode(ByteBuf buffer) {
        // TODO: Confirm this actually works.
        // I received this packet before but it threw an error and the data read was incorrect.
        // But it could just be because the client wasn't initialized?
        System.out.println(buffer.readableBytes() + " bytes in violation");
        System.out.println(VarInts.readInt(buffer));
        System.out.println(VarInts.readInt(buffer));
        System.out.println(VarInts.readInt(buffer));
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        System.out.println(new String(data, StandardCharsets.UTF_8));

        throw new AssertionError("Violation packet retrieved");
    }

    @Override
    public void encode(ViolationPacket packet, ByteBuf buffer) {

    }

}

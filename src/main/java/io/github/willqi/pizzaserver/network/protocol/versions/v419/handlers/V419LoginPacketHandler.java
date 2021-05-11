package io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.PacketHandler;
import io.netty.buffer.ByteBuf;

public class V419LoginPacketHandler extends PacketHandler<LoginPacket> {

    @Override
    public LoginPacket decode(ByteBuf buffer) {
        LoginPacket packet = new LoginPacket();
        packet.setProtocol(buffer.readInt());

//        int chainAndSkinDataLength = VarInts.readInt(buffer);
//        ByteBuf chainAndSkinData = buffer.readSlice(chainAndSkinDataLength);
//        System.out.println(chainAndSkinData.readIntLE());
//        System.out.println(chainAndSkinData.readSlice(chainAndSkinData.readIntLE()).toString(StandardCharsets.UTF_8));

        return packet;
    }

    @Override
    public void encode(LoginPacket packet, ByteBuf buffer) {

    }

}

package io.github.willqi.pizzaserver.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.network.protocol.packets.DisconnectPacket;
import io.github.willqi.pizzaserver.network.protocol.packets.LoginPacket;
import io.github.willqi.pizzaserver.network.protocol.packets.PlayStatusPacket;
import io.github.willqi.pizzaserver.network.protocol.packets.ViolationPacket;
import io.github.willqi.pizzaserver.network.protocol.versions.PacketRegistry;
import io.github.willqi.pizzaserver.network.protocol.versions.v419.handlers.*;

public class V419PacketRegistry extends PacketRegistry {

    public V419PacketRegistry() {
        this.register(LoginPacket.ID, new V419LoginPacketHandler())
            .register(PlayStatusPacket.ID, new V419PlayStatusPacketHandler())
            .register(DisconnectPacket.ID, new V419DisconnectPacketHandler())
            .register(ViolationPacket.ID, new V419ViolationPacketHandler());
    }

}
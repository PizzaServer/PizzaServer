package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.*;

public class V419PacketRegistry extends PacketRegistry {

    public V419PacketRegistry() {
        this.register(LoginPacket.ID, new V419LoginPacketHandler())
            .register(PlayStatusPacket.ID, new V419PlayStatusPacketHandler())
            .register(DisconnectPacket.ID, new V419DisconnectPacketHandler())
            .register(ResourcePacksInfoPacket.ID, new V419ResourcePacksInfoPacketHandler())
            .register(ResourcePackResponsePacket.ID, new V419ResourcePackResponsePacketHandler())
            .register(ResourcePackDataInfoPacket.ID, new V419ResourcePackDataInfoPacketHandler())
            .register(ResourcePackChunkDataPacket.ID, new V419ResourcePackChunkDataPacketHandler())
            .register(ResourcePackChunkRequestPacket.ID, new V419ResourcePackChunkRequestPacketHandler())
            .register(StartGamePacket.ID, new V419StartGamePacketHandler())
            .register(ViolationPacket.ID, new V419ViolationPacketHandler())
            .register(ClientCacheStatusPacket.ID, new V419ClientCacheStatusPacketHandler());
    }

    @Override
    public PacketHelper getPacketHelper() {
        return V419PacketHelper.INSTANCE;
    }

}
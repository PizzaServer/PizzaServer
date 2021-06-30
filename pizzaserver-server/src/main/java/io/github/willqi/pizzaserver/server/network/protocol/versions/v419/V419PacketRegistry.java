package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import com.google.common.graph.Network;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.*;

public class V419PacketRegistry extends PacketRegistry {

    public V419PacketRegistry() {
        this.register(LoginPacket.ID, new V419LoginPacketHandler())
            .register(SetLocalPlayerAsInitializedPacket.ID, new V419SetLocalPlayerAsInitializedPacketHandler())
            .register(PlayStatusPacket.ID, new V419PlayStatusPacketHandler())
            .register(DisconnectPacket.ID, new V419DisconnectPacketHandler())
            .register(ResourcePacksInfoPacket.ID, new V419ResourcePacksInfoPacketHandler())
            .register(ResourcePackResponsePacket.ID, new V419ResourcePackResponsePacketHandler())
            .register(ResourcePackStackPacket.ID, new V419ResourcePackStackPacketHandler())
            .register(ResourcePackDataInfoPacket.ID, new V419ResourcePackDataInfoPacketHandler())
            .register(ResourcePackChunkDataPacket.ID, new V419ResourcePackChunkDataPacketHandler())
            .register(ResourcePackChunkRequestPacket.ID, new V419ResourcePackChunkRequestPacketHandler())
            .register(StartGamePacket.ID, new V419StartGamePacketHandler())
            .register(CreativeContentPacket.ID, new V419CreativeContentPacketHandler())
            .register(BiomeDefinitionPacket.ID, new V419BiomeDefinitionPacketHandler())
            .register(RequestChunkRadiusPacket.ID, new V419RequestChunkRadiusPacketHandler())
            .register(LevelChunkPacket.ID, new V419LevelChunkPacketHandler())
            .register(NetworkChunkPublisherUpdatePacket.ID, new V419NetworkChunkPublisherUpdatePacketHandler())
            .register(MovePlayerPacket.ID, new V419MovePlayerPacketHandler())
            .register(ViolationPacket.ID, new V419ViolationPacketHandler())
            .register(ClientCacheStatusPacket.ID, new V419ClientCacheStatusPacketHandler());
    }

    @Override
    public PacketHelper getPacketHelper() {
        return V419PacketHelper.INSTANCE;
    }

}
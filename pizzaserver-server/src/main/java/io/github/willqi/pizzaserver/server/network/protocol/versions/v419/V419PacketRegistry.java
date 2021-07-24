package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketHelper;
import io.github.willqi.pizzaserver.server.network.protocol.versions.PacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.*;

public class V419PacketRegistry extends PacketRegistry {

    public V419PacketRegistry() {
        this.register(LoginPacket.ID, new V419LoginPacketHandler())
            .register(SetLocalPlayerAsInitializedPacket.ID, new V419SetLocalPlayerAsInitializedPacketHandler())
            .register(PlayStatusPacket.ID, new V419PlayStatusPacketHandler())
            .register(PlayerActionPacket.ID, new V419PlayerActionPacketHandler())
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
            .register(ChunkRadiusUpdatedPacket.ID, new V419ChunkRadiusUpdatedPacketHandler())
            .register(LevelChunkPacket.ID, new V419LevelChunkPacketHandler())
            .register(UpdateBlockPacket.ID, new V419UpdateBlockPacketHandler())
            .register(NetworkChunkPublisherUpdatePacket.ID, new V419NetworkChunkPublisherUpdatePacketHandler())
            .register(ClientCacheStatusPacket.ID, new V419ClientCacheStatusPacketHandler())
            .register(MovePlayerPacket.ID, new V419MovePlayerPacketHandler())
            .register(UpdateAttributesPacket.ID, new V419UpdateAttributesPacketHandler())
            .register(TextPacket.ID, new V419TextPacketHandler())
            .register(SetEntityDataPacket.ID, new V419SetEntityDataPacketHandler())
            .register(ViolationPacket.ID, new V419ViolationPacketHandler());
    }

    @Override
    public PacketHelper getPacketHelper() {
        return V419PacketHelper.INSTANCE;
    }

}
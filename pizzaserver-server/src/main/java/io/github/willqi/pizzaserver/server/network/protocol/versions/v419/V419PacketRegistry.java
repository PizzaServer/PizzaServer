package io.github.willqi.pizzaserver.server.network.protocol.versions.v419;

import io.github.willqi.pizzaserver.server.network.protocol.packets.MobEquipmentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketRegistry;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.*;

public class V419PacketRegistry extends BasePacketRegistry {

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
            .register(ItemComponentPacket.ID, new V419ItemComponentPacketHandler())
            .register(CreativeContentPacket.ID, new V419CreativeContentPacketHandler())
            .register(BiomeDefinitionPacket.ID, new V419BiomeDefinitionPacketHandler())
            .register(RequestChunkRadiusPacket.ID, new V419RequestChunkRadiusPacketHandler())
            .register(ChunkRadiusUpdatedPacket.ID, new V419ChunkRadiusUpdatedPacketHandler())
            .register(WorldEventPacket.ID, new V419WorldEventPacketHandler())
            .register(WorldChunkPacket.ID, new V419WorldChunkPacketHandler())
            .register(UpdateBlockPacket.ID, new V419UpdateBlockPacketHandler())
            .register(NetworkChunkPublisherUpdatePacket.ID, new V419NetworkChunkPublisherUpdatePacketHandler())
            .register(ClientCacheStatusPacket.ID, new V419ClientCacheStatusPacketHandler())
            .register(PlayerListPacket.ID, new V419PlayerListPacketHandler())
            .register(AddPlayerPacket.ID, new V419AddPlayerPacketHandler())
            .register(PlayerSkinPacket.ID, new V419PlayerSkinPacketHandler())
            .register(MovePlayerPacket.ID, new V419MovePlayerPacketHandler())
            .register(InteractPacket.ID, new V419InteractPacketHandler())
            .register(ContainerOpenPacket.ID, new V419ContainerOpenPacketHandler())
            .register(ContainerClosePacket.ID, new V419ContainerClosePacketHandler())
            .register(InventoryContentPacket.ID, new V419InventoryContentPacketHandler())
            .register(InventorySlotPacket.ID, new V419InventorySlotPacketHandler())
            .register(InventoryTransactionPacket.ID, new V419InventoryTransactionPacketHandler())
            .register(ItemStackRequestPacket.ID, new V419ItemStackRequestPacketHandler())
            .register(ItemStackResponsePacket.ID, new V419ItemStackResponsePacketHandler())
            .register(PlayerHotbarPacket.ID, new V419PlayerHotbarPacketHandler())
            .register(MobEquipmentPacket.ID, new V419MobEquipmentPacketHandler())
            .register(MobArmourEquipmentPacket.ID, new V419MobArmourEquipmentPacketHandler())
            .register(UpdateAttributesPacket.ID, new V419UpdateAttributesPacketHandler())
            .register(TextPacket.ID, new V419TextPacketHandler())
            .register(SetEntityDataPacket.ID, new V419SetEntityDataPacketHandler())
            .register(ViolationPacket.ID, new V419ViolationPacketHandler())
            .register(WorldSoundEventPacket.ID, new V419WorldSoundEventPacketHandler())
            .register(PlayerAnimatePacket.ID, new V419PlayerAnimatePacketHandler())
            .register(RemoveEntityPacket.ID, new V419RemoveEntityPacketHandler())
            .register(AnimateEntityPacket.ID, new V419AnimateEntityPacketHandler());
    }

}
package io.github.willqi.pizzaserver.server.network;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.MobEquipmentPacket;
import io.github.willqi.pizzaserver.api.network.protocol.packets.*;

public abstract class BaseBedrockPacketHandler {

    /**
     * Called when any packet is processed.
     * @param packet any bedrock packet sent by the client
     */
    public void onPacket(BaseBedrockPacket packet) {}

    public void onPacket(LoginPacket packet) {}

    public void onPacket(SetLocalPlayerAsInitializedPacket packet) {}

    public void onPacket(ResourcePackResponsePacket packet) {}

    public void onPacket(ResourcePackChunkRequestPacket packet) {}


    // chunk related
    public void onPacket(ClientCacheStatusPacket packet) {}

    public void onPacket(RequestChunkRadiusPacket packet) {}

    public void onPacket(WorldSoundEventPacket packet) {}


    // player actions
    public void onPacket(MovePlayerPacket packet) {}

    public void onPacket(TextPacket packet) {}

    public void onPacket(PlayerActionPacket packet) {}

    public void onPacket(PlayerAnimatePacket packet) {}

    public void onPacket(PlayerSkinPacket packet) {}

    public void onPacket(MobEquipmentPacket packet) {}

    public void onPacket(InteractPacket packet) {}


    // inventory actions
    public void onPacket(ContainerClosePacket packet) {}

    public void onPacket(ItemStackRequestPacket packet) {}

    public void onPacket(InventoryTransactionPacket packet) {}


    // misc
    public void onPacket(ViolationPacket packet) {}

}

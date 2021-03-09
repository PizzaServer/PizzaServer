package io.github.willqi.pizzaserver.network.handlers;

import com.nukkitx.protocol.bedrock.BedrockPacket;
import com.nukkitx.protocol.bedrock.BedrockServerSession;
import com.nukkitx.protocol.bedrock.handler.BedrockPacketHandler;
import com.nukkitx.protocol.bedrock.packet.*;
import io.github.willqi.pizzaserver.Server;

public class PlayerPacketHandler implements BedrockPacketHandler {

    private BedrockServerSession session;
    private Server server;

    public PlayerPacketHandler(BedrockServerSession session, Server server) {
        // Handles moving packets from the network thread to the main thread
        this.session = session;
        this.server = server;
    }

    @Override
    public boolean handle(LoginPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(ResourcePackClientResponsePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(TextPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(MoveEntityAbsolutePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(MovePlayerPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(RiderJumpPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(TickSyncPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(InventoryTransactionPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(MobEquipmentPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(InteractPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(BlockPickRequestPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(EntityPickRequestPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(PlayerActionPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(SetEntityDataPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(SetEntityMotionPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(AnimatePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(RespawnPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(ContainerClosePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(PlayerHotbarPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(InventoryContentPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(InventorySlotPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(AdventureSettingsPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(BlockEntityDataPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(PlayerInputPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(SetPlayerGameTypePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(MapInfoRequestPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(RequestChunkRadiusPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(ItemFrameDropItemPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(CommandRequestPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(CommandBlockUpdatePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(ResourcePackChunkRequestPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(PlayerSkinPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(ModalFormResponsePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(ServerSettingsRequestPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(SetLocalPlayerAsInitializedPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(EmotePacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(EntityEventPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(BookEditPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(FilterTextPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    @Override
    public boolean handle(PacketViolationWarningPacket packet) {
        this.queuePacket(packet);
        return true;
    }

    private void queuePacket(BedrockPacket packet) {
        this.server.getNetwork().queueServerboundPacket(this.session, packet);
    }

}

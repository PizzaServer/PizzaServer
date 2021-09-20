package io.github.willqi.pizzaserver.server.network.protocol.versions.v428.handlers;

import io.github.willqi.pizzaserver.server.ImplServer;
import io.github.willqi.pizzaserver.server.network.protocol.data.PlayerAction;
import io.github.willqi.pizzaserver.server.network.protocol.packets.PlayerActionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419PlayerActionPacketHandler;

public class V428PlayerActionPacketHandler extends V419PlayerActionPacketHandler {

    public V428PlayerActionPacketHandler() {
        this.actions.put(PlayerAction.BLOCK_PREDICT_DESTROY, 26);
        this.actions.put(PlayerAction.BLOCK_CONTINUE_DESTROY, 27);
    }

    @Override
    public PlayerActionPacket decode(BasePacketBuffer buffer) {
        PlayerActionPacket packet = new PlayerActionPacket();
        packet.setEntityRuntimeID(buffer.readUnsignedVarLong());
        int action = buffer.readVarInt();
        packet.setActionType(this.actions.inverse().get(action));
        if (packet.getActionType() == null) {
            ImplServer.getInstance().getLogger().warn("There is an unidentified PlayerAction with an id of " + action + "!");
        }
        packet.setVector3(buffer.readVector3i());
        packet.setFace(buffer.readVarInt());
        return packet;
    }
}

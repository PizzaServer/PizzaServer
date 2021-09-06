package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryActionType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class V419ItemStackRequestPacketHandler extends BaseProtocolPacketHandler<ItemStackRequestPacket> {

    @Override
    public ItemStackRequestPacket decode(BasePacketBuffer buffer) {
        ItemStackRequestPacket packet = new ItemStackRequestPacket();
        int totalRequests = buffer.readUnsignedVarInt();
        List<ItemStackRequestPacket.Request> requests = new ArrayList<>(totalRequests);
        for (int i = 0; i < totalRequests; i++) {
            requests.add(this.readRequest(buffer));
        }

        packet.setRequests(requests);
        return packet;
    }

    protected ItemStackRequestPacket.Request readRequest(BasePacketBuffer buffer) {
        int requestId = buffer.readVarInt();

        int actionsLength = buffer.readUnsignedVarInt();
        List<InventoryAction> actions = new ArrayList<>(actionsLength);
        for (int i = 0; i < actionsLength; i++) {
            int typeId = buffer.readByte();
            InventoryActionType type = buffer.getData().getInventoryActionType(typeId);
            actions.add(buffer.readInventoryAction(type));
        }
        return new ItemStackRequestPacket.Request(requestId, actions);
    }

}

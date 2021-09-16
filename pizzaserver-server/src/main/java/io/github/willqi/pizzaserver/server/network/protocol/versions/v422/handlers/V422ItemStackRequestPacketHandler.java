package io.github.willqi.pizzaserver.server.network.protocol.versions.v422.handlers;

import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.authoritative.actions.InventoryActionType;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackRequestPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419ItemStackRequestPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class V422ItemStackRequestPacketHandler extends V419ItemStackRequestPacketHandler {

    @Override
    protected ItemStackRequestPacket.Request readRequest(BasePacketBuffer buffer) {
        int requestId = buffer.readVarInt();

        int actionsLength = buffer.readUnsignedVarInt();
        List<InventoryAction> actions = new ArrayList<>(actionsLength);
        for (int i = 0; i < actionsLength; i++) {
            int typeId = buffer.readByte();
            InventoryActionType type = buffer.getData().getInventoryActionType(typeId);
            actions.add(buffer.readInventoryAction(type));
        }

        int customNamesLength = buffer.readUnsignedVarInt();
        List<String> customNames = new ArrayList<>();
        for (int i = 0; i < customNamesLength; i++) {
            customNames.add(buffer.readString());
        }

        return new ItemStackRequestPacket.Request(requestId, actions, customNames);
    }

}

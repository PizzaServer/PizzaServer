package io.github.willqi.pizzaserver.server.network.protocol.versions.v431.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources.*;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers.V419InventoryTransactionPacketHandler;

import java.util.ArrayList;
import java.util.List;

public class V431InventoryTransactionPacketHandler extends V419InventoryTransactionPacketHandler {

    @Override
    protected List<InventoryTransactionAction> readActions(BasePacketBuffer buffer) {
        int actionCount = buffer.readUnsignedVarInt();
        List<InventoryTransactionAction> actions = new ArrayList<>(actionCount);
        for (int i = 0; i < actionCount; i++) {
            int actionTypeId = buffer.readUnsignedVarInt();
            InventoryTransactionSourceType actionType = InventoryTransactionSourceType.getById(actionTypeId);
            if (actionType == null) {
                throw new NullPointerException("Unknown action type was sent: " + actionTypeId);
            }

            InventoryTransactionSource source;
            switch (actionType) {
                case CONTAINER:
                    int inventoryId = buffer.readVarInt();
                    source = new InventoryTransactionContainerSource(inventoryId);
                    break;
                case GLOBAL:
                    source = new InventoryTransactionGlobalSource();
                    break;
                case WORLD:
                    InventoryTransactionWorldSource.Flag flag = InventoryTransactionWorldSource.Flag.values()[buffer.readUnsignedVarInt()];
                    source = new InventoryTransactionWorldSource(flag);
                    break;
                case CREATIVE:
                    source = new InventoryTransactionCreativeSource();
                    break;
                case UNTRACKED:
                    int untrackedInventoryId = buffer.readVarInt();
                    source = new InventoryTransactionUntrackedSource(untrackedInventoryId);
                    break;
                case NOT_IMPLEMENTED:
                    int notImplementedInventoryId = buffer.readVarInt();
                    source = new InventoryTransactionNotImplementedSource(notImplementedInventoryId);
                    break;
                default:
                    throw new AssertionError("Missing action type handler for inventory transaction action type: " + actionType);
            }

            int slot = buffer.readUnsignedVarInt();
            ItemStack oldItemStack = buffer.readItem();
            ItemStack newItemStack = buffer.readItem();

            actions.add(new InventoryTransactionAction(source, slot, oldItemStack, newItemStack));
        }

        return actions;
    }

}

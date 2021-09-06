package io.github.willqi.pizzaserver.server.network.protocol.versions.v419.handlers;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.BlockFace;
import io.github.willqi.pizzaserver.commons.utils.Vector3;
import io.github.willqi.pizzaserver.commons.utils.Vector3i;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionSlotChange;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionType;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionReleaseItemData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionUseItemData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionUseItemOnEntityData;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.sources.*;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryTransactionPacket;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BasePacketBuffer;
import io.github.willqi.pizzaserver.server.network.protocol.versions.BaseProtocolPacketHandler;

import java.util.*;

public class V419InventoryTransactionPacketHandler extends BaseProtocolPacketHandler<InventoryTransactionPacket> {

    @Override
    public InventoryTransactionPacket decode(BasePacketBuffer buffer) {
        InventoryTransactionPacket inventoryTransactionPacket = new InventoryTransactionPacket();
        inventoryTransactionPacket.setLegacyRequestId(buffer.readVarInt());
        if (inventoryTransactionPacket.getLegacyRequestId() != 0) {
            inventoryTransactionPacket.setLegacySlotsChanges(this.getContainerSlotChanges(buffer));
        } else {
            inventoryTransactionPacket.setLegacySlotsChanges(Collections.emptySet());
        }
        InventoryTransactionType transactionType = InventoryTransactionType.values()[buffer.readUnsignedVarInt()];
        inventoryTransactionPacket.setType(transactionType);
        inventoryTransactionPacket.setActions(this.readActions(buffer));
        inventoryTransactionPacket.setData(this.readData(buffer, transactionType));

        return inventoryTransactionPacket;
    }

    protected Set<InventoryTransactionSlotChange> getContainerSlotChanges(BasePacketBuffer buffer) {
        int changedSlotsLength = buffer.readUnsignedVarInt();
        Set<InventoryTransactionSlotChange> slotsChanges = new HashSet<>(changedSlotsLength);
        for (int i = 0; i < changedSlotsLength; i++) {
            int containerId = buffer.readByte();
            byte[] changedSlots = buffer.readByteArray();
            slotsChanges.add(new InventoryTransactionSlotChange(containerId, changedSlots));
        }

        return slotsChanges;
    }

    protected List<InventoryTransactionAction> readActions(BasePacketBuffer buffer) {
        boolean usingNetworkIds = buffer.readBoolean();

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

    protected InventoryTransactionData readData(BasePacketBuffer buffer, InventoryTransactionType type) {
        switch (type) {
            case ITEM_USE:
                return new InventoryTransactionUseItemData(InventoryTransactionUseItemData.Action.values()[buffer.readUnsignedVarInt()],
                        buffer.readVector3i(),
                        BlockFace.resolve(buffer.readVarInt()),
                        buffer.readVarInt(),
                        buffer.readItem(),
                        buffer.readVector3(),
                        buffer.readVector3(),
                        buffer.readUnsignedVarInt());
            case ITEM_USE_ON_ENTITY:
                return new InventoryTransactionUseItemOnEntityData(buffer.readUnsignedVarLong(),
                        InventoryTransactionUseItemOnEntityData.Action.values()[buffer.readUnsignedVarInt()],
                        buffer.readVarInt(),
                        buffer.readItem(),
                        buffer.readVector3(),
                        buffer.readVector3());
            case ITEM_RELEASE:
                return new InventoryTransactionReleaseItemData(InventoryTransactionReleaseItemData.Action.values()[buffer.readUnsignedVarInt()],
                        buffer.readVarInt(),
                        buffer.readItem(),
                        buffer.readVector3());
            default:
                return null;
        }
    }

}

package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionAction;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionSlotChange;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.InventoryTransactionType;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.transactions.data.InventoryTransactionData;

import java.util.Collections;
import java.util.List;
import java.util.Set;

/**
 * Sent by the client when it modifies/uses the inventory in a way that is not handled by server authoritative inventories.
 * (e.g. dropping items, using/releasing items)
 */
public class InventoryTransactionPacket extends BaseBedrockPacket {

    public static final int ID = 0x1e;

    private int legacyRequestId;
    private Set<InventoryTransactionSlotChange> legacySlotsChanges;
    private List<InventoryTransactionAction> actions;
    private InventoryTransactionType type;
    private InventoryTransactionData data;


    public InventoryTransactionPacket() {
        super(ID);
    }

    public int getLegacyRequestId() {
        return this.legacyRequestId;
    }

    public void setLegacyRequestId(int legacyRequestId) {
        this.legacyRequestId = legacyRequestId;
    }

    public Set<InventoryTransactionSlotChange> getLegacySlotsChanges() {
        return Collections.unmodifiableSet(this.legacySlotsChanges);
    }

    public void setLegacySlotsChanges(Set<InventoryTransactionSlotChange> slotsChanges) {
        this.legacySlotsChanges = slotsChanges;
    }

    public InventoryTransactionType getType() {
        return this.type;
    }

    public void setType(InventoryTransactionType type) {
        this.type = type;
    }

    public InventoryTransactionData getData() {
        return this.data;
    }

    public void setData(InventoryTransactionData data) {
        this.data = data;
    }

    public List<InventoryTransactionAction> getActions() {
        return Collections.unmodifiableList(this.actions);
    }

    public void setActions(List<InventoryTransactionAction> actions) {
        this.actions = actions;
    }


}

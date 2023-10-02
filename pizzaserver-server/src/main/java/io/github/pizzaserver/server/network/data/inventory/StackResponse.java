package io.github.pizzaserver.server.network.data.inventory;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponse;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseContainer;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseSlot;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.response.ItemStackResponseStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StackResponse {

    private final int requestId;
    private final Map<ContainerSlotType, List<ItemStackResponseSlot>> changes = new HashMap<>();


    public StackResponse(int requestId) {
        this.requestId = requestId;
    }

    public void addChange(InventorySlotContainer slotContainer) {
        if (!this.changes.containsKey(slotContainer.getSlotType())) {
            this.changes.put(slotContainer.getSlotType(), new ArrayList<>());
        }
        this.changes.get(slotContainer.getSlotType()).add(new ItemStackResponseSlot((byte) slotContainer.getNetworkSlot(),
                (byte) slotContainer.getNetworkSlot(),
                (byte) slotContainer.getItemStack().getCount(),
                slotContainer.getItemStack().getNetworkId(),
                slotContainer.getItemStack().getCustomName().orElse(""),
                slotContainer.getItemStack().getNBT().getInt("Damage")));
    }

    public ItemStackResponse serialize() {
        List<ItemStackResponseContainer> payload = new ArrayList<>();
        for (ContainerSlotType slotType : this.changes.keySet()) {
            payload.add(new ItemStackResponseContainer(slotType, this.changes.get(slotType)));
        }

        return new ItemStackResponse(ItemStackResponseStatus.OK, this.requestId, payload);
    }

}

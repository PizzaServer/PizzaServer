package io.github.pizzaserver.server.network.data.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.packet.ItemStackResponsePacket;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StackResponse {

    private final int requestId;
    private final Map<ContainerSlotType, List<ItemStackResponsePacket.ItemEntry>> changes = new HashMap<>();


    public StackResponse(int requestId) {
        this.requestId = requestId;
    }

    public void addChange(InventorySlotContainer slotContainer) {
        if (!this.changes.containsKey(slotContainer.getSlotType())) {
            this.changes.put(slotContainer.getSlotType(), new ArrayList<>());
        }
        this.changes.get(slotContainer.getSlotType()).add(new ItemStackResponsePacket.ItemEntry((byte) slotContainer.getNetworkSlot(),
                (byte) slotContainer.getNetworkSlot(),
                (byte) slotContainer.getItemStack().getCount(),
                slotContainer.getItemStack().getNetworkId(),
                slotContainer.getItemStack().getCustomName().orElse(""),
                slotContainer.getItemStack().getNBT().getInt("Damage")));
    }

    public ItemStackResponsePacket.Response serialize() {
        List<ItemStackResponsePacket.ContainerEntry> payload = new ArrayList<>();
        for (ContainerSlotType slotType : this.changes.keySet()) {
            payload.add(new ItemStackResponsePacket.ContainerEntry(slotType, this.changes.get(slotType)));
        }

        return new ItemStackResponsePacket.Response(ItemStackResponsePacket.ResponseStatus.OK, this.requestId, payload);
    }

}

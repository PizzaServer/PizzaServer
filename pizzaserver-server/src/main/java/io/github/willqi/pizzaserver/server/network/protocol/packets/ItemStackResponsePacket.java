package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;

import java.util.*;

/**
 * Used to respond to a ItemStackRequestPacket.
 */
public class ItemStackResponsePacket extends BaseBedrockPacket {

    public static final int ID = 0x94;

    private List<Response> responses = Collections.emptyList();


    public ItemStackResponsePacket() {
        super(ID);
    }

    public List<Response> getResponses() {
        return this.responses;
    }

    public void setResponses(List<Response> responses) {
        this.responses = responses;
    }


    public static class Response {

        private final int requestId;
        private Status status = Status.OK;
        private final Map<InventorySlotType, List<SlotInfo>> inventories = new HashMap<>();

        public Response(int requestId) {
            this.requestId = requestId;
        }

        public int getRequestId() {
            return this.requestId;
        }

        public Status getStatus() {
            return this.status;
        }

        public void setStatus(Status status) {
            this.status = status;
        }

        public Map<InventorySlotType, List<SlotInfo>> getInventories() {
            return this.inventories;
        }

        public void addSlotChange(InventorySlotType slotType, SlotInfo slotInfo) {
            if (!this.inventories.containsKey(slotType)) {
                this.inventories.put(slotType, new ArrayList<>());
            }
            this.inventories.get(slotType).add(slotInfo);
        }


        public enum Status {
            OK,
            ERROR
        }

        public static class SlotInfo {

            private int slot;
            private ItemStack itemStack;


            public SlotInfo(int slot, ItemStack itemStack) {
                this.slot = slot;
                this.itemStack = itemStack;
            }

            public int getSlot() {
                return this.slot;
            }

            public ItemStack getItemStack() {
                return this.itemStack;
            }

        }

    }

}

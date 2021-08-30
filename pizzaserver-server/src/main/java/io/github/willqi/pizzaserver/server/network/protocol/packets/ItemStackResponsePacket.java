package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;

import java.util.*;

/**
 * Used to respond to a ItemStackRequestPacket
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
            private int hotbarSlot;

            private int itemStackNetworkId;
            private int itemStackCount;


            private SlotInfo(int slot, int hotbarSlot, int itemStackNetworkId, int itemStackCount) {
                this.slot = slot;
                this.hotbarSlot = hotbarSlot;
                this.itemStackNetworkId = itemStackNetworkId;
                this.itemStackCount = itemStackCount;
            }

            public int getSlot() {
                return this.slot;
            }

            public int getHotbarSlot() {
                return this.hotbarSlot;
            }

            public int getItemStackNetworkId() {
                return this.itemStackNetworkId;
            }

            public int getItemStackCount() {
                return this.itemStackCount;
            }


            public static class Builder {

                private int slot;
                private int hotbarSlot;

                private int itemStackNetworkId;
                private int itemStackCount;


                public Builder setSlot(int slot) {
                    this.slot = slot;
                    return this;
                }

                public Builder setHotbarSlot(int hotbarSlot) {
                    this.hotbarSlot = hotbarSlot;
                    return this;
                }

                public Builder setItemStackNetworkId(int itemStackNetworkId) {
                    this.itemStackNetworkId = itemStackNetworkId;
                    return this;
                }

                public Builder setItemStackCount(int itemStackCount) {
                    this.itemStackCount = itemStackCount;
                    return this;
                }

                public SlotInfo build() {
                    return new SlotInfo(this.slot, this.hotbarSlot, this.itemStackNetworkId, this.itemStackCount);
                }

            }

        }

    }

}

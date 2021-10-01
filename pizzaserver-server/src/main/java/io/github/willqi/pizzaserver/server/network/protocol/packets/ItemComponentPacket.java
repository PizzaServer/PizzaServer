package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.api.item.types.ItemType;
import io.github.willqi.pizzaserver.api.network.protocol.packets.BaseBedrockPacket;

import java.util.Collections;

import java.util.Set;

/**
 * Used to send the components of custom items.
 * Required to be sent for custom items to properly work
 */
public class ItemComponentPacket extends BaseBedrockPacket {

    public static final int ID = 0xa2;

    private Set<Entry> customItemTypes = Collections.emptySet();


    public ItemComponentPacket() {
        super(ID);
    }

    public Set<Entry> getEntries() {
        return Collections.unmodifiableSet(this.customItemTypes);
    }

    public void setEntries(Set<Entry> customItemTypes) {
        this.customItemTypes = customItemTypes;
    }


    public static class Entry {

        private final ItemType customItemType;
        private final int runtimeId;


        public Entry(ItemType customItemType, int runtimeId) {
            this.customItemType = customItemType;
            this.runtimeId = runtimeId;
        }

        public ItemType getCustomItemType() {
            return this.customItemType;
        }

        public int getRuntimeId() {
            return this.runtimeId;
        }

    }

}

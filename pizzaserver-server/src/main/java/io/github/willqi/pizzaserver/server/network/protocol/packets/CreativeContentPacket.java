package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.item.Item;

import java.util.Collection;
import java.util.HashSet;

/**
 * Sent with all of the creative items in the creative inventory
 */
public class CreativeContentPacket extends ImplBedrockPacket {

    public static final int ID = 0x91;

    private Collection<Item> items = new HashSet<>();


    public CreativeContentPacket() {
        super(ID);
    }

    public Collection<Item> getItems() {
        return this.items;
    }

    public void setItems(Collection<Item> items) {
        this.items = items;
    }

}

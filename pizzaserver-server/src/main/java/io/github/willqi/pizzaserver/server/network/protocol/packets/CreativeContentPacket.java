package io.github.willqi.pizzaserver.server.network.protocol.packets;

import io.github.willqi.pizzaserver.server.item.Item;

public class CreativeContentPacket extends BedrockPacket {

    public static final int ID = 0x91;

    private Item[] items = new Item[0];


    public CreativeContentPacket() {
        super(ID);
    }

    public Item[] getItems() {
        return this.items;
    }

    public void setItems(Item[] items) {
        this.items = items;
    }

}

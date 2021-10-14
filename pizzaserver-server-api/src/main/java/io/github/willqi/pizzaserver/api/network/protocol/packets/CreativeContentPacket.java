package io.github.willqi.pizzaserver.api.network.protocol.packets;

import io.github.willqi.pizzaserver.api.item.ItemStack;

import java.util.ArrayList;
import java.util.List;

/**
 * Sent with all the creative items in the creative inventory.
 */
public class CreativeContentPacket extends BaseBedrockPacket {

    public static final int ID = 0x91;

    private List<ItemStack> entries = new ArrayList<>();


    public CreativeContentPacket() {
        super(ID);
    }

    public List<ItemStack> getEntries() {
        return this.entries;
    }

    public void setEntries(List<ItemStack> entries) {
        this.entries = entries;
    }

}

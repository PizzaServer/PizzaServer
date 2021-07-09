package io.github.willqi.pizzaserver.server.item;

import java.util.Collection;
import java.util.HashSet;

public abstract class ItemBlock extends Item {

    private Collection<ItemID> canPlaceOn = new HashSet<>();


    public Collection<ItemID> getBlocksCanBePlacedOn() {
        return this.canPlaceOn;
    }

    public void setBlocksCanBePlacedOn(Collection<ItemID> blocks) {
        this.canPlaceOn = blocks;
    }

}

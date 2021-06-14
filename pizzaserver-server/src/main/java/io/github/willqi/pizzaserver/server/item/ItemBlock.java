package io.github.willqi.pizzaserver.server.item;

public abstract class ItemBlock extends Item {

    private ItemID[] canPlaceOn = new ItemID[0];


    public ItemID[] getBlocksCanBePlacedOn() {
        return this.canPlaceOn;
    }

    public void setBlocksCanBePlacedOn(ItemID[] blocks) {
        this.canPlaceOn = blocks;
    }

}

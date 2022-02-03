package io.github.pizzaserver.api.event.type.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;

/**
 * Called when a player destroys an item by placing it in the creative mode inventory.
 */
public class InventoryCreativeDestructionEvent extends BaseInventoryEvent.Cancellable {

    protected Player player;
    protected ContainerSlotType sourceSlotType;
    protected int sourceSlot;
    protected Item sourceItem;
    protected Item droppedItem;

    public InventoryCreativeDestructionEvent(Player player,
                                             Inventory inventory,
                                             ContainerSlotType sourceSlotType,
                                             int sourceSlot,
                                             Item sourceItem,
                                             Item droppedItem) {
        super(inventory);
        this.player = player;
        this.sourceSlotType = sourceSlotType;
        this.sourceSlot = sourceSlot;
        this.sourceItem = sourceItem;
        this.droppedItem = droppedItem;
    }

    public Player getPlayer() {
        return this.player;
    }

    public ContainerSlotType getSourceSlotType() {
        return this.sourceSlotType;
    }

    public int getSourceSlot() {
        return this.sourceSlot;
    }

    public Item getSource() {
        return this.sourceItem.clone();
    }

    public Item getDrop() {
        return this.droppedItem.clone();
    }

}

package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import io.github.pizzaserver.api.inventory.PlayerCraftingInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemStick;
import io.github.pizzaserver.api.player.Player;

import java.util.Collections;
import java.util.Set;

public class ImplPlayerCraftingInventory extends BaseInventory implements PlayerCraftingInventory {

    public static final int SLOT_OFFSET = 28;

    private final Player player;
    private Item creativeOutput;


    public ImplPlayerCraftingInventory(Player player) {
        super(ContainerType.WORKBENCH, 5);
        this.player = player;
    }

    /**
     * The creative slot exists as an intermediate between crafting creative items to becoming actualized as items.
     * @return current item in creative output slot
     */
    public Item getCreativeOutput() {
        return Item.getAirIfNull(this.creativeOutput).clone();
    }

    public void setCreativeOutput(Item output) {
        this.creativeOutput = Item.getAirIfNull(output).clone();
    }

    @Override
    public Player getPlayer() {
        return this.player;
    }

    @Override
    public Item getResult() {
        return this.getSlot(4);
    }

    @Override
    public Item[] getGridSlots() {
        return new Item[] {
            this.getSlot(0),
            this.getSlot(1),
            this.getSlot(2),
            this.getSlot(3)
        };
    }

    @Override
    public Item getGridSlot(int slot) {
        if (slot < 0 || slot >= 4) {
            throw new IllegalArgumentException("Invalid grid slot provided.");
        }

        return this.getSlot(slot);
    }

    @Override
    public void setGridSlots(Item[] grid) {
        if (grid.length > 4) {
            throw new IllegalArgumentException("Invalid grid items length provided.");
        }
        Item[] slotsSet = new Item[5];
        switch (grid.length) {
            case 4:
                slotsSet[3] = grid[3];
            case 3:
                slotsSet[2] = grid[2];
            case 2:
                slotsSet[1] = grid[1];
            case 1:
                slotsSet[0] = grid[0];
                break;
        }
        slotsSet[4] = this.getResult();

        this.setSlots(slotsSet);
    }

    @Override
    public void setGridSlot(int slot, Item item) {
        if (slot < 0 || slot >= 4) {
            throw new IllegalArgumentException("Invalid grid slot provided.");
        }
        this.setSlot(slot, item);
    }

    @Override
    public void sendSlots(Player player) {
        for (int slot = 0; slot < 4; slot++) {
            this.sendSlot(player, slot);
        }
    }

    @Override
    public void sendSlot(Player player, int slot) {
        sendInventorySlot(player, this.getSlot(slot), slot + SLOT_OFFSET, ContainerId.UI);
    }

    @Override
    public Set<Player> getViewers() {
        return Collections.singleton(this.player);
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return this.getPlayer().equals(player);
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ((ImplPlayerInventory) this.getPlayer().getInventory()).sendContainerOpenPacket(player);
    }

}

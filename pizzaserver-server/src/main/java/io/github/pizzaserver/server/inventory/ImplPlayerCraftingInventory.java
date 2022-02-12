package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.inventory.PlayerCraftingInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;

import java.util.Collections;
import java.util.Set;

public class ImplPlayerCraftingInventory extends BaseInventory implements PlayerCraftingInventory {

    public static final int RESULT_SLOT = 50;

    private final Player player;
    private Item creativeOutput;


    public ImplPlayerCraftingInventory(Player player) {
        super(ContainerType.WORKBENCH, 5);
        this.player = player;
    }

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
        System.out.println("failed to send slots TODO");
    }

    @Override
    public void sendSlot(Player player, int slot) {
        int actualSlot = slot;
        if (slot == 4) {
            // RESULT
        } else {
            actualSlot += 27;
        }
        // sendInventorySlot(player, this.getSlot(slot), 30, ContainerId.UI);
        // sendInventorySlot(player, new ItemStick(3), 30, ContainerId.UI);
        // sendInventorySlot(player, new ItemStick(4), 31, ContainerId.UI);
        // sendInventorySlot(player, new ItemStick(2), 29, ContainerId.UI);
        // sendInventorySlot(player, new ItemStick(1), 28, ContainerId.UI);
    }

    @Override
    public Set<Player> getViewers() {
        return Collections.singleton(this.player);
    }

}

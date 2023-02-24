package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerId;
import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.inventory.PlayerCraftingInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;

import java.util.Collections;
import java.util.Set;

public class ImplPlayerCraftingInventory extends BaseInventory implements PlayerCraftingInventory {

    private static final int SLOT_OFFSET = 28;

    private final Player player;
    private Item creativeOutput;


    public ImplPlayerCraftingInventory(Player player) {
        super(ContainerType.WORKBENCH, 4);
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

    @Override
    public int convertFromNetworkSlot(int networkSlot) {
        return networkSlot - SLOT_OFFSET;
    }

    @Override
    public RecipeBlockType getRecipeBlockType() {
        return RecipeBlockType.CRAFTING_TABLE;
    }

}

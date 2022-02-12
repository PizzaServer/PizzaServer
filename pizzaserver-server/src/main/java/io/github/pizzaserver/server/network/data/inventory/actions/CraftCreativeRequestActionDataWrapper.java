package io.github.pizzaserver.server.network.data.inventory.actions;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.CraftCreativeStackRequestActionData;
import io.github.pizzaserver.api.item.CreativeRegistry;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.server.inventory.ImplPlayerCraftingInventory;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;

public class CraftCreativeRequestActionDataWrapper extends StackRequestActionWrapper<CraftCreativeStackRequestActionData> {

    private final Item creativeItem;
    private final InventorySlotContainer destination;

    public CraftCreativeRequestActionDataWrapper(ImplPlayer player, CraftCreativeStackRequestActionData action) {
        super(player);
        this.creativeItem = CreativeRegistry.getInstance().getItemByNetworkId(action.getCreativeItemNetworkId()).orElse(null);
        this.destination = new InventorySlotContainer(player, ContainerSlotType.CREATIVE_OUTPUT, ImplPlayerCraftingInventory.RESULT_SLOT);
    }

    public Item getItem() {
        return Item.getAirIfNull(this.creativeItem).clone();
    }

    public InventorySlotContainer getDestination() {
        return this.destination;
    }

}

package io.github.pizzaserver.server.network.data.inventory.actions;

import io.github.pizzaserver.api.item.CreativeRegistry;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.server.network.data.inventory.InventorySlotContainer;
import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;
import org.cloudburstmc.protocol.bedrock.data.inventory.itemstack.request.action.CraftCreativeAction;

public class CraftCreativeRequestActionDataWrapper extends StackRequestActionWrapper<CraftCreativeAction> {

    private final Item creativeItem;
    private final InventorySlotContainer destination;

    public CraftCreativeRequestActionDataWrapper(ImplPlayer player, CraftCreativeAction action) {
        super(player);
        this.creativeItem = CreativeRegistry.getInstance().getItemByNetworkId(action.getCreativeItemNetworkId()).orElse(null);
        this.destination = new InventorySlotContainer(player, ContainerSlotType.CRAFTING_OUTPUT, 0);
    }

    public Item getItem() {
        return Item.getAirIfNull(this.creativeItem).clone();
    }

    public InventorySlotContainer getDestination() {
        return this.destination;
    }

}

package io.github.pizzaserver.api.item.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.player.Player;

public class ItemToolBehavior extends DefaultItemBehavior<Item> {

    @Override
    public void onBreak(Player player, Item item, Block block) {
        DurableItemComponent durableItemComponent = (DurableItemComponent) item;
        if (durableItemComponent.getMaxDurability() != -1) {
            int damage = item.getNBT().getInt("Damage");
            item.setNBT(item.getNBT()
                    .toBuilder()
                    .putInt("Damage", damage + 1)
                    .build());

            if (damage > durableItemComponent.getMaxDurability()) {
                item = Item.getAirIfNull(null);
            }

            player.getInventory().setHeldItem(item);
        }
    }

}

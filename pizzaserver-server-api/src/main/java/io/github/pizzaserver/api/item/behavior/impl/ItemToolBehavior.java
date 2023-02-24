package io.github.pizzaserver.api.item.behavior.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.item.descriptors.DurableItem;
import io.github.pizzaserver.api.player.Player;

public class ItemToolBehavior extends BaseItemBehavior<DurableItem> {

    @Override
    public void onBreak(Player player, DurableItem item, Block block) {
        item.useDurability();
        player.getInventory().setHeldItem(item);
    }

}

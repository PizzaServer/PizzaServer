package io.github.pizzaserver.api.item.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.impl.ItemArmor;
import io.github.pizzaserver.api.player.Player;

public class ItemArmorBehavior extends BaseItemBehavior<ItemArmor> {

    @Override
    public boolean onInteract(Player player, ItemArmor item, Block block, BlockFace blockFace, Vector3f clickPosition) {
        Item oldItemStack = null;
        switch (item.getArmorSlot()) {
            case HELMET:
                oldItemStack = player.getInventory().getHelmet();
                player.getInventory().setHelmet(item);
                break;
            case CHESTPLATE:
                oldItemStack = player.getInventory().getChestplate();
                player.getInventory().setChestplate(item);
                break;
            case LEGGINGS:
                oldItemStack = player.getInventory().getLeggings();
                player.getInventory().setLeggings(item);
                break;
            case BOOTS:
                oldItemStack = player.getInventory().getBoots();
                player.getInventory().setBoots(item);
                break;
        }
        player.getInventory().setHeldItem(oldItemStack);
        return true;
    }

}

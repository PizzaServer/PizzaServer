package io.github.pizzaserver.api.item.types.impl;

import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.item.types.component.ArmorItemComponent;
import io.github.pizzaserver.api.player.Player;

public abstract class ItemTypeArmor extends BaseItemType implements ArmorItemComponent {

    @Override
    public boolean onInteract(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
        ItemStack oldItemStack = null;
        switch (this.getArmorSlot()) {
            case HELMET:
                oldItemStack = player.getInventory().getHelmet();
                player.getInventory().setHelmet(itemStack);
                break;
            case CHESTPLATE:
                oldItemStack = player.getInventory().getChestplate();
                player.getInventory().setChestplate(itemStack);
                break;
            case LEGGINGS:
                oldItemStack = player.getInventory().getLeggings();
                player.getInventory().setLeggings(itemStack);
                break;
            case BOOTS:
                oldItemStack = player.getInventory().getBoots();
                player.getInventory().setBoots(itemStack);
                break;
        }
        player.getInventory().setHeldItem(oldItemStack);
        return super.onInteract(player, itemStack, block, blockFace);
    }

}

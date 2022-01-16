package io.github.pizzaserver.api.item.impl;

import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.data.ItemID;
import io.github.pizzaserver.api.item.data.ToolTier;
import io.github.pizzaserver.api.item.data.ToolType;
import io.github.pizzaserver.api.item.descriptors.DurableItemComponent;
import io.github.pizzaserver.api.item.descriptors.ToolItemComponent;

public class ItemNetheriteSword extends Item implements DurableItemComponent, ToolItemComponent {

    public ItemNetheriteSword() {
        this(1);
    }

    public ItemNetheriteSword(int count) {
        this(count, 0);
    }

    public ItemNetheriteSword(int count, int meta) {
        super(ItemID.NETHERITE_SWORD, count, meta);
    }

    @Override
    public String getName() {
        return "Netherite Sword";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public int getDamage() {
        return 9;
    }

    @Override
    public ToolType getToolType() {
        return ToolType.SWORD;
    }

    @Override
    public ToolTier getToolTier() {
        return ToolTier.NETHERITE;
    }

    @Override
    public int getMaxDurability() {
        return 2032;
    }
}

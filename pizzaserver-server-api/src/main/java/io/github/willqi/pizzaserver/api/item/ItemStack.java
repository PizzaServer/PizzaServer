package io.github.willqi.pizzaserver.api.item;

import io.github.willqi.pizzaserver.api.item.types.BaseItemType;

public class ItemStack {

    private final BaseItemType itemType;
    private int amount;
    private int damage;

    public ItemStack(BaseItemType itemType) {
        this(itemType, 1);
    }

    public ItemStack(BaseItemType itemType, int amount) {
        this(itemType, amount, 0);
    }

    public ItemStack(BaseItemType itemType, int amount, int damage) {
        this.itemType = itemType;
        this.amount = amount;
        this.damage = damage;
    }

    public BaseItemType getItemType() {
        return this.itemType;
    }

    public int getAmount() {
        return this.amount;
    }

    public void setAmount(int stackSize) {
        this.amount = stackSize;
    }

    public int getDamage() {
        return this.damage;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

}

package io.github.willqi.pizzaserver.api.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.LivingEntity;
import io.github.willqi.pizzaserver.api.item.ItemStack;

public interface LivingEntityInventory extends EntityInventory {

    /**
     * Get the entity who owns this inventory
     * @return the entity who owns this inventory
     */
    LivingEntity getEntity();

    ItemStack getHelmet();

    void setHelmet(ItemStack helmet);

    ItemStack getChestplate();

    void setChestplate(ItemStack chestplate);

    ItemStack getLeggings();

    void setLeggings(ItemStack leggings);

    ItemStack getBoots();

    void setBoots(ItemStack boots);

    /**
     * Get the item the entity is holding
     * @return item the entity is holding
     */
    ItemStack getHeldItem();

    /**
     * Change the item the entity is holding
     * @param mainHand item the entity should hold
     */
    void setHeldItem(ItemStack mainHand);

    /**
     * Change the item that is in the offhand of the entity
     * @return item in the offhand of the entity
     */
    ItemStack getOffhandItem();

    /**
     * Set the item that is in the offhand of the entity
     * @param offHand item in the offhand of the entity
     */
    void setOffhandItem(ItemStack offHand);

}

package io.github.pizzaserver.api.entity.inventory;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.ItemStack;

public interface EntityInventory extends Inventory {

    /**
     * Get the entity who owns this inventory.
     * @return the entity who owns this inventory
     */
    Entity getEntity();

    /**
     * Change all pieces of armour.
     * @param helmet new helmet
     * @param chestplate new chestplate
     * @param leggings new leggings
     * @param boots new boots
     */
    void setArmour(ItemStack helmet, ItemStack chestplate, ItemStack leggings, ItemStack boots);

    ItemStack getHelmet();

    /**
     * Change the helmet of this entity.
     * @param helmet the helmet to change it to
     */
    void setHelmet(ItemStack helmet);

    ItemStack getChestplate();

    /**
     * Change the chestplate of this entity.
     * @param chestplate the chestplate to change it to
     */
    void setChestplate(ItemStack chestplate);

    ItemStack getLeggings();

    /**
     * Change the leggings of this entity.
     * @param leggings the leggings to change it to
     */
    void setLeggings(ItemStack leggings);

    ItemStack getBoots();

    /**
     * Change the boots of this entity.
     * @param boots the boots to change it to
     */
    void setBoots(ItemStack boots);

    /**
     * Get the item the entity is holding.
     * @return item the entity is holding
     */
    ItemStack getHeldItem();

    /**
     * Change the item the entity is holding.
     * @param mainHand item the entity should hold
     */
    void setHeldItem(ItemStack mainHand);

    /**
     * Change the item that is in the offhand of the entity.
     * @return item in the offhand of the entity
     */
    ItemStack getOffhandItem();

    /**
     * Set the item that is in the offhand of the entity.
     * @param offHand item in the offhand of the entity
     */
    void setOffhandItem(ItemStack offHand);

}

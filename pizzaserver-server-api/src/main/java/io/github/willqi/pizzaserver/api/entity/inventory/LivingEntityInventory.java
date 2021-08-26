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

    /**
     * Change the helmet of this entity
     * @param helmet the helmet to change it to
     * @return if the slot was changed
     */
    boolean setHelmet(ItemStack helmet);

    ItemStack getChestplate();

    /**
     * Change the chestplate of this entity
     * @param chestplate the chestplate to change it to
     * @return if the slot was changed
     */
    boolean setChestplate(ItemStack chestplate);

    ItemStack getLeggings();

    /**
     * Change the leggings of this entity
     * @param leggings the leggings to change it to
     * @return if the slot was changed
     */
    boolean setLeggings(ItemStack leggings);

    ItemStack getBoots();

    /**
     * Change the boots of this entity
     * @param boots the boots to change it to
     * @return if the slot was changed
     */
    boolean setBoots(ItemStack boots);

    /**
     * Get the item the entity is holding
     * @return item the entity is holding
     */
    ItemStack getHeldItem();

    /**
     * Change the item the entity is holding
     * @param mainHand item the entity should hold
     * @return if the slot was changed
     */
    boolean setHeldItem(ItemStack mainHand);

    /**
     * Change the item that is in the offhand of the entity
     * @return item in the offhand of the entity
     */
    ItemStack getOffhandItem();

    /**
     * Set the item that is in the offhand of the entity
     * @param offHand item in the offhand of the entity
     * @return if the slot was changed
     */
    boolean setOffhandItem(ItemStack offHand);

}

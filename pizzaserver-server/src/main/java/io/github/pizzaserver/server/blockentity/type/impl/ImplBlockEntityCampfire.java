package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockCampfire;
import io.github.pizzaserver.api.blockentity.type.BlockEntityCampfire;
import io.github.pizzaserver.api.entity.EntityItem;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.recipe.data.RecipeType;
import io.github.pizzaserver.api.recipe.type.FurnaceRecipe;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.commons.utils.Check;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;
import io.github.pizzaserver.server.recipe.ImplRecipeRegistry;

import java.util.Collections;
import java.util.Optional;
import java.util.Set;

public class ImplBlockEntityCampfire extends BaseBlockEntity<BlockCampfire> implements BlockEntityCampfire {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.CAMPFIRE);
    private static final int COOK_TICKS_REQUIRED = 600;

    protected Item[] slots = new Item[4];
    protected int[] cookTimes = new int[4];

    public ImplBlockEntityCampfire(BlockLocation location) {
        super(location);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

    @Override
    public Item getItem(int slot) {
        Check.withinBoundsInclusive(slot, 0, 3, "slot");
        return Item.getAirIfNull(this.slots[slot]).clone();
    }

    @Override
    public void setItem(int slot, Item item) {
        Check.withinBoundsInclusive(slot, 0, 3, "slot");
        this.slots[slot] = Item.getAirIfNull(item).clone();
        this.update();
    }

    @Override
    public int getCookTickProgressForSlot(int slot) {
        Check.withinBoundsInclusive(slot, 0, 3, "slot");
        return this.cookTimes[slot];
    }

    @Override
    public void setCookTickProgressForSlot(int slot, int cookTimeTicks) {
        Check.withinBoundsInclusive(slot, 0, 3, "slot");
        this.cookTimes[slot] = cookTimeTicks;
    }

    @Override
    public boolean onInteract(Player player) {
        Item itemInHand = player.getInventory().getHeldItem();
        Optional<FurnaceRecipe> recipe = this.getRecipeForItem(itemInHand);

        if (recipe.isPresent()) {
            for (int slot = 0; slot < 4; slot++) {
                if (this.getItem(slot).isEmpty()) {
                    itemInHand.setCount(itemInHand.getCount() - 1);
                    player.getInventory().setHeldItem(itemInHand);

                    Item cookingItem = ItemRegistry.getInstance().getItem(itemInHand.getItemId(), 1, itemInHand.getMeta());
                    this.setItem(slot, cookingItem);
                    break;
                }
            }

        }

        return super.onInteract(player);
    }

    @Override
    public void tick() {
        for (int slot = 0; slot < 4; slot++) {
            Item cookingItem = this.getItem(slot);
            if (!cookingItem.isAir()) {
                // See if we need to cook this item.
                Optional<FurnaceRecipe> furnaceRecipe = this.getRecipeForItem(cookingItem);
                if (furnaceRecipe.isPresent()) {
                    // Check cook progress!
                    if (this.getCookTickProgressForSlot(slot) >= COOK_TICKS_REQUIRED) {
                        this.setItem(slot, null);
                        this.setCookTickProgressForSlot(slot, 0);

                        // Drop the result item
                        this.getBlock().getWorld().addItemEntity(furnaceRecipe.get().getOutput(),
                                this.getBlock().getLocation().toVector3f(),
                                EntityItem.getRandomMotion());
                    } else {
                        this.setCookTickProgressForSlot(slot, this.getCookTickProgressForSlot(slot) + 1);
                    }
                }
            }
        }
    }

    protected Optional<FurnaceRecipe> getRecipeForItem(Item item) {
        return Optional.ofNullable((FurnaceRecipe) ((ImplRecipeRegistry) RecipeRegistry.getInstance()).getRecipes()
                .stream().filter(recipe -> recipe.getType() == RecipeType.FURNACE && recipe.getBlockType() == this.getRecipeBlockType())
                .filter(recipe -> ((FurnaceRecipe) recipe).getInput().hasSameDataAs(item))
                .findAny().orElse(null));
    }

    protected RecipeBlockType getRecipeBlockType() {
        return RecipeBlockType.CAMPFIRE;
    }

}

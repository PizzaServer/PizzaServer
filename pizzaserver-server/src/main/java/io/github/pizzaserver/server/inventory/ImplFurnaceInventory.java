package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.inventory.FurnaceInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.recipe.type.FurnaceRecipe;
import io.github.pizzaserver.api.recipe.type.Recipe;
import io.github.pizzaserver.api.recipe.type.RecipeType;
import io.github.pizzaserver.server.blockentity.type.impl.ImplBlockEntityFurnace;
import io.github.pizzaserver.server.recipe.ImplRecipeRegistry;

import java.util.Optional;
import java.util.Set;

public class ImplFurnaceInventory extends ImplContainerInventory<BlockFurnace, BlockEntityFurnace> implements FurnaceInventory {

    protected static final int INGREDIENT = 0;
    protected static final int FUEL = 1;
    protected static final int RESULT = 2;

    public ImplFurnaceInventory(BlockEntityFurnace blockEntity) {
        super(blockEntity, ContainerType.FURNACE);
    }

    @Override
    public Item getIngredient() {
        return this.getSlot(INGREDIENT);
    }

    @Override
    public void setIngredient(Item ingredient) {
        this.setSlot(INGREDIENT, ingredient);
    }

    @Override
    public Item getFuel() {
        return this.getSlot(FUEL);
    }

    @Override
    public void setFuel(Item fuel) {
        this.setSlot(FUEL, fuel);
    }

    @Override
    public Item getResult() {
        return this.getSlot(RESULT);
    }

    @Override
    public void setResult(Item result) {
        this.setSlot(RESULT, result);
    }

    @Override
    protected void onSlotChange(int slot, Item oldSlotItem, Item newSlotItem) {
        if (slot == INGREDIENT && !oldSlotItem.hasSameDataAs(newSlotItem)) {
            ((ImplBlockEntityFurnace) this.getBlockEntity()).onIngredientChange();
        }
    }

}

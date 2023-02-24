package io.github.pizzaserver.server.blockentity.type.impl;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerSetDataPacket;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockFurnace;
import io.github.pizzaserver.api.blockentity.type.BlockEntityFurnace;
import io.github.pizzaserver.api.inventory.FurnaceInventory;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.recipe.RecipeRegistry;
import io.github.pizzaserver.api.recipe.data.RecipeBlockType;
import io.github.pizzaserver.api.recipe.type.FurnaceRecipe;
import io.github.pizzaserver.api.recipe.data.RecipeType;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.inventory.ImplFurnaceInventory;
import io.github.pizzaserver.server.recipe.ImplRecipeRegistry;

import java.util.Set;

public class ImplBlockEntityFurnace extends ImplBlockEntityContainer<BlockFurnace> implements BlockEntityFurnace {

    public static final Set<String> BLOCK_IDS = Set.of(BlockID.FURNACE, BlockID.LIT_FURNACE);
    protected static final int MAX_COOK_TICKS = 200;

    private int fuelTicks;
    private int fuelDurationTicks;
    private int cookTime;

    private FurnaceRecipe activeRecipe;

    public ImplBlockEntityFurnace(BlockLocation location) {
        super(location, ContainerType.FURNACE);
        this.inventory = new ImplFurnaceInventory(this);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

    @Override
    public FurnaceInventory getInventory() {
        return (FurnaceInventory) this.inventory;
    }

    @Override
    public int getFuelTicks() {
        return this.fuelTicks;
    }

    @Override
    public void setFuelTicks(int ticks) {
        if (this.fuelTicks != ticks) {
            this.fuelTicks = ticks;
            this.sendContainerDataPacket(ContainerSetDataPacket.FURNACE_LIT_TIME, ticks);
        }
    }

    @Override
    public int getFuelDurationTicks() {
        return this.fuelDurationTicks;
    }

    @Override
    public void setFuelDurationTicks(int ticks) {
        if (this.fuelDurationTicks != ticks) {
            this.fuelDurationTicks = ticks;
            this.sendContainerDataPacket(ContainerSetDataPacket.FURNACE_LIT_DURATION, ticks);
        }
    }

    @Override
    public int getCookTimeTicks() {
        return this.cookTime;
    }

    @Override
    public void setCookTimeTicks(int ticks) {
        if (this.cookTime != ticks) {
            this.cookTime = ticks;
            this.sendContainerDataPacket(ContainerSetDataPacket.FURNACE_TICK_COUNT, ticks);
        }
    }

    protected void sendContainerDataPacket(int property, int value) {
        ContainerSetDataPacket setDataPacket = new ContainerSetDataPacket();
        setDataPacket.setProperty(property);
        setDataPacket.setValue(value);
        setDataPacket.setWindowId((byte) this.getInventory().getId());

        for (Player player : this.getViewers()) {
            player.sendPacket(setDataPacket);
        }
    }

    public void onIngredientChange() {
        FurnaceRecipe recipe = (FurnaceRecipe) ((ImplRecipeRegistry) RecipeRegistry.getInstance()).getRecipes()
                .stream().filter(r -> r.getType() == RecipeType.FURNACE && r.getBlockType() == this.getBlockRecipeType())
                .filter(r -> ((FurnaceRecipe) r).getInput().hasSameDataAs(this.getInventory().getIngredient()))
                .findAny().orElse(null);

        this.setCookTimeTicks(0);
        this.activeRecipe = recipe;
    }

    @Override
    public void tick() {
        if (this.activeRecipe != null) {
            if (this.fuelTicks == 0) {
                // Check if we can add fuel.
                Item fuelItem = this.getInventory().getFuel();

                if (fuelItem.getFuelTicks() != -1) {
                    this.setFuelTicks(fuelItem.getFuelTicks());
                    this.setFuelDurationTicks(fuelItem.getFuelTicks());

                    fuelItem.setCount(fuelItem.getCount() - 1);
                    this.getInventory().setFuel(fuelItem);
                }
            }

            if (this.fuelTicks > 0) {
                if (this.getCookTimeTicks() >= (MAX_COOK_TICKS / this.getBurnRate())) {
                    Item outputItem = this.activeRecipe.getOutput();
                    Item resultSlotItem = this.getInventory().getResult();

                    boolean isResultSlotEmpty = resultSlotItem.isEmpty();
                    boolean canAddOutputToResultSlot = resultSlotItem.hasSameDataAs(outputItem) && resultSlotItem.getCount() + outputItem.getCount() <= resultSlotItem.getMaxStackSize();

                    if (canAddOutputToResultSlot) {
                        outputItem.setCount(resultSlotItem.getCount() + outputItem.getCount());
                    }

                    if (isResultSlotEmpty || canAddOutputToResultSlot) {
                        this.getInventory().setResult(outputItem);
                        this.setCookTimeTicks(0);

                        Item inputItem = this.getInventory().getIngredient();
                        inputItem.setCount(inputItem.getCount() - 1);
                        this.getInventory().setIngredient(inputItem);
                    }
                } else {
                    this.setCookTimeTicks(this.getCookTimeTicks() + 1);
                }
            } else {
                this.setCookTimeTicks(0);
            }
        } else {
            this.setCookTimeTicks(0);
        }

        if (this.getFuelTicks() > 0) {
            this.setFuelTicks(this.getFuelTicks() - 1);
            this.reevaluateLitState();
        }
    }

    /**
     * Checks if this furnace should be lit or not and changes its state to lit/unlit as necessary.
     */
    protected void reevaluateLitState() {
        BlockFurnace furnaceBlock = this.getBlock();
        if (!furnaceBlock.isLit() && this.getFuelTicks() > 0) {
            furnaceBlock.setLit(true);
            this.getBlock().getWorld().setAndUpdateBlock(furnaceBlock, furnaceBlock.getLocation().toVector3i());
        } else if (furnaceBlock.isLit() && this.getFuelTicks() == 0) {
            furnaceBlock.setLit(false);
            this.getBlock().getWorld().setAndUpdateBlock(furnaceBlock, furnaceBlock.getLocation().toVector3i());
        }
    }

    protected float getBurnRate() {
        return 1;
    }

    protected RecipeBlockType getBlockRecipeType() {
        return RecipeBlockType.FURNACE;
    }

}

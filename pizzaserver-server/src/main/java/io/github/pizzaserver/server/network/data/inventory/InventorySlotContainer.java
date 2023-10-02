package io.github.pizzaserver.server.network.data.inventory;

import io.github.pizzaserver.api.inventory.*;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.server.inventory.*;
import io.github.pizzaserver.server.player.ImplPlayer;
import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerSlotType;

import java.util.Optional;

public class InventorySlotContainer {

    private final ImplPlayer player;
    private final ContainerSlotType slotType;
    private final int networkSlot;
    private boolean modified;


    public InventorySlotContainer(ImplPlayer player,
                                  ContainerSlotType slotType,
                                  int networkSlot) {
        this.player = player;
        this.slotType = slotType;
        this.networkSlot = networkSlot;
    }

    public ContainerSlotType getSlotType() {
        return this.slotType;
    }

    public int getNetworkSlot() {
        return this.networkSlot;
    }

    public BaseInventory getInventory() {
        Optional<Inventory> openInventory = this.player.getOpenInventory();
        if (openInventory.isPresent() && InventoryUtils.getSlotTypes(openInventory.get().getContainerType()).contains(this.slotType)) {
            // the inventory targeted was the open inventory.
            return (BaseInventory) openInventory.get();
        } else if (InventoryUtils.getSlotTypes(this.player.getInventory().getContainerType()).contains(this.slotType)) {
            // the inventory targeted was their own inventory.
            return this.player.getInventory();
        } else if (InventoryUtils.getSlotTypes(this.player.getInventory().getCraftingGrid().getContainerType()).contains(this.slotType)) {
            // the inventory targeted was their crafting grid.
            return (ImplPlayerCraftingInventory) this.player.getInventory().getCraftingGrid();
        } else {
            return null;
        }
    }

    public Item getItemStack() {
        BaseInventory inventory = this.getInventory();
        if (inventory == null) {
            return null;
        }

        int slot = inventory.convertFromNetworkSlot(this.getNetworkSlot());

        switch (this.slotType) {
            case CURSOR:
            case OFFHAND:
                if (this.getInventory() instanceof PlayerInventory) {
                    if (this.slotType == ContainerSlotType.CURSOR) {
                        return ((PlayerInventory) this.getInventory()).getCursor();
                    } else {
                        return ((PlayerInventory) this.getInventory()).getOffhandItem();
                    }
                } else {
                    return null;
                }
            case ARMOR:
                if (this.getInventory() instanceof EntityInventory entityInventory) {
                    return switch (slot) {
                        case 0 -> entityInventory.getHelmet();
                        case 1 -> entityInventory.getChestplate();
                        case 2 -> entityInventory.getLeggings();
                        case 3 -> entityInventory.getBoots();
                        default -> null;
                    };
                } else {
                    return null;
                }
            case CRAFTING_OUTPUT:
                if (inventory instanceof CraftingInventory) {
                    return ((ImplPlayerCraftingInventory) this.player.getInventory().getCraftingGrid()).getCreativeOutput();
                } else {
                    return null;
                }
            default:
                if (slot >= 0 && slot < this.getInventory().getSize()) {
                    return this.getInventory().getSlot(slot);
                } else {
                    return null;
                }
        }
    }

    public void setItemStack(Item itemStack) {
        if (!this.exists()) {
            throw new NullPointerException("Attempted to set non-existent item slot");
        }

        int slot = this.getInventory().convertFromNetworkSlot(this.getNetworkSlot());

        switch (this.slotType) {
            case CURSOR:
            case OFFHAND:
                if (this.getInventory() instanceof PlayerInventory) {
                    ImplPlayerInventory playerInventory = (ImplPlayerInventory) this.getInventory();
                    if (this.slotType == ContainerSlotType.CURSOR) {
                        playerInventory.setCursor(itemStack, true);
                    } else {
                        playerInventory.setOffhandItem(itemStack, true);
                    }
                }
                break;
            case ARMOR:
                if (this.getInventory() instanceof EntityInventory) {
                    ImplEntityInventory entityInventory = (ImplEntityInventory) this.getInventory();
                    switch (slot) {
                        case 0 -> entityInventory.setHelmet(itemStack, true);
                        case 1 -> entityInventory.setChestplate(itemStack, true);
                        case 2 -> entityInventory.setLeggings(itemStack, true);
                        case 3 -> entityInventory.setBoots(itemStack, true);
                    }
                }
                break;
            case CRAFTING_OUTPUT:
                if (this.getInventory() instanceof CraftingInventory) {
                    ((ImplPlayerCraftingInventory) this.player.getInventory().getCraftingGrid()).setCreativeOutput(itemStack);
                }
                break;
            default:
                if (slot >= 0 && slot < this.getInventory().getSize()) {
                    this.getInventory().setSlot(this.player, slot, itemStack, true);
                }
                break;
        }
        this.modified = true;
    }

    /**
     * Retrieves the new item stack for this slot if changed.
     * @return new item stack for this slot if changed
     */
    public boolean isModified() {
        return this.modified;
    }

    public boolean exists() {
        return this.getItemStack() != null;
    }

}

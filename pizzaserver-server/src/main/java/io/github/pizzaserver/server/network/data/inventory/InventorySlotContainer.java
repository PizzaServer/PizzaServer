package io.github.pizzaserver.server.network.data.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import io.github.pizzaserver.api.inventory.*;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.server.inventory.*;
import io.github.pizzaserver.server.player.ImplPlayer;

import java.util.Optional;

public class InventorySlotContainer {

    private final ImplPlayer player;
    private final ContainerSlotType slotType;
    private final int slot;
    private boolean modified;


    public InventorySlotContainer(ImplPlayer player,
                                  ContainerSlotType slotType,
                                  int slot) {
        this.player = player;
        this.slotType = slotType;
        this.slot = slot;
    }

    public ContainerSlotType getSlotType() {
        return this.slotType;
    }

    public int getSlot() {
        return this.slot;
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
                    return switch (this.getSlot()) {
                        case 0 -> entityInventory.getHelmet();
                        case 1 -> entityInventory.getChestplate();
                        case 2 -> entityInventory.getLeggings();
                        case 3 -> entityInventory.getBoots();
                        default -> null;
                    };
                } else {
                    return null;
                }
            case CREATIVE_OUTPUT:
                if (inventory instanceof ImplPlayerCraftingInventory craftingInventory) {
                    return craftingInventory.getCreativeOutput();
                } else {
                    return null;
                }
            case CRAFTING_INPUT:
                if (inventory instanceof PlayerCraftingInventory craftingInventory
                        && this.slot >= ImplPlayerCraftingInventory.SLOT_OFFSET
                        && this.slot <= ImplPlayerCraftingInventory.SLOT_OFFSET + 3) {
                    return craftingInventory.getSlot(this.slot - ImplPlayerCraftingInventory.SLOT_OFFSET);
                } else {
                    return null;
                }
            default:
                if (this.slot >= 0 && this.slot < this.getInventory().getSize()) {
                    return this.getInventory().getSlot(this.slot);
                } else {
                    return null;
                }
        }
    }

    public void setItemStack(Item itemStack) {
        if (!this.exists()) {
            throw new NullPointerException("Attempted to set non-existent item slot");
        }
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
                    switch (this.getSlot()) {
                        case 0:
                            entityInventory.setHelmet(itemStack, true);
                            break;
                        case 1:
                            entityInventory.setChestplate(itemStack, true);
                            break;
                        case 2:
                            entityInventory.setLeggings(itemStack, true);
                            break;
                        case 3:
                            entityInventory.setBoots(itemStack, true);
                            break;
                    }
                }
                break;
            case CREATIVE_OUTPUT:
                if (this.getInventory() instanceof ImplPlayerCraftingInventory craftingInventory) {
                    craftingInventory.setCreativeOutput(itemStack);
                }
            case CRAFTING_INPUT:
                if (this.getInventory() instanceof PlayerCraftingInventory
                        && this.slot >= ImplPlayerCraftingInventory.SLOT_OFFSET
                        && this.slot <= ImplPlayerCraftingInventory.SLOT_OFFSET + 3) {
                    this.getInventory().setSlot(this.slot - ImplPlayerCraftingInventory.SLOT_OFFSET, itemStack);
                }
                break;
            default:
                if (this.slot >= 0 && this.slot < this.getInventory().getSize()) {
                    this.getInventory().setSlot(this.player, this.slot, itemStack, true);
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

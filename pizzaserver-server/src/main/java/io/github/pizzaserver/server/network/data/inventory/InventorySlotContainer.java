package io.github.pizzaserver.server.network.data.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerSlotType;
import io.github.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.server.entity.inventory.BaseInventory;
import io.github.pizzaserver.server.entity.inventory.ImplEntityInventory;
import io.github.pizzaserver.server.entity.inventory.ImplPlayerInventory;
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
        if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(this.slotType)) {
            return (BaseInventory) openInventory.get();
        } else if (this.player.getInventory().getSlotTypes().contains(this.slotType)) {
            return this.player.getInventory();
        } else {
            return null;
        }
    }

    public ItemStack getItemStack() {
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
                if (this.getInventory() instanceof EntityInventory) {
                    EntityInventory entityInventory = (EntityInventory) this.getInventory();
                    switch (this.getSlot()) {
                        case 0:
                            return entityInventory.getHelmet();
                        case 1:
                            return entityInventory.getChestplate();
                        case 2:
                            return entityInventory.getLeggings();
                        case 3:
                            return entityInventory.getBoots();
                        default:
                            return null;
                    }
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

    public void setItemStack(ItemStack itemStack) {
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

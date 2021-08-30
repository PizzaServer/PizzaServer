package io.github.willqi.pizzaserver.server.network.handlers.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.entity.inventory.BaseEntityInventory;
import io.github.willqi.pizzaserver.server.entity.inventory.ImplPlayerInventory;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.InventorySlot;
import io.github.willqi.pizzaserver.server.network.protocol.data.inventory.actions.InventoryAction;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ItemStackResponsePacket;

import java.util.Optional;

public abstract class InventoryActionHandler<T extends InventoryAction> {

    protected InventoryActionHandler() {}

    /**
     * Returns if the action is valid (slots exist and action makes sense to be sent)
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action is valid
     */
    public abstract boolean isValid(Player player, T action);

    /**
     * Returns if the action went through
     * The action is validated before this is called
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action went through
     */
    public abstract boolean handle(ItemStackResponsePacket.Response response, Player player, T action);

    protected static Optional<EntityInventory> getInventory(Player player, InventorySlot slot) {
        Optional<EntityInventory> openInventory = player.getOpenInventory();
        if (player.getInventory().getSlotTypes().contains(slot.getInventorySlotType())) {
            return Optional.of(player.getInventory());
        } else if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(slot.getInventorySlotType())) {
            return openInventory;
        }
        return Optional.empty();
    }

    protected static boolean stackExists(Player player, InventorySlot slot) {
        Optional<EntityInventory> openInventory = player.getOpenInventory();
        if (player.getInventory().getSlotTypes().contains(slot.getInventorySlotType())) {
            // only consider the main inventory
            return playerInventoryStackExists(player, slot);
        } else if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(slot.getInventorySlotType())) {
            // only consider the open inventory
            // Check that the slot exists and matches
            return slot.getSlot() >= 0 &&
                    slot.getSlot() < openInventory.get().getSize() &&
                    openInventory.get().getSlot(slot.getSlot()).getNetworkId() == slot.getNetworkStackId();
        }
        return false;
    }

    private static boolean playerInventoryStackExists(Player player, InventorySlot slot) {
        switch (slot.getInventorySlotType()) {
            case ARMOR:
                switch (slot.getSlot()) {
                    case 0:
                        return player.getInventory().getHelmet().getNetworkId() == slot.getNetworkStackId();
                    case 1:
                        return player.getInventory().getChestplate().getNetworkId() == slot.getNetworkStackId();
                    case 2:
                        return player.getInventory().getLeggings().getNetworkId() == slot.getNetworkStackId();
                    case 3:
                        return player.getInventory().getBoots().getNetworkId() == slot.getNetworkStackId();
                    default:
                        return false;   // Armor slots are only 0-3
                }
            case OFFHAND:
                return slot.getSlot() == 1 && player.getInventory().getOffhandItem().getNetworkId() == slot.getNetworkStackId();
            case CURSOR:
                return slot.getSlot() == 0 && player.getInventory().getCursor().getNetworkId() == slot.getNetworkStackId();
            default:
                return slot.getSlot() >= 0 &&
                        slot.getSlot() < player.getInventory().getSize() &&
                        player.getInventory().getSlot(slot.getSlot()).getNetworkId() == slot.getNetworkStackId();
        }
    }


    protected static class SlotLocation {

        private final ItemStackResponsePacket.Response response;
        private final Player player;
        private final EntityInventory inventory;
        private final InventorySlot inventorySlot;
        private ItemStack itemStack;

        public SlotLocation(ItemStackResponsePacket.Response response, Player player, InventorySlot inventorySlot) {
            this.response = response;
            this.player = player;

            Optional<EntityInventory> inventory = InventoryActionHandler.getInventory(player, inventorySlot);
            if (!inventory.isPresent()) {
                throw new IllegalArgumentException("Slot does not exist");
            }
            this.inventory = inventory.get();
            this.inventorySlot = inventorySlot;

            // Assign the item stack this slot is referring to
            if (this.isUniquePlayerSlot()) {
                // Handle the slot differently
                ImplPlayerInventory playerInventory = (ImplPlayerInventory)this.getInventory();

                switch (this.inventorySlot.getInventorySlotType()) {
                    case OFFHAND:
                        this.itemStack = playerInventory.getOffhandItem();
                        break;
                    case ARMOR:
                        switch (this.inventorySlot.getSlot()) {
                            case 0:
                                this.itemStack = playerInventory.getHelmet();
                                break;
                            case 1:
                                this.itemStack = playerInventory.getChestplate();
                                break;
                            case 2:
                                this.itemStack = playerInventory.getLeggings();
                                break;
                            case 3:
                                this.itemStack = playerInventory.getBoots();
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid armor slot: " + this.inventorySlot.getSlot());
                        }
                        break;
                    case CURSOR:
                        this.itemStack = playerInventory.getCursor();
                        break;
                    default:
                        throw new IllegalArgumentException("Missing unique player slot handler: " + this.inventorySlot.getInventorySlotType());
                }
            } else {
                this.itemStack = this.getInventory().getSlot(this.inventorySlot.getSlot());
            }

        }

        public EntityInventory getInventory() {
            return this.inventory;
        }

        public ItemStack getItem() {
            return this.itemStack;
        }

        public void setItem(ItemStack itemStack) {
            this.itemStack = ItemStack.ensureItemStackExists(itemStack);

            // Change the slot
            if (this.isUniquePlayerSlot()) {
                // Handle the slot differently
                ImplPlayerInventory playerInventory = (ImplPlayerInventory)this.getInventory();

                switch (this.inventorySlot.getInventorySlotType()) {
                    case OFFHAND:
                        playerInventory.setOffhandItem(this.player, itemStack, true);
                        break;
                    case ARMOR:
                        switch (this.inventorySlot.getSlot()) {
                            case 0:
                                playerInventory.setHelmet(this.player, itemStack, true);
                                break;
                            case 1:
                                playerInventory.setChestplate(this.player, itemStack, true);
                                break;
                            case 2:
                                playerInventory.setLeggings(this.player, itemStack, true);
                                break;
                            case 3:
                                playerInventory.setBoots(this.player, itemStack, true);
                                break;
                            default:
                                throw new IllegalArgumentException("Invalid armor slot: " + this.inventorySlot.getSlot());
                        }
                        break;
                    case CURSOR:
                        playerInventory.setCursor(itemStack, true);
                        break;
                    default:
                        throw new IllegalArgumentException("Missing unique player slot handler: " + this.inventorySlot.getInventorySlotType());
                }
            } else {
                ((BaseEntityInventory)this.getInventory()).setSlot(this.player, this.inventorySlot.getSlot(), this.itemStack, true);
            }

            this.response.addSlotChange(this.inventorySlot.getInventorySlotType(), new ItemStackResponsePacket.Response.SlotInfo.Builder()
                    .setSlot(this.inventorySlot.getSlot())
                    .setHotbarSlot(this.inventorySlot.getSlot())
                    .setItemStackCount(this.itemStack.getCount())
                    .setItemStackNetworkId(this.itemStack.getNetworkId())
                    .build());
        }

        private boolean isUniquePlayerSlot() {
            return this.getInventory() instanceof PlayerInventory &&
                    (this.inventorySlot.getInventorySlotType() != InventorySlotType.INVENTORY &&
                            this.inventorySlot.getInventorySlotType() != InventorySlotType.HOTBAR);
        }

    }

}

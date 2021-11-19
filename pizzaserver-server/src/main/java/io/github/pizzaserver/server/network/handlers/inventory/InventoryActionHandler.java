package io.github.pizzaserver.server.network.handlers.inventory;

import io.github.pizzaserver.server.entity.inventory.BaseInventory;
import io.github.pizzaserver.server.entity.inventory.ImplPlayerInventory;
import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.ItemType;
import io.github.pizzaserver.api.item.types.components.ArmorItemComponent;
import io.github.pizzaserver.api.player.Player;

import java.util.Optional;

public abstract class InventoryActionHandler<T extends InventoryAction> {

    protected InventoryActionHandler() {}

    /**
     * Returns if the action is valid. (slots exist and action makes sense to be sent)
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action is valid
     */
    public abstract boolean isValid(Player player, T action);

    /**
     * Run the action after validating it.
     * The action should be validated before this is called
     * @param response response container
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action went through successfully
     */
    public abstract boolean runAction(ItemStackResponsePacket.Response response, Player player, T action);

    /**
     * Validates the action before running it.
     * @param response response container
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action went through successfully
     */
    public boolean tryAction(ItemStackResponsePacket.Response response, Player player, T action) {
        return this.isValid(player, action) && this.runAction(response, player, action);
    }

    /**
     * Returns the inventory of the slot provided or none if it doesn't exist.
     * @param player the player who requested the slot
     * @param inventorySlot the slot requested
     * @return the inventory or none if it doesn't exist
     */
    protected static Optional<Inventory> getInventory(Player player, AuthoritativeInventorySlot inventorySlot) {
        Optional<Inventory> openInventory = player.getOpenInventory();
        if (player.getInventory().getSlotTypes().contains(inventorySlot.getInventorySlotType())) {
            return Optional.of(player.getInventory());
        } else if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(inventorySlot.getInventorySlotType())) {
            return openInventory;
        }
        return Optional.empty();
    }

    /**
     * Returns the item stack at the specified slot or none if it doesn't exist.
     * @param player the player who requested the slot
     * @param inventorySlot the slot requested
     * @return the item stack or none if it doesn't exist
     */
    protected static Optional<ItemStack> getItemStack(Player player, AuthoritativeInventorySlot inventorySlot) {
        Optional<Inventory> optionalInventory = getInventory(player, inventorySlot);
        boolean validSlotId = inventorySlot.getInventorySlotType().isValidSlot(inventorySlot.getSlot());
        if (!validSlotId || !optionalInventory.isPresent()) {
            return Optional.empty();
        }

        Inventory inventory = optionalInventory.get();

        if (isUniquePlayerSlot(inventory, inventorySlot)) {
            // Call the correct getter as getSlot is not sufficient
            ImplPlayerInventory playerInventory = (ImplPlayerInventory) inventory;

            switch (inventorySlot.getInventorySlotType()) {
                case OFFHAND:
                    return Optional.of(playerInventory.getOffhandItem());
                case ARMOR:
                    switch (inventorySlot.getSlot()) {
                        case 0:
                            return Optional.of(playerInventory.getHelmet());
                        case 1:
                            return Optional.of(playerInventory.getChestplate());
                        case 2:
                            return Optional.of(playerInventory.getLeggings());
                        case 3:
                            return Optional.of(playerInventory.getBoots());
                        default:
                            throw new IllegalArgumentException("Invalid armor slot: " + inventorySlot.getSlot());
                    }
                case CURSOR:
                    return Optional.of(playerInventory.getCursor());
                default:
                    throw new IllegalArgumentException("Missing unique player slot handler: " + inventorySlot.getInventorySlotType());
            }
        } else {
            return Optional.of(inventory.getSlot(inventorySlot.getSlot()));
        }
    }

    /**
     * Returns if a stack has a matching network id at a slot.
     * @param player the player who requested the check
     * @param inventorySlot the slot requested
     * @return if the stacks match
     */
    protected static boolean stackExists(Player player, AuthoritativeInventorySlot inventorySlot) {
        Optional<ItemStack> itemStack = getItemStack(player, inventorySlot);
        return itemStack.isPresent()
                && itemStack.get().getNetworkId() == inventorySlot.getNetworkStackId();
    }

    /**
     * Returns if an item type can be placed in a specific slot.
     * @param itemType the item type
     * @param slotType the slot type
     * @return if the item type can be placed in the slot type
     */
    protected static boolean canPutItemTypeInSlot(ItemType itemType, InventorySlotType slotType) {
        switch (slotType) {
            case ARMOR:
                return itemType instanceof ArmorItemComponent;
            case OFFHAND:
                return itemType.isAllowedInOffHand();
            default:
                return true;
        }
    }

    private static boolean isUniquePlayerSlot(Inventory inventory, AuthoritativeInventorySlot slot) {
        if (inventory instanceof PlayerInventory) {
            switch (slot.getInventorySlotType()) {
                case INVENTORY:
                case HOTBAR:
                case PLAYER_INVENTORY:
                    return false;
                default:
                    return true;
            }
        } else {
            return false;
        }
    }


    /**
     * Helper class to make changing slots and registering slot responses easier.
     */
    protected static class SlotLocation {

        private final ItemStackResponsePacket.Response response;
        private final Player player;
        private final Inventory inventory;
        private final AuthoritativeInventorySlot inventorySlot;
        private ItemStack itemStack;


        public SlotLocation(ItemStackResponsePacket.Response response, Player player, AuthoritativeInventorySlot inventorySlot) {
            this.response = response;
            this.player = player;

            Optional<Inventory> inventory = InventoryActionHandler.getInventory(player, inventorySlot);
            if (!inventory.isPresent()) {
                throw new IllegalArgumentException("Inventory does not exist");
            }
            this.inventory = inventory.get();
            this.inventorySlot = inventorySlot;

            Optional<ItemStack> itemStack = getItemStack(player, inventorySlot);
            if (!itemStack.isPresent()) {
                throw new IllegalArgumentException("Slot does not exist");
            }
            this.itemStack = itemStack.get();
        }

        public Inventory getInventory() {
            return this.inventory;
        }

        public ItemStack getItem() {
            return this.itemStack;
        }

        public void setItem(ItemStack itemStack) {
            this.itemStack = ItemStack.ensureItemStackExists(itemStack);

            // Change the slot
            if (isUniquePlayerSlot(this.inventory, this.inventorySlot)) {
                // Call the correct setter as setSlot is not sufficient
                ImplPlayerInventory playerInventory = (ImplPlayerInventory) this.getInventory();

                switch (this.inventorySlot.getInventorySlotType()) {
                    case OFFHAND:
                        playerInventory.setOffhandItem(itemStack, true);
                        break;
                    case ARMOR:
                        switch (this.inventorySlot.getSlot()) {
                            case 0:
                                playerInventory.setHelmet(itemStack, true);
                                break;
                            case 1:
                                playerInventory.setChestplate(itemStack, true);
                                break;
                            case 2:
                                playerInventory.setLeggings(itemStack, true);
                                break;
                            case 3:
                                playerInventory.setBoots(itemStack, true);
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
                ((BaseInventory) this.getInventory()).setSlot(this.player, this.inventorySlot.getSlot(), this.itemStack, true);
            }

            // Record the change to be sent in the ItemStackResponsePacket
            this.response.addSlotChange(this.inventorySlot.getInventorySlotType(), new ItemStackResponsePacket.Response.SlotInfo(this.inventorySlot.getSlot(), this.itemStack));
        }

    }

}

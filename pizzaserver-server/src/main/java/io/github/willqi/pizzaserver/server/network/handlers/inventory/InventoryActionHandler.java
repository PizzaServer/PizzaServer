package io.github.willqi.pizzaserver.server.network.handlers.inventory;

import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.entity.inventory.InventorySlotType;
import io.github.willqi.pizzaserver.api.entity.inventory.PlayerInventory;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.item.types.BaseItemType;
import io.github.willqi.pizzaserver.api.item.types.components.ArmorItemComponent;
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

    /**
     * Returns the inventory of the slot provided or none if it doesn't exist
     * @param player the player who requested the slot
     * @param inventorySlot the slot requested
     * @return the inventory or none if it doesn't exist
     */
    protected static Optional<EntityInventory> getInventory(Player player, InventorySlot inventorySlot) {
        Optional<EntityInventory> openInventory = player.getOpenInventory();
        if (player.getInventory().getSlotTypes().contains(inventorySlot.getInventorySlotType())) {
            return Optional.of(player.getInventory());
        } else if (openInventory.isPresent() && openInventory.get().getSlotTypes().contains(inventorySlot.getInventorySlotType())) {
            return openInventory;
        }
        return Optional.empty();
    }

    /**
     * Returns the item stack at the specified slot or none if it doesn't exist
     * @param player the player who requested the slot
     * @param inventorySlot the slot requested
     * @return the item stack or none if it doesn't exist
     */
    protected static Optional<ItemStack> getItemStack(Player player, InventorySlot inventorySlot) {
        Optional<EntityInventory> optionalInventory = getInventory(player, inventorySlot);
        if (optionalInventory.isPresent()) {
            EntityInventory inventory = optionalInventory.get();

            if (isUniquePlayerSlot(inventory, inventorySlot)) {
                // Handle the slot differently
                ImplPlayerInventory playerInventory = (ImplPlayerInventory)inventory;

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
        } else {
            return Optional.empty();
        }
    }

    /**
     * Returns if a stack has a matching network id at a slot
     * @param player the player who requested the check
     * @param slot the slot requested
     * @return if the stacks match
     */
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

    /**
     * Returns if an item type can be placed in a specific slot
     * @param itemType the item type
     * @param slotType the slot type
     * @return if the item type can be placed in the slot type
     */
    protected static boolean canPutItemTypeInSlot(BaseItemType itemType, InventorySlotType slotType) {
        switch (slotType) {
            case ARMOR:
                return itemType instanceof ArmorItemComponent;
            case OFFHAND:
                return itemType.isAllowedInOffHand();
            default:
                return true;
        }
    }

    private static boolean isUniquePlayerSlot(EntityInventory inventory, InventorySlot slot) {
        return inventory instanceof PlayerInventory &&
                (slot.getInventorySlotType() != InventorySlotType.INVENTORY &&
                        slot.getInventorySlotType() != InventorySlotType.HOTBAR);
    }


    /**
     * Wrapper to make changing slots and registering slot responses easier
     */
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

            // Slot exists as we checked it before
            this.itemStack = getItemStack(player, inventorySlot).get();

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
            if (isUniquePlayerSlot(this.inventory, this.inventorySlot)) {
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

    }

}

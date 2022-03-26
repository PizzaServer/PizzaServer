package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.inventory.Inventory;
import io.github.pizzaserver.api.event.type.inventory.InventoryCloseEvent;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.item.ItemUtils;
import org.checkerframework.checker.nullness.Opt;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseInventory implements Inventory {

    public static int ID = 1;

    protected final int id;
    protected final int size;

    protected Item[] slots;
    protected final ContainerType containerType;

    protected final Set<Player> viewers = new HashSet<>();


    public BaseInventory(ContainerType containerType, int size) {
        this(containerType, size, ID++);
    }

    public BaseInventory(ContainerType containerType, int size, int id) {
        if (size > InventoryUtils.getSlotCount(containerType) || size < 0) {
            throw new IllegalArgumentException("Slot count of " + containerType + " must be within 0-" + InventoryUtils.getSlotCount(containerType));
        }

        this.size = size;
        this.id = id;
        this.containerType = containerType;
        this.slots = new Item[this.size];
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public ContainerType getContainerType() {
        return this.containerType;
    }

    @Override
    public void clear() {
        this.setSlots(new Item[this.size]);
    }

    @Override
    public Item[] getSlots() {
        Item[] slots = new Item[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {
            slots[i] = this.getSlot(i); // These slots are cloned
        }
        return slots;
    }

    @Override
    public void setSlots(Item[] slots) {
        if (slots.length != this.size) {
            throw new IllegalArgumentException("The slots provided must be " + this.size + " in length.");
        }

        for (int i = 0; i < this.size; i++) {
            this.slots[i] = Item.getAirIfNull(slots[i]).newNetworkCopy();
        }

        for (Player viewer : this.getViewers()) {
            this.sendSlots(viewer);
        }
    }

    @Override
    public Item getSlot(int slot) {
        return this.getSlot(slot, true);
    }

    /**
     * Get a slot in the inventory.
     * @param slot the slot
     * @param clone if the Item should be cloned or if it should retrieve the actual object
     * @return Item in that slot
     */
    public Item getSlot(int slot, boolean clone) {
        Item item = Item.getAirIfNull(this.slots[slot]);
        if (clone) {
            return item.clone();
        } else {
            return item;
        }
    }

    @Override
    public void setSlot(int slot, Item item) {
        this.setSlot(null, slot, item, false);
    }

    /**
     * Change a slot in the inventory.
     * @param player the player who is changing the slot if any exists
     * @param slot the slot changed
     * @param item the new item stack
     * @param keepNetworkId if the network id of the Item should be kept or if a new one should be generated
     */
    public void setSlot(Player player, int slot, Item item, boolean keepNetworkId) {
        if (item == null || item.isEmpty()) {
            this.slots[slot] = null;
        } else {
            Item newItem = keepNetworkId ? Item.getAirIfNull(item).clone() : Item.getAirIfNull(item).newNetworkCopy();
            this.slots[slot] = newItem;
        }

        for (Player viewer : this.getViewers()) {
            if (!viewer.equals(player)) {
                this.sendSlot(viewer, slot);
            }
        }
    }

    @Override
    public boolean contains(String itemId) {
        for (int i = 0; i < this.slots.length; i++) {
            if (this.getSlot(i).getItemId().equals(itemId)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean contains(Item item) {
        int countNeeded = item.getCount();
        for (int i = 0; i < this.slots.length; i++) {
            Item slot = this.getSlot(i);
            if (slot.hasSameDataAs(item)) {
                countNeeded = Math.max(0, countNeeded - slot.getCount());

                if (countNeeded == 0) {
                    break;
                }
            }
        }

        return countNeeded == 0;
    }

    @Override
    public Optional<Item> addItem(Item item) {
        Item remainingItem = Item.getAirIfNull(item).clone();

        // Add item to any existing stack
        for (int slot = 0; slot < this.getSize(); slot++) {
            if (remainingItem.getCount() <= 0) {
                break;
            }

            Item slotStack = this.getSlot(slot);
            if (slotStack.hasSameDataAs(item)) {
                int maxStackCount = remainingItem.getMaxStackSize();
                int spaceLeft = Math.max(maxStackCount - slotStack.getCount(), 0);
                int addedAmount = Math.min(spaceLeft, remainingItem.getCount());

                slotStack.setCount(slotStack.getCount() + addedAmount);
                this.setSlot(slot, slotStack);
                remainingItem.setCount(remainingItem.getCount() - addedAmount);
            }
        }

        // Add item to free slots
        for (int slot = 0; slot < this.getSize(); slot++) {
            if (remainingItem.getCount() <= 0) {
                break;
            }

            Item slotStack = this.getSlot(slot);
            if (slotStack.isEmpty()) {
                int maxStackCount = remainingItem.getMaxStackSize();
                int spaceLeft = Math.max(maxStackCount - slotStack.getCount(), 0);
                int addedAmount = Math.min(spaceLeft, remainingItem.getCount());

                Item newSlot = remainingItem.clone();
                newSlot.setCount(addedAmount);
                this.setSlot(slot, newSlot);
                remainingItem.setCount(remainingItem.getCount() - addedAmount);
            }
        }

        if (remainingItem.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(remainingItem);
        }
    }

    @Override
    public Optional<Item> removeItem(Item item) {
        Item remainingItem = Item.getAirIfNull(item).clone();

        for (int slot = 0; slot < this.getSize(); slot++) {
            if (remainingItem.getCount() <= 0) {
                break;
            }

            Item slotStack = this.getSlot(slot);
            if (slotStack.hasSameDataAs(remainingItem)) {
                int removedAmount = Math.min(remainingItem.getCount(), slotStack.getCount());

                slotStack.setCount(slotStack.getCount() - removedAmount);
                this.setSlot(slot, slotStack);

                remainingItem.setCount(remainingItem.getCount() - removedAmount);
            }
        }

        return Optional.ofNullable(remainingItem.isEmpty() ? null : remainingItem);
    }

    @Override
    public void sendSlots(Player player) {
        if (this.getViewers().contains(player)) {
            sendInventorySlots(player, this.getSlots(), this.getId());
        }
    }

    @Override
    public void sendSlot(Player player, int slot) {
        if (this.getViewers().contains(player)) {
            sendInventorySlot(player, this.getSlot(slot), slot, this.getId());
        }
    }

    @Override
    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(this.viewers);
    }

    @Override
    public int getExcessIfAdded(Item item) {
        Item remainingItem = Item.getAirIfNull(item).clone();

        for (int slot = 0; slot < this.getSize(); slot++) {
            if (remainingItem.getCount() <= 0) {
                break;
            }

            Item slotStack = this.getSlot(slot);
            int maxStackCount = remainingItem.getMaxStackSize();
            int spaceLeft = Math.max(maxStackCount - slotStack.getCount(), 0);
            int addedAmount = Math.min(spaceLeft, remainingItem.getCount());
            if (slotStack.isEmpty() || slotStack.hasSameDataAs(remainingItem)) {
                remainingItem.setCount(remainingItem.getCount() - addedAmount);
            }
        }

        return remainingItem.getCount();
    }

    protected abstract void sendContainerOpenPacket(Player player);

    /**
     * Close this inventory for a player.
     * @param player the player to close this inventory for
     * @return if the inventory was closed
     */
    public boolean closeFor(Player player) {
        if (this.viewers.contains(player)) {
            this.viewers.remove(player);

            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setId((byte) this.getId());
            player.sendPacket(containerClosePacket);

            InventoryCloseEvent inventoryCloseEvent = new InventoryCloseEvent(player, this);
            Server.getInstance().getEventManager().call(inventoryCloseEvent);
            return true;
        } else {
            return false;
        }
    }

    public boolean shouldBeClosedFor(Player player) {
        return this.getViewers().contains(player);
    }

    /**
     * Tries to open the inventory.
     * @param player the player to open this inventory to
     * @return if the inventory was opened
     */
    public boolean openFor(Player player) {
        if (this.canBeOpenedBy(player)) {
            this.viewers.add(player);
            this.sendContainerOpenPacket(player);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return !this.viewers.contains(player);
    }

    /**
     * Helper method to send a slot of an inventory.
     * @param player player to send the slot to
     * @param item the item stack to send
     * @param slot the slot
     * @param inventoryId the id of the inventory
     */
    protected static void sendInventorySlot(Player player, Item item, int slot, int inventoryId) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setContainerId(inventoryId);
        inventorySlotPacket.setSlot(slot);
        inventorySlotPacket.setItem(ItemUtils.serializeForNetwork(item, player.getVersion()));
        player.sendPacket(inventorySlotPacket);
    }

    /**
     * Helper method to send slots of an inventory.
     * @param player player to send the slots to
     * @param slots the slots
     * @param inventoryId the inventory id
     */
    protected static void sendInventorySlots(Player player, Item[] slots, int inventoryId) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(inventoryId);
        inventoryContentPacket.setContents(Arrays.stream(slots).map(item -> ItemUtils.serializeForNetwork(item, player.getVersion())).collect(Collectors.toList()));
        player.sendPacket(inventoryContentPacket);
    }

}

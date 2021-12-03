package io.github.pizzaserver.server.entity.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerClosePacket;
import com.nukkitx.protocol.bedrock.packet.InventoryContentPacket;
import com.nukkitx.protocol.bedrock.packet.InventorySlotPacket;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.entity.inventory.Inventory;
import io.github.pizzaserver.api.event.type.inventory.InventoryCloseEvent;
import io.github.pizzaserver.api.item.ItemRegistry;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.block.types.BlockTypeID;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.item.ItemUtils;

import java.util.*;
import java.util.stream.Collectors;

public abstract class BaseInventory implements Inventory {

    public static int ID = 1;

    protected final int id;
    protected final int size;

    protected ItemStack[] slots;
    protected final ContainerType containerType;

    private final Set<Player> viewers = new HashSet<>();


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
        this.slots = new ItemStack[this.size];
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
        this.setSlots(new ItemStack[this.size]);
    }

    @Override
    public ItemStack[] getSlots() {
        ItemStack[] slots = new ItemStack[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {
            slots[i] = this.getSlot(i); // These slots are cloned
        }
        return slots;
    }

    @Override
    public void setSlots(ItemStack[] slots) {
        if (slots.length != this.size) {
            throw new IllegalArgumentException("The slots provided must be " + this.size + " in length.");
        }

        for (int i = 0; i < this.size; i++) {
            this.slots[i] = ItemStack.ensureItemStackExists(slots[i]).newNetworkStack();
        }

        for (Player viewer : this.getViewers()) {
            this.sendSlots(viewer);
        }
    }

    @Override
    public ItemStack getSlot(int slot) {
        return this.getSlot(slot, true);
    }

    /**
     * Get a slot in the inventory.
     * @param slot the slot
     * @param clone if the ItemStack should be cloned or if it should retrieve the actual object
     * @return ItemStack in that slot
     */
    public ItemStack getSlot(int slot, boolean clone) {
        ItemStack itemStack = Optional.ofNullable(this.slots[slot]).orElse(ItemRegistry.getInstance().getItem(BlockTypeID.AIR));
        if (clone) {
            return itemStack.clone();
        } else {
            return itemStack;
        }
    }

    @Override
    public void setSlot(int slot, ItemStack itemStack) {
        this.setSlot(null, slot, itemStack, false);
    }

    /**
     * Change a slot in the inventory.
     * @param player the player who is changing the slot if any exists
     * @param slot the slot changed
     * @param itemStack the new item stack
     * @param keepNetworkId if the network id of the ItemStack should be kept or if a new one should be generated
     */
    public void setSlot(Player player, int slot, ItemStack itemStack, boolean keepNetworkId) {
        this.slots[slot] = keepNetworkId ? itemStack : ItemStack.ensureItemStackExists(itemStack).newNetworkStack();

        for (Player viewer : this.getViewers()) {
            if (!viewer.equals(player)) {
                sendInventorySlot(viewer, this.getSlot(slot), slot, this.getId());
            }
        }
    }

    @Override
    public Optional<ItemStack> addItem(ItemStack itemStack) {
        ItemStack remainingItemStack = ItemStack.ensureItemStackExists(itemStack.clone());

        for (int slot = 0; slot < this.getSize(); slot++) {
            if (remainingItemStack.getCount() <= 0) {
                break;
            }

            ItemStack slotStack = this.getSlot(slot);
            int maxStackCount = remainingItemStack.getItemType().getMaxStackSize();
            int spaceLeft = Math.max(maxStackCount - slotStack.getCount(), 0);
            int addedAmount = Math.min(spaceLeft, remainingItemStack.getCount());
            if (slotStack.isEmpty()) {
                ItemStack newSlot = remainingItemStack.clone();
                newSlot.setCount(addedAmount);
                this.setSlot(slot, newSlot);
                remainingItemStack.setCount(remainingItemStack.getCount() - addedAmount);
            } else if (slotStack.hasSameDataAs(remainingItemStack)) {
                slotStack.setCount(slotStack.getCount() + addedAmount);
                this.setSlot(slot, slotStack);
                remainingItemStack.setCount(remainingItemStack.getCount() - addedAmount);
            }
        }

        if (remainingItemStack.isEmpty()) {
            return Optional.empty();
        } else {
            return Optional.of(remainingItemStack);
        }
    }

    @Override
    public Set<ItemStack> addItems(Collection<ItemStack> itemStacks) {
        return this.addItems(itemStacks.toArray(new ItemStack[0]));
    }

    @Override
    public Set<ItemStack> addItems(ItemStack... itemStacks) {
        Set<ItemStack> failedToAddItems = new HashSet<>();
        for (ItemStack itemStack : itemStacks) {
            this.addItem(itemStack).ifPresent(failedToAddItems::add);
        }
        return failedToAddItems;
    }

    @Override
    public void sendSlots(Player player) {
        sendInventorySlots(player, this.getSlots(), this.getId());
    }

    @Override
    public void sendSlot(Player player, int slot) {
        sendInventorySlot(player, this.getSlot(slot), slot, this.getId());
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return !this.viewers.contains(player);
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

    @Override
    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(this.viewers);
    }

    @Override
    public int getExcessIfAdded(ItemStack itemStack) {
        ItemStack remainingItemStack = ItemStack.ensureItemStackExists(itemStack.clone());

        for (int slot = 0; slot < this.getSize(); slot++) {
            if (remainingItemStack.getCount() <= 0) {
                break;
            }

            ItemStack slotStack = this.getSlot(slot);
            int maxStackCount = remainingItemStack.getItemType().getMaxStackSize();
            int spaceLeft = Math.max(maxStackCount - slotStack.getCount(), 0);
            int addedAmount = Math.min(spaceLeft, remainingItemStack.getCount());
            if (slotStack.isEmpty() || slotStack.hasSameDataAs(remainingItemStack)) {
                remainingItemStack.setCount(remainingItemStack.getCount() - addedAmount);
            }
        }

        return remainingItemStack.getCount();
    }

    /**
     * Helper method to send a slot of an inventory.
     * @param player player to send the slot to
     * @param itemStack the item stack to send
     * @param slot the slot
     * @param inventoryId the id of the inventory
     */
    protected static void sendInventorySlot(Player player, ItemStack itemStack, int slot, int inventoryId) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setContainerId(inventoryId);
        inventorySlotPacket.setSlot(slot);
        inventorySlotPacket.setItem(ItemUtils.serializeForNetwork(itemStack, player.getVersion()));
        player.sendPacket(inventorySlotPacket);
    }

    /**
     * Helper method to send slots of an inventory.
     * @param player player to send the slots to
     * @param slots the slots
     * @param inventoryId the inventory id
     */
    protected static void sendInventorySlots(Player player, ItemStack[] slots, int inventoryId) {
        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setContainerId(inventoryId);
        inventoryContentPacket.setContents(Arrays.stream(slots).map(itemStack -> ItemUtils.serializeForNetwork(itemStack, player.getVersion())).collect(Collectors.toList()));
        player.sendPacket(inventoryContentPacket);
    }

}

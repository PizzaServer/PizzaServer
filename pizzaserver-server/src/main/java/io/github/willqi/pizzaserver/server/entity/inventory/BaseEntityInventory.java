package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerClosePacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventorySlotPacket;

import java.util.*;

public abstract class BaseEntityInventory implements EntityInventory {

    public static int ID = 1;

    private final Entity entity;
    private final int id;
    private final int size;

    private final ItemStack[] slots;

    private final Set<Player> viewers = new HashSet<>();


    public BaseEntityInventory(Entity entity, int size) {
        this(entity, size, ID++);
    }

    public BaseEntityInventory(Entity entity, int size, int id) {
        this.entity = entity;
        this.size = size;
        this.slots = new ItemStack[size];
        this.id = id;
    }

    @Override
    public int getId() {
        return this.id;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public int getSize() {
        return this.size;
    }

    @Override
    public ItemStack[] getSlots() {
        ItemStack[] slots = new ItemStack[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {
            slots[i] = this.getSlot(i);
        }
        return slots;
    }

    @Override
    public boolean setSlots(ItemStack[] slots) {
        if (slots.length != this.size) {
            throw new IllegalArgumentException("The slots provided must be " + this.size + " in length.");
        }

        if (!Arrays.equals(this.slots, slots)) {
            for (int i = 0 ; i < this.size; i++) {
                this.slots[i] = slots[i].clone();
            }

            for (Player viewer : this.getViewers()) {
                this.sendSlots(viewer);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack getSlot(int slot) {
        return Optional.ofNullable(this.slots[slot]).orElse(ItemRegistry.getItem(BlockTypeID.AIR)).clone();
    }

    @Override
    public boolean setSlot(int slot, ItemStack itemStack) {
        if (isDifferentItems(this.slots[slot], itemStack)) {
            this.slots[slot] = itemStack.clone();
            for (Player viewer : this.getViewers()) {
                sendSlot(viewer, this.getSlot(slot), slot, this.getId());
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Optional<ItemStack> addItem(ItemStack item) {
        return Optional.empty();
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
        ItemStack[] contents = new ItemStack[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {  // TODO: change serializers to include way to get MinecraftVersion to reduce time complexity
            contents[i] = this.getSlot(i);
        }

        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setInventoryId(this.getId());
        inventoryContentPacket.setContents(contents);
        player.sendPacket(inventoryContentPacket);
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return true;
    }

    /**
     * Tries to open the inventory
     * @param player the player to open this inventory to
     * @return if the inventory was opened
     */
    public boolean openFor(Player player) {
        if (this.viewers.add(player)) {
            this.sendContainerOpenPacket(player);
            return true;
        } else {
            return false;
        }
    }

    protected abstract void sendContainerOpenPacket(Player player);

    /**
     * Close this inventory for a player
     * @param player the player to close this inventory for
     * @return if the inventory was closed
     */
    public boolean closeFor(Player player) {
        if (this.viewers.remove(player)) {
            ContainerClosePacket containerClosePacket = new ContainerClosePacket();
            containerClosePacket.setInventoryId(this.getId());
            player.sendPacket(containerClosePacket);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public Set<Player> getViewers() {
        return Collections.unmodifiableSet(this.viewers);
    }

    protected static boolean isAir(ItemStack itemStack) {
        return (itemStack == null) || itemStack.getItemType().getItemId().equals(BlockTypeID.AIR);
    }

    protected static boolean isDifferentItems(ItemStack itemStackA, ItemStack itemStackB) {
        return !Objects.equals(itemStackA, itemStackB) && !(isAir(itemStackA) == isAir(itemStackB));
    }

    protected static void sendSlot(Player player, ItemStack itemStack, int slot, int inventoryId) {
        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setInventoryId(inventoryId);
        inventorySlotPacket.setSlot(slot);
        inventorySlotPacket.setItem(itemStack);
        player.sendPacket(inventorySlotPacket);
    }

}

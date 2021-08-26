package io.github.willqi.pizzaserver.server.entity.inventory;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.inventory.EntityInventory;
import io.github.willqi.pizzaserver.api.item.ItemRegistry;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.level.world.blocks.types.BlockTypeID;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.server.network.protocol.data.NetworkItemStackData;
import io.github.willqi.pizzaserver.server.network.protocol.packets.ContainerClosePacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventoryContentPacket;
import io.github.willqi.pizzaserver.server.network.protocol.packets.InventorySlotPacket;

import java.util.*;

public abstract class BaseEntityInventory implements EntityInventory {

    public static int ID = 1;

    private final Entity entity;
    private final int id;
    private final int size;

    private ItemStack[] slots;

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
        return this.slots;
    }

    @Override
    public boolean setSlots(ItemStack[] slots) {
        if (slots.length != this.size) {
            throw new IllegalArgumentException("The slots provided must be " + this.size + " in length.");
        }

        if (!Arrays.equals(this.slots, slots)) {
            this.slots = slots;
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
        return Optional.ofNullable(this.getSlots()[slot]).orElse(ItemRegistry.getItem(BlockTypeID.AIR));
    }

    @Override
    public boolean setSlot(int slot, ItemStack itemStack) {
        if (isDifferentItems(this.slots[slot], itemStack)) {
            this.slots[slot] = itemStack;
            for (Player viewer : this.getViewers()) {
                this.sendSlot(viewer, slot);
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

    protected void sendSlot(Player player, int slot) {
        ItemStack itemStack = this.getSlot(slot);

        InventorySlotPacket inventorySlotPacket = new InventorySlotPacket();
        inventorySlotPacket.setInventoryId(this.getId());
        inventorySlotPacket.setSlot(slot);
        inventorySlotPacket.setItemStackData(new NetworkItemStackData(itemStack, player.getVersion().getItemRuntimeId(itemStack.getItemType().getItemId())));
        player.sendPacket(inventorySlotPacket);
    }

    @Override
    public void sendSlots(Player player) {
        NetworkItemStackData[] contents = new NetworkItemStackData[this.getSize()];
        for (int i = 0; i < this.getSize(); i++) {  // TODO: change serializers to include way to get MinecraftVersion to reduce time complexity
            ItemStack itemStack = this.getSlot(i);
            contents[i] = new NetworkItemStackData(itemStack, player.getVersion().getItemRuntimeId(itemStack.getItemType().getItemId()));
        }

        InventoryContentPacket inventoryContentPacket = new InventoryContentPacket();
        inventoryContentPacket.setInventoryId(this.getId());
        inventoryContentPacket.setContents(contents);
        player.sendPacket(inventoryContentPacket);
    }

    @Override
    public boolean openFor(Player player) {
        if (this.viewers.add(player)) {
            this.sendContainerOpenPacket(player);
            return true;
        } else {
            return false;
        }
    }

    protected abstract void sendContainerOpenPacket(Player player);

    @Override
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

}

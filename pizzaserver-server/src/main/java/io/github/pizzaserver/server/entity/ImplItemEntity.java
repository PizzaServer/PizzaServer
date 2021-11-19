package io.github.pizzaserver.server.entity;

import com.nukkitx.protocol.bedrock.packet.AddItemEntityPacket;
import com.nukkitx.protocol.bedrock.packet.TakeItemEntityPacket;
import io.github.pizzaserver.api.entity.ItemEntity;
import io.github.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.pizzaserver.api.event.type.entity.EntityPickupItemEvent;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.player.Player;

import java.util.Optional;

public class ImplItemEntity extends ImplEntity implements ItemEntity {

    protected ItemStack itemStack;
    protected int pickUpDelay;


    public ImplItemEntity(EntityDefinition entityDefinition) {
        super(entityDefinition);
    }

    @Override
    public ItemStack getItem() {
        return this.itemStack;
    }

    @Override
    public void setItem(ItemStack itemStack) {
        this.itemStack = itemStack;

        // We cannot change the item entity displayed without respawning the item.
        for (Player player : this.getViewers()) {
            this.despawnFrom(player);
            this.spawnTo(player);
        }
    }

    @Override
    public void setPickupDelay(int ticks) {
        this.pickUpDelay = ticks;
    }

    @Override
    public int getPickupDelay() {
        return this.pickUpDelay;
    }

    @Override
    public void tick() {
        super.tick();

        if (this.getPickupDelay() > 0) {
            this.pickUpDelay--;
        } else if (this.getItem().getCount() > 0) {
            for (Player player : this.getWorld().getPlayers()) {
                if (player.getLocation().distanceBetween(this.getLocation()) <= 1) {
                    if (this.getPickupDelay() <= 0) {
                        int pickedUpCount = this.getItem().getCount() - player.getInventory().getExcessIfAdded(this.getItem());
                        if (pickedUpCount <= 0) {
                            continue;
                        }

                        EntityPickupItemEvent pickupItemEvent = new EntityPickupItemEvent(player, this, pickedUpCount);
                        this.getServer().getEventManager().call(pickupItemEvent);
                        if (pickupItemEvent.isCancelled())  {
                            continue;
                        }

                        TakeItemEntityPacket takeItemEntityPacket = new TakeItemEntityPacket();
                        takeItemEntityPacket.setItemRuntimeEntityId(this.getId());
                        takeItemEntityPacket.setRuntimeEntityId(player.getId());
                        for (Player viewer : this.getViewers()) {
                            viewer.sendPacket(takeItemEntityPacket);
                        }

                        ItemStack stackToAdd = this.getItem().clone();
                        stackToAdd.setCount(pickupItemEvent.getPickedUpCount());
                        Optional<ItemStack> excessStack = player.getInventory().addItem(stackToAdd);

                        if (excessStack.isPresent()) {
                            this.getItem().setCount(excessStack.get().getCount());
                        } else {
                            this.getItem().setCount(0);
                            this.despawn();
                        }
                        break;
                    }
                }
            }
        }
    }

    @Override
    public boolean canBeSpawnedTo(Player player) {
        return super.canBeSpawnedTo(player) && !this.getItem().isEmpty();
    }

    @Override
    public boolean spawnTo(Player player) {
        if (this.getItem() == null) {
            this.getServer().getLogger().error("Attempted to spawn an item entity with no item.");
            return false;
        }

        if (this.canBeSpawnedTo(player)) {
            this.spawnedTo.add(player);

            AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
            addItemEntityPacket.setUniqueEntityId(this.getId());
            addItemEntityPacket.setRuntimeEntityId(this.getId());
            addItemEntityPacket.setItemStack(this.getItem());
            addItemEntityPacket.getMetadata().putAll(this.getMetaData().serialize());
            addItemEntityPacket.setPosition(this.getLocation().toVector3f());
            addItemEntityPacket.setMotion(this.getMotion());

            player.sendPacket(addItemEntityPacket);
            return true;
        } else {
            return false;
        }
    }

}

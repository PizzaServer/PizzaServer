package io.github.willqi.pizzaserver.server.entity;

import io.github.willqi.pizzaserver.api.entity.ItemEntity;
import io.github.willqi.pizzaserver.api.entity.definition.EntityDefinition;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.network.protocol.packets.AddItemEntityPacket;
import io.github.willqi.pizzaserver.api.player.Player;

public class ImplItemEntity extends ImplEntity implements ItemEntity {

    protected ItemStack itemStack;


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
    public boolean spawnTo(Player player) {
        if (this.getItem() == null) {
            this.getServer().getLogger().error("Attempted to spawn an item entity with no item.");
            return false;
        }

        if (this.canBeSpawnedTo(player)) {
            this.spawnedTo.add(player);

            AddItemEntityPacket addItemEntityPacket = new AddItemEntityPacket();
            addItemEntityPacket.setEntityUniqueId(this.getId());
            addItemEntityPacket.setEntityRuntimeId(this.getId());
            addItemEntityPacket.setItemStack(this.getItem());
            addItemEntityPacket.setMetaData(this.getMetaData());
            addItemEntityPacket.setPosition(this.getLocation());
            addItemEntityPacket.setVelocity(this.getVelocity());

            player.sendPacket(addItemEntityPacket);
            return true;
        } else {
            return false;
        }
    }

}

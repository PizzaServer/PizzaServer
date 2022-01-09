package io.github.pizzaserver.api.item.types.impl;

import com.nukkitx.math.vector.Vector3f;
import com.nukkitx.protocol.bedrock.data.entity.EntityData;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.data.WoodType;
import io.github.pizzaserver.api.block.descriptors.Liquid;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.impl.BoatEntityDefinition;
import io.github.pizzaserver.api.item.ItemStack;
import io.github.pizzaserver.api.item.types.BaseItemType;
import io.github.pizzaserver.api.player.Player;

public class ItemTypeBoat extends BaseItemType {

    protected final String id;
    protected final WoodType woodType;


    public ItemTypeBoat(String id, WoodType woodType) {
        this.id = id;
        this.woodType = woodType;
    }

    @Override
    public String getItemId() {
        return this.id;
    }

    @Override
    public String getName() {
        return this.getWoodType().getName() + " Boat";
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public boolean canClickOnLiquids() {
        return true;
    }

    public WoodType getWoodType() {
        return this.woodType;
    }

    @Override
    public boolean onInteract(Player player, ItemStack itemStack, Block block, BlockFace blockFace) {
        itemStack.setCount(itemStack.getCount() - 1);
        player.getInventory().setHeldItem(itemStack);

        Entity boatEntity = EntityRegistry.getInstance().getEntity(BoatEntityDefinition.ID);
        boatEntity.getMetaData().putInt(EntityData.VARIANT, this.woodType.ordinal());
        Vector3f spawnLocation;
        if (player.getHeadBlock() instanceof Liquid) {
            spawnLocation = player.getLocation().toVector3f().add(0, player.getEyeHeight(), 0);
        } else {
            spawnLocation = block.getSide(blockFace).getLocation().toVector3f();
        }

        player.getWorld().addEntity(boatEntity, spawnLocation);
        return true;
    }

}

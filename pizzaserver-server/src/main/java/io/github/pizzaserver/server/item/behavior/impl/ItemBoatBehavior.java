package io.github.pizzaserver.server.item.behavior.impl;

import org.cloudburstmc.math.vector.Vector3f;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.block.trait.LiquidTrait;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.impl.EntityBoatDefinition;
import io.github.pizzaserver.api.item.behavior.impl.BaseItemBehavior;
import io.github.pizzaserver.api.item.impl.ItemBaseBoat;
import io.github.pizzaserver.api.player.Player;
import org.cloudburstmc.protocol.bedrock.data.entity.EntityDataTypes;

public class ItemBoatBehavior extends BaseItemBehavior<ItemBaseBoat> {

    @Override
    public boolean onInteract(Player player, ItemBaseBoat item, Block block, BlockFace blockFace, Vector3f clickPosition) {
        item.setCount(item.getCount() - 1);
        player.getInventory().setHeldItem(item);

        Entity boatEntity = EntityRegistry.getInstance().getEntity(EntityBoatDefinition.ID);
        // TODO: THIS ONE IS RIGHT I THINK, check all the others later
        boatEntity.getMetaData().putInt(EntityDataTypes.VARIANT, item.getWoodType().ordinal());
        Vector3f spawnLocation;
        if (player.getHeadBlock() instanceof LiquidTrait) {
            spawnLocation = player.getLocation().toVector3f().add(0, player.getEyeHeight(), 0);
        } else {
            spawnLocation = block.getSide(blockFace).getLocation().toVector3f();
        }

        player.getWorld().addEntity(boatEntity, spawnLocation);
        return true;
    }

}

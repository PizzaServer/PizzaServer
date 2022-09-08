package io.github.pizzaserver.server.item.behavior.impl;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.block.data.BlockFace;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.behavior.impl.BaseItemBehavior;
import io.github.pizzaserver.api.item.descriptors.FoodItem;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.ImplServer;

public class ItemFoodBehavior extends BaseItemBehavior<FoodItem> {

    @Override
    public boolean onInteract(Player player, FoodItem item, Block block, BlockFace blockFace, Vector3f clickPosition) {
        if (!item.canAlwaysBeEaten() && player.getFoodLevel() >= 20f) {
            return true;
        }

        //TODO: Set player walk speed as a feature
        if(player.getUseTicks() < item.getUseDurationTicks()) {
            return true;
        }

        ImplServer.getInstance().getLogger().debug("Player ate food");

        //TODO: Fire event?

        item.setCount(item.getCount() - 1);
        player.getInventory().setHeldItem(item);
        if (item.getResultItem().isPresent()) {
            player.getInventory().addItem(item.getResultItem().get());
        }
        item.onConsume(player);

        player.setFoodLevel(Math.min(20, player.getFoodLevel() + item.getNutrition()));
        player.setSaturationLevel(player.getSaturationLevel() + item.getSaturation());

        player.setUseTicks(0);
        return true;
    }

    @Override
    public void onInteract(Player player, FoodItem item, Entity entity) {
        super.onInteract(player, item, entity);
    }
}

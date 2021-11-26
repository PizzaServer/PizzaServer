package io.github.pizzaserver.api.blockentity;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public interface BlockEntity {

    BlockEntityType getType();

    BlockLocation getLocation();

    int getLayer();

    NbtMap getNetworkData();

    NbtMap getDiskData();

    void tick();

    boolean onInteract(Player player);

    /**
     * Called after the block associated with this entity is placed.
     * @param player the player who placed the block
     */
    void onPlace(Player player);

    /**
     * Called right before the block associated with this entity is broken.
     * @param player the player who broke the block
     */
    void onBreak(Player player);

}

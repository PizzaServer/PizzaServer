package io.github.pizzaserver.api.blockentity;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.blockentity.types.BlockEntityType;
import io.github.pizzaserver.api.player.Player;

public interface BlockEntity {

    BlockEntityType getType();

    Vector3i getPosition();

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

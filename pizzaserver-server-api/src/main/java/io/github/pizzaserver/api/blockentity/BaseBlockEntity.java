package io.github.pizzaserver.api.blockentity;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

public abstract class BaseBlockEntity implements BlockEntity {

    protected BlockLocation blockPosition;

    protected boolean updated;


    public BaseBlockEntity(BlockLocation blockPosition) {
        this.blockPosition = blockPosition;
    }

    @Override
    public BlockLocation getLocation() {
        return this.blockPosition;
    }

    @Override
    public void tick() {

    }

    /**
     * Mark this block entity to be updated to viewers.
     */
    public void update() {
        this.updated = true;
    }

    @Override
    public boolean requestedUpdate() {
        if (this.updated) {
            this.updated = false;
            return true;
        }
        return false;
    }

    @Override
    public boolean onInteract(Player player) {
        return true;
    }

    @Override
    public void onPlace(Player player) {

    }

    @Override
    public void onBreak(Player player) {

    }

    @Override
    public NbtMap getNetworkData() {
        return this.getType().serializeForNetwork(this);
    }

    @Override
    public NbtMap getDiskData() {
        return this.getType().serializeForDisk(this);
    }
}

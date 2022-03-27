package io.github.pizzaserver.api.blockentity;

import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.player.Player;

public abstract class BaseBlockEntity<T extends Block> implements BlockEntity<T> {

    protected T block;

    protected boolean updated;


    public BaseBlockEntity(T block) {
        this.block = block;
    }

    @Override
    public T getBlock() {
        return this.block;
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

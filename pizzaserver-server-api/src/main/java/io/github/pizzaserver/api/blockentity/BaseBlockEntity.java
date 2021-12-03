package io.github.pizzaserver.api.blockentity;

import com.nukkitx.math.vector.Vector3i;
import com.nukkitx.nbt.NbtMap;
import io.github.pizzaserver.api.player.Player;

public abstract class BaseBlockEntity implements BlockEntity {

    protected Vector3i blockPosition;


    public BaseBlockEntity(Vector3i blockPosition) {
        this.blockPosition = blockPosition;
    }

    @Override
    public Vector3i getPosition() {
        return this.blockPosition;
    }

    @Override
    public void tick() {

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

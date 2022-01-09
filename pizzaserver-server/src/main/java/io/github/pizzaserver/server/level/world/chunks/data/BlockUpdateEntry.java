package io.github.pizzaserver.server.level.world.chunks.data;

import com.nukkitx.math.vector.Vector3i;
import io.github.pizzaserver.api.block.data.BlockUpdateType;

public class BlockUpdateEntry {

    private final BlockUpdateType type;
    private final Vector3i blockCoordinates;
    private int ticks;

    public BlockUpdateEntry(BlockUpdateType type, Vector3i blockCoordinates, int ticks) {
        this.type = type;
        this.blockCoordinates = blockCoordinates;
        this.ticks = ticks;
    }

    public BlockUpdateType getType() {
        return this.type;
    }

    public Vector3i getBlockCoordinates() {
        return this.blockCoordinates;
    }

    public int getTicksLeft() {
        return this.ticks;
    }

    public void tick() {
        if (this.ticks > 0) {
            this.ticks--;
        }
    }

    @Override
    public int hashCode() {
        return (71 * this.type.hashCode()) + (71 * this.blockCoordinates.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof BlockUpdateEntry) {
            return ((BlockUpdateEntry) obj).getType().equals(this.getType())
                    && ((BlockUpdateEntry) obj).getBlockCoordinates().equals(this.getBlockCoordinates());
        }
        return false;
    }

}

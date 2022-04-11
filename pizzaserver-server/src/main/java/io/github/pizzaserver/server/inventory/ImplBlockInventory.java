package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.inventory.BlockInventory;
import io.github.pizzaserver.api.player.Player;

import java.util.Optional;

public abstract class ImplBlockInventory<T extends Block> extends BaseInventory implements BlockInventory<T> {

    protected final T block;


    public ImplBlockInventory(T block, ContainerType containerType, int size) {
        super(containerType, size);
        this.block = block;
    }

    @Override
    public T getBlock() {
        return this.block;
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return player.canReach(this.getBlock()) && super.canBeOpenedBy(player);
    }

    @Override
    public boolean closeFor(Player player) {
        if (super.closeFor(player)) {
            Optional<BlockEntity<? extends Block>> possibleBlockEntity = this.getBlock().getWorld().getBlockEntity(this.getBlock().getLocation().toVector3i());

            boolean sendCloseContainerEvent = possibleBlockEntity.filter(blockEntity -> blockEntity instanceof BlockEntityContainer).isPresent()
                    && this.getViewers().isEmpty();

            if (sendCloseContainerEvent) {
                player.getWorld().addBlockEvent(this.getBlock().getLocation().toVector3i(), 1, 0);
            }

            return true;
        }
        return false;
    }

    @Override
    public boolean shouldBeClosedFor(Player player) {
        Block currentBlockAtBlockPos = this.getBlock().getLocation().getBlock();

        // check if we can reach the block and that the block id has not changed.
        boolean canInteractWithBlock = player.canReach(this.getBlock())
                && currentBlockAtBlockPos.getBlockId().equals(this.getBlock().getBlockId());

        return !canInteractWithBlock && super.shouldBeClosedFor(player);
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) this.getId());
        containerOpenPacket.setType(this.getContainerType());
        containerOpenPacket.setBlockPosition(this.getBlock().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);

        this.sendSlots(player);
    }

}

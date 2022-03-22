package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityContainer;
import io.github.pizzaserver.api.inventory.BlockInventory;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Optional;

public class ImplBlockInventory extends BaseInventory implements BlockInventory {

    protected final BlockLocation blockLocation;


    public ImplBlockInventory(BlockLocation blockLocation, ContainerType containerType, int size) {
        super(containerType, size);
        this.blockLocation = blockLocation;
    }

    public ImplBlockInventory(BlockLocation blockLocation, ContainerType containerType, int size, int id) {
        super(containerType, size, id);
        this.blockLocation = blockLocation;
    }

    @Override
    public Block getBlock() {
        return this.blockLocation.getBlock();
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return super.canBeOpenedBy(player) && player.getWorld()
                .getBlockEntity(this.getBlock().getLocation().toVector3i())
                .filter(blockEntity -> blockEntity.getType().getBlockIds().contains(this.getBlock().getBlockId()))
                .isPresent();
    }

    @Override
    public boolean closeFor(Player player) {
        if (super.closeFor(player)) {
            Optional<BlockEntity> possibleBlockEntity = this.getBlock().getWorld().getBlockEntity(this.getBlock().getLocation().toVector3i());

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
        return !player.canReach(this.getBlock()) && super.shouldBeClosedFor(player);
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

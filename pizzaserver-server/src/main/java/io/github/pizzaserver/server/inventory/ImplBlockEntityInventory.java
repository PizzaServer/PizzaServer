package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.impl.BlockEntityContainer;
import io.github.pizzaserver.api.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.player.Player;

public class ImplBlockEntityInventory extends BaseInventory implements BlockEntityInventory {

    protected final BlockEntity blockEntity;


    public ImplBlockEntityInventory(BlockEntity blockEntity, ContainerType containerType, int size) {
        super(containerType, size);
        this.blockEntity = blockEntity;
    }

    public ImplBlockEntityInventory(BlockEntity blockEntity, ContainerType containerType, int size, int id) {
        super(containerType, size, id);
        this.blockEntity = blockEntity;
    }

    @Override
    public BlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    @Override
    public boolean canBeOpenedBy(Player player) {
        return super.canBeOpenedBy(player) && player.getWorld()
                .getBlockEntity(this.getBlockEntity().getLocation().toVector3i())
                .filter(otherBlockEntity -> otherBlockEntity.equals(this.getBlockEntity()))
                .isPresent();
    }

    @Override
    public boolean closeFor(Player player) {
        if (super.closeFor(player)) {
            if (this.getBlockEntity() instanceof BlockEntityContainer && this.getViewers().isEmpty()) {
                player.getWorld().addBlockEvent(this.getBlockEntity().getLocation().toVector3i(), 1, 0);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldBeClosedFor(Player player) {
        return !player.canReach(this.getBlockEntity()) && super.shouldBeClosedFor(player);
    }

    @Override
    protected void sendContainerOpenPacket(Player player) {
        ContainerOpenPacket containerOpenPacket = new ContainerOpenPacket();
        containerOpenPacket.setId((byte) this.getId());
        containerOpenPacket.setType(this.getContainerType());
        containerOpenPacket.setBlockPosition(this.getBlockEntity().getLocation().toVector3i());
        player.sendPacket(containerOpenPacket);

        this.sendSlots(player);
    }

}

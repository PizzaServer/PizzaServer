package io.github.pizzaserver.server.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.ContainerType;
import com.nukkitx.protocol.bedrock.packet.ContainerOpenPacket;
import io.github.pizzaserver.api.block.Block;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.blockentity.trait.BlockEntityOpenableTrait;
import io.github.pizzaserver.api.blockentity.type.BlockEntityContainer;
import io.github.pizzaserver.api.inventory.BlockEntityInventory;
import io.github.pizzaserver.api.player.Player;

public abstract class ImplBlockEntityInventory<B extends Block, T extends BlockEntity<B>> extends ImplBlockInventory<B> implements BlockEntityInventory<B, T> {

    protected final T blockEntity;


    public ImplBlockEntityInventory(T blockEntity, ContainerType containerType) {
        super(null, containerType);
        this.blockEntity = blockEntity;
    }

    @Override
    public T getBlockEntity() {
        return this.blockEntity;
    }

    @Override
    public B getBlock() {
        return this.getBlockEntity().getBlock();
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
            if (this.getBlockEntity() instanceof BlockEntityOpenableTrait openableBlockEntity && this.getViewers().isEmpty()) {
                openableBlockEntity.showCloseAnimation();
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

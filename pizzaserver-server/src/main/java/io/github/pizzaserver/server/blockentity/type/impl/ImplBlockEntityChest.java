package io.github.pizzaserver.server.blockentity.type.impl;

import org.cloudburstmc.protocol.bedrock.data.inventory.ContainerType;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockChest;
import io.github.pizzaserver.api.blockentity.type.BlockEntityChest;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;

import java.util.Collections;
import java.util.Set;

public class ImplBlockEntityChest extends ImplBlockEntityContainer<BlockChest> implements BlockEntityChest {

    public static final Set<String> BLOCK_IDS = Collections.singleton(BlockID.CHEST);

    public ImplBlockEntityChest(BlockLocation location) {
        super(location, ContainerType.CONTAINER);
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

    @Override
    public void showOpenAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showOpenAnimation(player);
        }
    }

    @Override
    public void showOpenAnimation(Player player) {
        player.getWorld().addBlockEvent(player, this.getLocation().toVector3i(), 1, 1);
    }

    @Override
    public void showCloseAnimation() {
        for (Player player : this.getLocation().getChunk().getViewers()) {
            this.showCloseAnimation(player);
        }
    }

    @Override
    public void showCloseAnimation(Player player) {
        player.getWorld().addBlockEvent(player, this.getLocation().toVector3i(), 1, 0);
    }

}

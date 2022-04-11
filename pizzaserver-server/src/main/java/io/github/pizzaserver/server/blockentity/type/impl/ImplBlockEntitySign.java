package io.github.pizzaserver.server.blockentity.type.impl;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.block.BlockID;
import io.github.pizzaserver.api.block.impl.BlockSign;
import io.github.pizzaserver.api.blockentity.type.BlockEntitySign;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.BlockLocation;
import io.github.pizzaserver.server.blockentity.type.BaseBlockEntity;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public class ImplBlockEntitySign extends BaseBlockEntity<BlockSign> implements BlockEntitySign {

    public static final Set<String> BLOCK_IDS = Set.of(BlockID.ACACIA_STANDING_SIGN, BlockID.ACACIA_WALL_SIGN,
            BlockID.BIRCH_STANDING_SIGN, BlockID.BIRCH_WALL_SIGN,
            BlockID.CRIMSON_STANDING_SIGN, BlockID.CRIMSON_WALL_SIGN,
            BlockID.DARK_OAK_STANDING_SIGN, BlockID.DARK_OAK_WALL_SIGN,
            BlockID.JUNGLE_STANDING_SIGN, BlockID.JUNGLE_WALL_SIGN,
            BlockID.OAK_STANDING_SIGN, BlockID.OAK_WALL_SIGN,
            BlockID.SPRUCE_STANDING_SIGN, BlockID.SPRUCE_WALL_SIGN,
            BlockID.WARPED_STANDING_SIGN, BlockID.WARPED_WALL_SIGN);

    private UUID owner;
    private String text = "";

    public ImplBlockEntitySign(BlockLocation location) {
        super(location);
    }

    @Override
    public Optional<Player> getEditor() {
        return Server.getInstance().getPlayerByUUID(this.owner);
    }

    @Override
    public void setEditor(Player player) {
        if (player != null) {
            this.owner = player.getUUID();
        } else {
            this.owner = null;
        }
    }

    @Override
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(String text) {
        this.text = text;
        this.update();
    }

    @Override
    public Set<String> getBlockIds() {
        return BLOCK_IDS;
    }

}

package io.github.pizzaserver.api.blockentity.type;

import io.github.pizzaserver.api.block.impl.BlockSign;
import io.github.pizzaserver.api.blockentity.BlockEntity;
import io.github.pizzaserver.api.player.Player;

import java.util.Optional;

public interface BlockEntitySign extends BlockEntity<BlockSign> {

    String ID = "Sign";

    @Override
    default String getId() {
        return ID;
    }

    /**
     * Get the current player who can edit this sign.
     * @return the player
     */
    Optional<Player> getEditor();

    /**
     * Set the current player who can edit this sign.
     * @param player the player editing the sign
     */
    void setEditor(Player player);

    String getText();

    void setText(String text);

}

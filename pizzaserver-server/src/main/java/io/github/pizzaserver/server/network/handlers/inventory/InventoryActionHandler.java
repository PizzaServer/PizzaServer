package io.github.pizzaserver.server.network.handlers.inventory;

import com.nukkitx.protocol.bedrock.data.inventory.stackrequestactions.StackRequestActionData;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.server.network.data.inventory.actions.StackRequestActionWrapper;

public abstract class InventoryActionHandler<T extends StackRequestActionWrapper<? extends StackRequestActionData>> {

    protected InventoryActionHandler() {}

    /**
     * Returns if the action is valid. (slots exist and action makes sense to be sent)
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action is valid
     */
    protected abstract boolean isValid(Player player, T action);

    /**
     * Run the action after validating it.
     * The action should be validated before this is called
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action went through successfully
     */
    protected abstract boolean runAction(Player player, T action);

    /**
     * Validates the action before running it.
     * @param player player who sent the request
     * @param action action being validated
     * @return if the action went through successfully
     */
    public boolean tryAction(Player player, T action) {
        return this.isValid(player, action) && this.runAction(player, action);
    }

}

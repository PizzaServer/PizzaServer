package io.github.pizzaserver.api.event.type.player;

import io.github.pizzaserver.api.player.Player;

import java.util.Set;

public class PlayerChatEvent extends BasePlayerEvent.Cancellable {

    private String message;
    private Set<Player> recipients;

    public PlayerChatEvent(Player player, String message, Set<Player> recipients) {
        super(player);
        this.message = message;
        this.recipients = recipients;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Set<Player> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(Set<Player> recipients) {
        this.recipients = recipients;
    }

}

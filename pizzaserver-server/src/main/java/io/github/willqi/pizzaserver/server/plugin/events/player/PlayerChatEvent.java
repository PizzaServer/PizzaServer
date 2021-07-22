package io.github.willqi.pizzaserver.server.plugin.events.player;

import io.github.willqi.pizzaserver.api.player.APIPlayer;
import io.github.willqi.pizzaserver.server.plugin.events.Cancellable;

import java.util.Set;

public class PlayerChatEvent extends PlayerEvent implements Cancellable {

    private String message;
    private Set<APIPlayer> recipients;

    public PlayerChatEvent(APIPlayer player, String message, Set<APIPlayer> recipients) {
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

    public Set<APIPlayer> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(Set<APIPlayer> recipients) {
        this.recipients = recipients;
    }

}

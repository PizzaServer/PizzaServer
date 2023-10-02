package io.github.pizzaserver.api.event.type.entity;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.item.Item;
import io.github.pizzaserver.api.player.Player;
import io.github.pizzaserver.api.utils.TextMessage;
import org.cloudburstmc.protocol.bedrock.packet.TextPacket;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Called when an entity dies.
 */
public class EntityDeathEvent extends BaseEntityEvent {

    protected TextMessage deathMessage;
    protected List<Item> drops;
    protected Set<Player> recipients;

    public EntityDeathEvent(Entity entity, List<Item> drops, TextMessage deathMessage, Set<Player> recipients) {
        super(entity);
        this.drops = drops;
        this.deathMessage = deathMessage;
        this.recipients = recipients;
    }

    public Optional<TextMessage> getDeathMessage() {
        return Optional.ofNullable(this.deathMessage);
    }

    public void setDeathMessage(TextMessage deathMessage) {
        this.deathMessage = deathMessage;
    }

    public void setDeathMessage(String message) {
        this.deathMessage = new TextMessage.Builder()
                .setType(TextPacket.Type.RAW)
                .setMessage(message)
                .build();
    }

    public List<Item> getDrops() {
        return this.drops;
    }

    public void setDrops(List<Item> drops) {
        this.drops = drops;
    }

    public Set<Player> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(Set<Player> recipients) {
        this.recipients = recipients;
    }

}

package io.github.willqi.pizzaserver.api.event.type.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.item.ItemStack;
import io.github.willqi.pizzaserver.api.player.Player;
import io.github.willqi.pizzaserver.api.utils.TextMessage;
import io.github.willqi.pizzaserver.api.utils.TextType;

import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * Called when an entity dies.
 */
public class EntityDeathEvent extends BaseEntityEvent {

    protected TextMessage deathMessage;
    protected List<ItemStack> drops;
    protected Set<Player> recipients;

    public EntityDeathEvent(Entity entity, List<ItemStack> drops, TextMessage deathMessage, Set<Player> recipients) {
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
                .setType(TextType.RAW)
                .setMessage(message)
                .build();
    }

    public List<ItemStack> getDrops() {
        return this.drops;
    }

    public void setDrops(List<ItemStack> drops) {
        this.drops = drops;
    }

    public Set<Player> getRecipients() {
        return this.recipients;
    }

    public void setRecipients(Set<Player> recipients) {
        this.recipients = recipients;
    }

}

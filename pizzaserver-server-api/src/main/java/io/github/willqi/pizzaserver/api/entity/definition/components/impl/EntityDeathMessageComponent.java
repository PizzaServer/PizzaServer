package io.github.willqi.pizzaserver.api.entity.definition.components.impl;

import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponent;

/**
 * Entity component that defines if death messages should appear in chat when this entity dies.
 */
public class EntityDeathMessageComponent extends EntityComponent {

    private final boolean enabled;

    public EntityDeathMessageComponent(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean showDeathMessages() {
        return this.enabled;
    }

    @Override
    public String getName() {
        return "pizzaserver:death_message";
    }

}

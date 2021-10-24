package io.github.willqi.pizzaserver.api.event.type.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.data.DamageCause;

/**
 * Abstract class that is used for entity damage events.
 */
public abstract class EntityDamageEvent extends BaseEntityEvent.Cancellable {

    protected DamageCause cause;
    protected float damage;

    public EntityDamageEvent(Entity entity, DamageCause cause, float damage) {
        super(entity);
        this.cause = cause;
        this.damage = damage;
    }

    public DamageCause getCause() {
        return this.cause;
    }

    public float getDamage() {
        return this.damage;
    }

}

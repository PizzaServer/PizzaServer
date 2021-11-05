package io.github.willqi.pizzaserver.api.event.type.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.data.DamageCause;

/**
 * Called for generic events that cause entity damage.
 */
public class EntityDamageEvent extends BaseEntityEvent.Cancellable {

    protected DamageCause cause;
    protected float damage;
    protected int noHitTicks;

    public EntityDamageEvent(Entity entity, DamageCause cause, float damage, int noHitTicks) {
        super(entity);
        this.cause = cause;
        this.damage = damage;
        this.noHitTicks = noHitTicks;
    }

    public DamageCause getCause() {
        return this.cause;
    }

    public float getDamage() {
        return this.damage;
    }

    public void setDamage(float damage) {
        this.damage = damage;
    }

    public int getNoHitTicks() {
        return this.noHitTicks;
    }

    public void setNoHitTicks(int noHitTicks) {
        this.noHitTicks = noHitTicks;
    }

}

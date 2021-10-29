package io.github.willqi.pizzaserver.api.event.type.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.data.DamageCause;
import io.github.willqi.pizzaserver.commons.utils.Vector3;

/**
 * Entity event called whenever an entity damages another entity.
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {

    protected Entity attacker;
    protected Vector3 knockback;

    public EntityDamageByEntityEvent(Entity entity, Entity attacker, DamageCause cause, float damage, int noHitTicks, Vector3 knockback) {
        super(entity, cause, damage, noHitTicks);
        this.attacker = attacker;
        this.knockback = knockback;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public Vector3 getKnockback() {
        return this.knockback;
    }

    public void setKnockback(Vector3 knockback) {
        this.knockback = knockback;
    }

}

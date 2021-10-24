package io.github.willqi.pizzaserver.api.event.type.entity;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.data.DamageCause;

/**
 * Entity event called whenever an entity damages another entity.
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {

    protected Entity attacker;

    public EntityDamageByEntityEvent(Entity entity, Entity attacker, DamageCause cause, float damage, int noHitTicks) {
        super(entity, cause, damage, noHitTicks);
        this.attacker = attacker;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

}

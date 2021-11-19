package io.github.pizzaserver.api.event.type.entity;

import com.nukkitx.math.vector.Vector3f;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.data.DamageCause;

/**
 * Entity event called whenever an entity damages another entity.
 */
public class EntityDamageByEntityEvent extends EntityDamageEvent {

    protected Entity attacker;
    protected Vector3f knockback;

    public EntityDamageByEntityEvent(Entity entity, Entity attacker, DamageCause cause, float damage, int noHitTicks, Vector3f knockback) {
        super(entity, cause, damage, noHitTicks);
        this.attacker = attacker;
        this.knockback = knockback;
    }

    public Entity getAttacker() {
        return this.attacker;
    }

    public Vector3f getKnockback() {
        return this.knockback;
    }

    public void setKnockback(Vector3f knockback) {
        this.knockback = knockback;
    }

}

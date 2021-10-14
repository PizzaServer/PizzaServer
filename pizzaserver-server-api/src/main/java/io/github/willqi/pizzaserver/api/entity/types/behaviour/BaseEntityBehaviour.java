package io.github.willqi.pizzaserver.api.entity.types.behaviour;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;

public abstract class BaseEntityBehaviour implements EntityBehaviour {

    protected Entity entity;


    public BaseEntityBehaviour(Entity entity) {
        this.entity = entity;
    }

    @Override
    public Entity getEntity() {
        return this.entity;
    }

    @Override
    public void tick() {}

    @Override
    public void onBehaviourExit() {}

    @Override
    public void onDeath() {}

    @Override
    public void onSpawned(Player player) {}

    @Override
    public void onDespawned(Player player) {}

}

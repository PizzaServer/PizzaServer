package io.github.willqi.pizzaserver.api.entity.types.behaviour;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.player.Player;

public interface EntityBehaviour {

    Entity getEntity();

    void tick();

    void onBehaviourExit();

    void onDeath();

    void onSpawned(Player player);

    void onDespawned(Player player);

}

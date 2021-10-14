package io.github.willqi.pizzaserver.api.entity.types.behaviour.impl;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.HumanEntity;
import io.github.willqi.pizzaserver.api.entity.types.behaviour.BaseEntityBehaviour;
import io.github.willqi.pizzaserver.api.player.Player;

public class HumanEntityBehaviour extends BaseEntityBehaviour {

    public HumanEntityBehaviour(Entity entity) {
        super(entity);
    }

    @Override
    public void onSpawned(Player player) {
        HumanEntity humanEntity = (HumanEntity) this.getEntity();
        player.getPlayerList().addEntry(humanEntity.getPlayerListEntry());

        // TODO: AddPlayerPacket

        if (!(humanEntity instanceof Player)) {
            player.getPlayerList().removeEntry(humanEntity.getPlayerListEntry());
        }
    }

}

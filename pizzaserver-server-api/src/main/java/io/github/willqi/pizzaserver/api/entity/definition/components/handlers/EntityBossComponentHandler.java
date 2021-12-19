package io.github.willqi.pizzaserver.api.entity.definition.components.handlers;

import io.github.willqi.pizzaserver.api.entity.Entity;
import io.github.willqi.pizzaserver.api.entity.boss.BossBar;
import io.github.willqi.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.willqi.pizzaserver.api.entity.definition.components.impl.EntityBossComponent;

public class EntityBossComponentHandler extends EntityComponentHandler<EntityBossComponent> {

    @Override
    public void onRegistered(Entity entity, EntityBossComponent component) {
        BossBar bossBar = null;
        if (component.getRange() != -1) {
            bossBar = new BossBar();
            bossBar.setTitle(component.getBossName().orElse(entity.getName()));
            bossBar.setDarkenSky(component.shouldDarkenSky());
            bossBar.setPercentage(entity.getHealth() / (entity.getMaxHealth() != 0 ? entity.getMaxHealth() : 1));
            bossBar.setRenderRange(component.getRange());
        }

        entity.setBossBar(bossBar);
    }

    @Override
    public void onUnregistered(Entity entity, EntityBossComponent component) {
        if (!entity.hasComponent(EntityBossComponent.class)) {
            entity.setBossBar(null);
        }
    }

}

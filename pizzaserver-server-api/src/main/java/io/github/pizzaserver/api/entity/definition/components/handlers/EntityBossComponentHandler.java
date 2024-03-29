package io.github.pizzaserver.api.entity.definition.components.handlers;

import io.github.pizzaserver.api.Server;
import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.boss.BossBar;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentHandler;
import io.github.pizzaserver.api.entity.definition.components.impl.EntityBossComponent;

public class EntityBossComponentHandler extends EntityComponentHandler<EntityBossComponent> {

    @Override
    public void onRegistered(Entity entity, EntityBossComponent component) {
        BossBar bossBar = null;
        if (component.isEnabled()) {
            bossBar = Server.getInstance().createBossBar();
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

package io.github.pizzaserver.api.entity.definition;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.api.entity.EntityRegistry;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentEvent;
import io.github.pizzaserver.api.entity.definition.components.EntityComponentGroup;
import io.github.pizzaserver.api.entity.definition.spawnrules.EntitySpawnRules;

import java.util.HashMap;
import java.util.Map;

public abstract class BaseEntityDefinition implements EntityDefinition {

    private static int ID = 1;

    private final Map<String, EntityComponentGroup> componentGroups = new HashMap<>();
    private final Map<String, EntityComponentEvent> events = new HashMap<>();

    private final int runtimeId;


    public BaseEntityDefinition() {
        this.runtimeId = ID++;
    }

    @Override
    public int getId() {
        return this.runtimeId;
    }

    @Override
    public boolean isSummonable() {
        return true;
    }

    @Override
    public boolean hasSpawnEgg() {
        return true;
    }

    @Override
    public EntityComponentGroup getComponentGroup(String groupId) {
        EntityComponentGroup group = this.componentGroups.getOrDefault(groupId, null);
        if (group == null) {
            throw new NullPointerException("There is no registered component group by the id: " + groupId);
        }

        return group;
    }

    public void registerComponentGroup(EntityComponentGroup group) {
        this.componentGroups.put(group.getGroupId(), group);
    }

    @Override
    public EntityComponentEvent getEvent(String eventId) {
        EntityComponentEvent event = this.events.getOrDefault(eventId, null);
        if (event == null) {
            throw new NullPointerException("There is no registered event by the id: " + eventId);
        }

        return event;
    }

    public void registerEvent(EntityComponentEvent event) {
        this.events.put(event.getId(), event);
    }

    @Override
    public EntitySpawnRules getSpawnRules() {
        return null;
    }

    @Override
    public Entity create() {
        return EntityRegistry.getInstance().getEntity(this.getEntityId());
    }

    @Override
    public void onCreation(Entity entity) {}

}

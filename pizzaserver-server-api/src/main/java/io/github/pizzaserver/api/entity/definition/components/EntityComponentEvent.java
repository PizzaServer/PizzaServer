package io.github.pizzaserver.api.entity.definition.components;

import io.github.pizzaserver.api.entity.Entity;

import java.util.HashSet;
import java.util.Set;

/**
 * Represents an event that removes/adds component groups when triggered.
 */
public class EntityComponentEvent {

    private final String eventId;
    private final Set<String> addGroups = new HashSet<>();
    private final Set<String> removeGroups = new HashSet<>();


    public EntityComponentEvent(String eventId) {
        this.eventId = eventId;
    }

    public String getId() {
        return this.eventId;
    }

    public EntityComponentEvent addComponentGroup(String groupId) {
        this.addGroups.add(groupId);
        return this;
    }

    public EntityComponentEvent removeComponentGroup(String groupId) {
        this.removeGroups.add(groupId);
        return this;
    }

    public void trigger(Entity entity) {
        for (String removeGroup : this.removeGroups) {
            entity.removeComponentGroup(removeGroup);
        }
        for (String addGroup : this.addGroups) {
            entity.addComponentGroup(addGroup);
        }
    }
}

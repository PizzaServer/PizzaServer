package io.github.willqi.pizzaserver.api.entity.definition;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Represents an event that removes/adds component groups when triggered.
 */
public class MinecraftComponentEvent {

    private final String eventId;
    private final Set<String> addGroups = new HashSet<>();
    private final Set<String> removeGroups = new HashSet<>();


    public MinecraftComponentEvent(String eventId) {
        this.eventId = eventId;
    }

    public String getId() {
        return this.eventId;
    }

    public Set<String> getAddComponentGroupIds() {
        return Collections.unmodifiableSet(this.addGroups);
    }

    public Set<String> getRemoveComponentGroupIds() {
        return Collections.unmodifiableSet(this.removeGroups);
    }

    public MinecraftComponentEvent addComponentGroup(String groupId) {
        this.addGroups.add(groupId);
        return this;
    }

    public MinecraftComponentEvent removeComponentGroup(String groupId) {
        this.removeGroups.add(groupId);
        return this;
    }

}

package io.github.pizzaserver.api.entity.definition.components.filter;

import io.github.pizzaserver.api.entity.definition.components.EntityComponentEvent;

import java.util.Optional;
import java.util.function.Predicate;

public class EntityFilter implements Predicate<EntityFilterData> {

    private final Predicate<EntityFilterData> predicate;
    private final String triggerEventId;

    public EntityFilter(Predicate<EntityFilterData> predicate) {
        this(predicate, (String) null);
    }

    public EntityFilter(Predicate<EntityFilterData> predicate, EntityComponentEvent triggerEvent) {
        this(predicate, triggerEvent.getId());
    }

    public EntityFilter(Predicate<EntityFilterData> predicate, String triggerEventId) {
        this.predicate = predicate;
        this.triggerEventId = triggerEventId;
    }

    @Override
    public boolean test(EntityFilterData data) {
        return this.predicate.test(data);
    }

    public Optional<String> getTriggerEventId() {
        return Optional.ofNullable(this.triggerEventId);
    }

}

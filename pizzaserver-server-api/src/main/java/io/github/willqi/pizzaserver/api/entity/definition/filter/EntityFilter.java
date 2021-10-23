package io.github.willqi.pizzaserver.api.entity.definition.filter;

import java.util.function.Predicate;

public class EntityFilter implements Predicate<EntityFilterData> {

    private final Predicate<EntityFilterData> predicate;

    public EntityFilter(Predicate<EntityFilterData> predicate) {
        this.predicate = predicate;
    }

    @Override
    public boolean test(EntityFilterData data) {
        return this.predicate.test(data);
    }

}

package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.commons.data.key.DataKey;
import io.github.pizzaserver.commons.data.value.ValueContainer;
import io.github.pizzaserver.commons.data.value.ValueInterface;
import io.github.pizzaserver.commons.utils.Check;

import java.util.*;
import java.util.function.Function;

public class AttributeTemplate<T extends Number> {

    private final DataKey<T> type;
    private final Set<DataKey<T>> dependentKeys;

    private final Function<Entity, ValueInterface<T>> minimumValueInterface;
    private final Function<Entity, ValueInterface<T>> maximumValueInterface;
    private final Function<Entity, ValueInterface<T>> defaultValueInterface;
    private final Function<Entity, ValueInterface<T>> currentValueInterface;

    private AttributeTemplate(DataKey<T> type, Set<DataKey<T>> dependentKeys, Function<Entity, ValueInterface<T>> min, Function<Entity, ValueInterface<T>> max, Function<Entity, ValueInterface<T>> def, Function<Entity, ValueInterface<T>> curr) {
        this.type = type;
        this.dependentKeys = Collections.unmodifiableSet(dependentKeys);
        this.minimumValueInterface = Check.notNull(min, "Attrib Template: Min Value Provider");
        this.maximumValueInterface = Check.notNull(max, "Attrib Template: Max Value Provider");
        this.defaultValueInterface = Check.notNull(def, "Attrib Template: Default Value Provider");
        this.currentValueInterface = Check.notNull(curr, "Attrib Template: Current Value Provider");
    }

    public DataKey<T> getType() {
        return this.type;
    }

    public Optional<AttributeView<T>> using(Entity entity) {
        for(DataKey<T> key: this.getDependentKeys()) {
            if(!entity.has(key))
                entity.getServer()
                        .getLogger()
                        .warn(String.format(
                                "Entity requires the attribute '%s' but was missing the data '%s'",
                                this.getType().getKey(),
                                key.getKey()
                        ));

            return Optional.empty();
        }

        return Optional.of(new AttributeView(
                type,
                minimumValueInterface.apply(entity),
                maximumValueInterface.apply(entity),
                defaultValueInterface.apply(entity),
                currentValueInterface.apply(entity)
        ));
    }

    public Set<DataKey<T>> getDependentKeys() {
        return this.dependentKeys;
    }

    protected static <T extends Number> Builder<T> builder(DataKey<T> id) {
        return new Builder<>(id);
    }


    public static final class Builder<T extends Number> {

        private final DataKey<T> type;

        private Function<Entity, ValueInterface<T>> minimumValueInterface;
        private Function<Entity, ValueInterface<T>> maximumValueInterface;
        private Function<Entity, ValueInterface<T>> defaultValueInterface;

        private DataKey<T> depKeyMinimum;
        private DataKey<T> depKeyMaximum;
        private DataKey<T> depKeyDefault;

        Builder(DataKey<T> id) {
            this.type = id;
        }

        public AttributeTemplate<T> build() {
            HashSet<DataKey<T>> dependentKeys = new HashSet<>();

            dependentKeys.add(type);
            this.getDependentKeyMinimum().ifPresent(dependentKeys::add);
            this.getDependentKeyMaximum().ifPresent(dependentKeys::add);
            this.getDependentKeyDefault().ifPresent(dependentKeys::add);

            return new AttributeTemplate<>(
                    this.type,
                    dependentKeys,
                    this.minimumValueInterface,
                    this.maximumValueInterface,
                    this.defaultValueInterface,
                    entity -> entity.getProxy(this.type).orElseThrow()
            );
        }

        public Builder<T> min(T value) {
            this.minimumValueInterface = ignored -> ValueContainer.wrap(value);
            this.depKeyMinimum = null;
            return this;
        }

        public Builder<T> min(DataKey<T> key) {
            this.minimumValueInterface = entity -> entity.getProxy(key).orElseThrow();
            this.depKeyMinimum = key;
            return this;
        }


        public Builder<T> max(T value) {
            this.maximumValueInterface = ignored -> ValueContainer.wrap(value);
            this.depKeyMaximum = null;
            return this;
        }

        public Builder<T> max(DataKey<T> key) {
            this.maximumValueInterface = entity -> entity.getProxy(key).orElseThrow();
            this.depKeyMaximum = key;
            return this;
        }


        public Builder<T> defaults(T value) {
            this.defaultValueInterface = ignored -> ValueContainer.wrap(value);
            this.depKeyDefault = null;
            return this;
        }

        public Builder<T> defaults(DataKey<T> key) {
            this.defaultValueInterface = entity -> entity.getProxy(key).orElseThrow();
            this.depKeyDefault = key;
            return this;
        }

        private Optional<DataKey<T>> getDependentKeyMinimum() {
            return Optional.ofNullable(depKeyMinimum);
        }

        private Optional<DataKey<T>> getDependentKeyMaximum() {
            return Optional.ofNullable(depKeyMaximum);
        }

        private Optional<DataKey<T>> getDependentKeyDefault() {
            return Optional.ofNullable(depKeyDefault);
        }
    }

}

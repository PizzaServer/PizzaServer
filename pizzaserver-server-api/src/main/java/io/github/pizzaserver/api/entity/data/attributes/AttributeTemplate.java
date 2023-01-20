package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.commons.data.DataKey;
import io.github.pizzaserver.commons.data.ValueContainer;
import io.github.pizzaserver.commons.data.ValueInterface;
import io.github.pizzaserver.commons.utils.Check;

import java.util.*;
import java.util.function.Function;

public class AttributeTemplate {

    private final DataKey<Float> type;
    private final Set<DataKey<Float>> dependentKeys;

    private final Function<Entity, ValueInterface<Float>> minimumValueInterface;
    private final Function<Entity, ValueInterface<Float>> maximumValueInterface;
    private final Function<Entity, ValueInterface<Float>> defaultValueInterface;
    private final Function<Entity, ValueInterface<Float>> currentValueInterface;

    private AttributeTemplate(DataKey<Float> type, Set<DataKey<Float>> dependentKeys, Function<Entity, ValueInterface<Float>> min, Function<Entity, ValueInterface<Float>> max, Function<Entity, ValueInterface<Float>> def, Function<Entity, ValueInterface<Float>> curr) {
        this.type = type;
        this.dependentKeys = Collections.unmodifiableSet(dependentKeys);
        this.minimumValueInterface = Check.notNull(min, "Attrib Template: Min Value Provider");
        this.maximumValueInterface = Check.notNull(max, "Attrib Template: Max Value Provider");
        this.defaultValueInterface = Check.notNull(def, "Attrib Template: Default Value Provider");
        this.currentValueInterface = Check.notNull(curr, "Attrib Template: Current Value Provider");
    }

    public DataKey<Float> getType() {
        return this.type;
    }

    public Optional<AttributeView> using(Entity entity) {
        for(DataKey<Float> key: this.getDependentKeys()) {
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

    public Set<DataKey<Float>> getDependentKeys() {
        return this.dependentKeys;
    }

    protected static Builder builder(DataKey<Float> id) {
        return new Builder(id);
    }


    public static final class Builder {

        private final DataKey<Float> type;

        private Function<Entity, ValueInterface<Float>> minimumValueInterface;
        private Function<Entity, ValueInterface<Float>> maximumValueInterface;
        private Function<Entity, ValueInterface<Float>> defaultValueInterface;

        private DataKey<Float> depKeyMinimum;
        private DataKey<Float> depKeyMaximum;
        private DataKey<Float> depKeyDefault;

        Builder(DataKey<Float> id) {
            this.type = id;
        }

        public AttributeTemplate build() {
            HashSet<DataKey<Float>> dependentKeys = new HashSet<>();

            dependentKeys.add(type);
            this.getDependentKeyMinimum().ifPresent(dependentKeys::add);
            this.getDependentKeyMaximum().ifPresent(dependentKeys::add);
            this.getDependentKeyDefault().ifPresent(dependentKeys::add);

            return new AttributeTemplate(
                    this.type,
                    dependentKeys,
                    this.minimumValueInterface,
                    this.maximumValueInterface,
                    this.defaultValueInterface,
                    entity -> entity.getProxy(this.type).orElseThrow()
            );
        }

        public Builder min(float value) {
            this.minimumValueInterface = ignored -> ValueContainer.wrap(value);
            this.depKeyMinimum = null;
            return this;
        }

        public Builder min(DataKey<Float> key) {
            this.minimumValueInterface = entity -> entity.getProxy(key).orElseThrow();
            this.depKeyMinimum = key;
            return this;
        }


        public Builder max(float value) {
            this.maximumValueInterface = ignored -> ValueContainer.wrap(value);
            this.depKeyMaximum = null;
            return this;
        }

        public Builder max(DataKey<Float> key) {
            this.maximumValueInterface = entity -> entity.getProxy(key).orElseThrow();
            this.depKeyMaximum = key;
            return this;
        }


        public Builder defaults(float value) {
            this.defaultValueInterface = ignored -> ValueContainer.wrap(value);
            this.depKeyDefault = null;
            return this;
        }

        public Builder defaults(DataKey<Float> key) {
            this.defaultValueInterface = entity -> entity.getProxy(key).orElseThrow();
            this.depKeyDefault = key;
            return this;
        }

        private Optional<DataKey<Float>> getDependentKeyMinimum() {
            return Optional.ofNullable(depKeyMinimum);
        }

        private Optional<DataKey<Float>> getDependentKeyMaximum() {
            return Optional.ofNullable(depKeyMaximum);
        }

        private Optional<DataKey<Float>> getDependentKeyDefault() {
            return Optional.ofNullable(depKeyDefault);
        }
    }

}

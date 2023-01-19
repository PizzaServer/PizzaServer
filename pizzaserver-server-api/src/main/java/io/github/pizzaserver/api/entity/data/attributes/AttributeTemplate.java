package io.github.pizzaserver.api.entity.data.attributes;

import io.github.pizzaserver.api.entity.Entity;
import io.github.pizzaserver.commons.data.DataKey;
import io.github.pizzaserver.commons.data.ValueContainer;
import io.github.pizzaserver.commons.data.ValueInterface;
import io.github.pizzaserver.commons.utils.Check;

import java.util.function.Function;

public class AttributeTemplate {

    private final DataKey<Float> type;

    private final Function<Entity, ValueInterface<Float>> minimumValueInterface;
    private final Function<Entity, ValueInterface<Float>> maximumValueInterface;
    private final Function<Entity, ValueInterface<Float>> defaultValueInterface;
    private final Function<Entity, ValueInterface<Float>> currentValueInterface;

    private AttributeTemplate(DataKey<Float> type, Function<Entity, ValueInterface<Float>> min, Function<Entity, ValueInterface<Float>> max, Function<Entity, ValueInterface<Float>> def, Function<Entity, ValueInterface<Float>> curr) {
        this.type = type;
        this.minimumValueInterface = Check.notNull(min, "Attrib Template: Min Value Provider");
        this.maximumValueInterface = Check.notNull(max, "Attrib Template: Max Value Provider");
        this.defaultValueInterface = Check.notNull(def, "Attrib Template: Default Value Provider");
        this.currentValueInterface = Check.notNull(curr, "Attrib Template: Current Value Provider");
    }

    public AttributeView using(Entity entity) {
        return new AttributeView(
                type,
                minimumValueInterface.apply(entity),
                maximumValueInterface.apply(entity),
                defaultValueInterface.apply(entity),
                currentValueInterface.apply(entity)
        );
    }

    protected static Builder builder(DataKey<Float> id) {
        return new Builder(id);
    }


    public static final class Builder {

        private final DataKey<Float> type;

        private Function<Entity, ValueInterface<Float>> minimumValueInterface;
        private Function<Entity, ValueInterface<Float>> maximumValueInterface;
        private Function<Entity, ValueInterface<Float>> defaultValueInterface;

        Builder(DataKey<Float> id) {
            this.type = id;
        }

        public AttributeTemplate build() {
            return new AttributeTemplate(
                    this.type,
                    this.minimumValueInterface,
                    this.maximumValueInterface,
                    this.defaultValueInterface,
                    entity -> entity.getProxy(this.type).orElseThrow()
            );
        }

        public Builder min(float value) {
            this.minimumValueInterface = ignored -> ValueContainer.wrap(value);
            return this;
        }

        public Builder min(DataKey<Float> key) {
            this.minimumValueInterface = entity -> entity.getProxy(key).orElseThrow();
            return this;
        }


        public Builder max(float value) {
            this.maximumValueInterface = ignored -> ValueContainer.wrap(value);
            return this;
        }

        public Builder max(DataKey<Float> key) {
            this.maximumValueInterface = entity -> entity.getProxy(key).orElseThrow();
            return this;
        }


        public Builder defaults(float value) {
            this.defaultValueInterface = ignored -> ValueContainer.wrap(value);
            return this;
        }

        public Builder defaults(DataKey<Float> key) {
            this.defaultValueInterface = entity -> entity.getProxy(key).orElseThrow();
            return this;
        }
    }

}

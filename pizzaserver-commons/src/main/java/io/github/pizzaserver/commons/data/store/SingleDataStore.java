package io.github.pizzaserver.commons.data.store;

import io.github.pizzaserver.commons.data.DataAction;
import io.github.pizzaserver.commons.data.key.DataKey;
import io.github.pizzaserver.commons.data.react.ActionRootSource;
import io.github.pizzaserver.commons.data.value.ValueContainer;
import io.github.pizzaserver.commons.data.value.ValueProxy;
import io.github.pizzaserver.commons.utils.Check;

import java.util.HashMap;
import java.util.Optional;

public class SingleDataStore extends ActionRootSource implements DataStore {

    private final HashMap<DataKey<?>, ValueContainer<?>> dataRegistry;

    public SingleDataStore() {
        this.dataRegistry = new HashMap<>();
    }


    /**
     * Obtains an entry of data attached to the provided key.
     * @param key a type-driven key.
     * @return the data, if present and not-null - wrapped in an optional.
     * @param <T> the type of the data.
     */
    public <T> Optional<T> get(DataKey<T> key) {
        Optional<ValueContainer<T>> container = this.getContainerFor(key);
        return container.map(ValueContainer::getValue);
    }


    /**
     * Sets the data within the store. If the key was already set, its
     * container is reused, otherwise a new container is created.
     * @param key the type-driven key to associate the value with.
     * @param value the value to set.
     * @param <T> the type of the value.
     */
    public <T> void set(DataKey<T> key, T value) {
        ValueContainer<T> cont = this.getOrCreateContainerFor(key, null);
        cont.setValue(value);
    }

    @Override
    public <T> boolean has(DataKey<T> key) {
        if(key == null) return false;

        boolean hasContainer = this.dataRegistry.containsKey(key);
        boolean notNull = !Check.isNull(this.dataRegistry.get(key).getValue());

        return hasContainer && notNull;
    }

    @Override
    public <T> Optional<ValueProxy<T>> getProxy(DataKey<T> proxy) {
        Optional<ValueContainer<T>> container = this.getContainerFor(proxy);
        return container.map(ValueProxy::new);
    }


    /**
     * Returns the raw container that the data is stored within.
     * @param type the key pointing to the data container (same as the data)
     * @return the container if present - wrapped in an optional
     * @param <T> the type of the data stored within the container.
     */
    @SuppressWarnings("unchecked")
    protected <T> Optional<ValueContainer<T>> getContainerFor(DataKey<T> type) {
        if(!this.hasContainerFor(type))
            return Optional.empty();

        ValueContainer<?> list = this.getRawDataRegistry().get(type);
        return Optional.of((ValueContainer<T>) list);
    }

    /**
     * @param type the key to locate the container for
     * @return true if a container already exists for the given key.
     * @param <T> the type paired with the key & container.
     */
    protected <T> boolean hasContainerFor(DataKey<T> type) {
        return this.getRawDataRegistry().containsKey(type);
    }

    /**
     * Returns the raw container that the data is stored within.
     * @param type the key pointing to the data container (same as the data)
     * @return the container.
     * @param <T> the type of the data stored within the container.
     */
    protected <T> ValueContainer<T> expectContainerFor(DataKey<T> type) {
        return this.getContainerFor(type).orElseThrow();
    }

    /**
     * Returns the raw container that the data is stored within.
     * @param type the key pointing to the data container (same as the data)
     * @return the container, freshly created if not present.
     * @param <T> the type of the data stored within the container.
     */
    protected <T> ValueContainer<T> getOrCreateContainerFor(DataKey<T> type, T fallbackValue) {
        Optional<ValueContainer<T>> container = this.getContainerFor(type);
        if(container.isPresent()) return container.get();

        ValueContainer<T> freshContainer = ValueContainer.wrap(fallbackValue);
        this.getRawDataRegistry().put(type, freshContainer);

        this.broadcast(DataAction.CONTAINER_CREATE, type);

        return freshContainer;
    }


    /** Sets all data to stale as they shouldn't be used. */
    protected void stale() {
        for(ValueContainer<?> container: this.getRawDataRegistry().values())
            container.stale();
    }

    protected HashMap<DataKey<?>, ValueContainer<?>> getRawDataRegistry() {
        return this.dataRegistry;
    }
}

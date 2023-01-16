package io.github.pizzaserver.commons.data;

import java.util.HashMap;
import java.util.Optional;

public class SingleDataStore implements DataStore {

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
     * Obtains an entry of data attached to the provided key, throwing
     * an error if it is null or not present in the store.
     * @param key a type-driven key.
     * @return the data
     * @param <T> the type of the data.
     */
    public <T> T expect(DataKey<T> key) {
        return this.get(key).orElseThrow();
    }

    /**
     * Sets the data within the store. If the key was already set, its
     * container is reused, otherwise a new container is created.
     * @param key the type-driven key to associate the value with.
     * @param value the value to set.
     * @param <T> the type of the value.
     */
    public <T> void set(DataKey<T> key, T value) {
        Optional<ValueContainer<T>> optCont = this.getContainerFor(key);

        if(optCont.isPresent()) {
            ValueContainer<T> container = optCont.get();
            container.setValue(value);
            return;
        }

        this.getDataRegistry().put(key, ValueContainer.wrap(value));
    }


    /**
     * Returns the raw container that the data is stored within.
     * @param type the key pointing to the data container (same as the data)
     * @return the container if present - wrapped in an optional
     * @param <T> the type of the data stored within the container.
     */
    @SuppressWarnings("unchecked")
    public <T> Optional<ValueContainer<T>> getContainerFor(DataKey<T> type) {
        if(!this.getDataRegistry().containsKey(type)) return Optional.empty();

        ValueContainer<?> list = this.getDataRegistry().get(type);
        return Optional.of((ValueContainer<T>) list);
    }


    /** Sets all data to stale as they shouldn't be used. */
    protected void stale() {
        for(ValueContainer<?> container: this.getDataRegistry().values()) {
            container.stale();
        }
    }

    protected HashMap<DataKey<?>, ValueContainer<?>> getDataRegistry() {
        return dataRegistry;
    }
}

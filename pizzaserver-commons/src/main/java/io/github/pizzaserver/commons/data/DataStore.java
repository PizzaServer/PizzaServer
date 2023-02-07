package io.github.pizzaserver.commons.data;

import io.github.pizzaserver.commons.data.react.ActionType;

import java.util.Optional;

public interface DataStore {

    @SuppressWarnings("rawtypes")
    ActionType<DataKey> ACTION_CREATE_CONTAINER = ActionType.of("container_create", DataKey.class);

    /**
     * Obtains an entry of data attached to the provided key.
     * @param key a type-driven key.
     * @return the data, if present and not-null - wrapped in an optional.
     * @param <T> the type of the data.
     */
    <T> Optional<T> get(DataKey<T> key);

    /**
     * Obtains an entry of data attached to the provided key, throwing
     * an error if it is null or not present in the store.
     * @param key a type-driven key.
     * @return the data
     * @param <T> the type of the data.
     */
    default <T> T expect(DataKey<T> key) {
        return this.get(key).orElseThrow();
    }

    /**
     * Sets the data within the store. If the key was already set, its
     * container is reused, otherwise a new container is created.
     * @param key the type-driven key to associate the value with.
     * @param value the value to set.
     * @param <T> the type of the value.
     */
    <T> void set(DataKey<T> key, T value);

    /**
     * Determines whether a key is defined. If the value is null or there
     * is no ValueContainer associated with the key, it returns null.
     * @param key the type-driven key to check.#
     * @return true if the value is non-null and present
     */
    <T> boolean has(DataKey<T> key);

    <T> Optional<ValueProxy<T>> getProxy(DataKey<T> proxy);

    default <T> ValueProxy<T> expectProxy(DataKey<T> proxy) {
        return this.getProxy(proxy).orElseThrow();
    }
}

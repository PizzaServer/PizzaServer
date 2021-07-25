package io.github.willqi.pizzaserver.commons.data.storage;

import io.github.willqi.pizzaserver.commons.utils.Check;

import java.util.Objects;

public final class Key<T> {

    private final String key;

    public Key(String key) {
        String modifiedKey = Check.nullParam(key, "key").trim().toLowerCase();

        if(modifiedKey.length() == 0) throw new IllegalArgumentException("Key cannot be made of only whitespace.");
        this.key = modifiedKey;
    }

    public String get() {
        return key;
    }

    @Override
    public String toString() {
        return key;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Key<?> key1 = (Key<?>) o;
        return Objects.equals(key, key1.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}

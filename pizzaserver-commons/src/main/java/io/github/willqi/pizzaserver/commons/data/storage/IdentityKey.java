package io.github.willqi.pizzaserver.commons.data.storage;

import io.github.willqi.pizzaserver.commons.data.id.Identifier;
import io.github.willqi.pizzaserver.commons.utils.Check;

import java.util.Objects;

public final class IdentityKey<T> {

    private final Identifier key;

    public IdentityKey(Identifier key) {
        this.key = Check.nullParam(key, "key");
    }

    // Just a nicer looking way of creating it.
    public static <T> IdentityKey<T> of(Identifier string) {
        return new IdentityKey<>(string);
    }


    public Identifier get() {
        return key;
    }

    public Key<T> getStringKey() {
        return new Key<>(key.getID());
    }

    @Override
    public String toString() {
        return getStringKey().toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityKey<?> key1 = (IdentityKey<?>) o;
        return Objects.equals(key, key1.key);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}

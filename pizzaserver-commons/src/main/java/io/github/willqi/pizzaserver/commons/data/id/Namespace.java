package io.github.willqi.pizzaserver.commons.data.id;

import io.github.willqi.pizzaserver.commons.utils.Util;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Namespace {

    public static final String MISSING_NAMESPACE = "minecraft";

    private String namespace;

    public Namespace(String namespace) {

        if(namespace == null || namespace.trim().length() == 0 || !namespace.matches("([A-Za-z0-9_-]*)")) {
            this.namespace = MISSING_NAMESPACE;

        } else {
            this.namespace = namespace.trim().toLowerCase();
        }
    }

    /**
     * @param id the id to be tied to the namespace.
     * @return an identifier using this namespace.
     */
    public Identifier id(String id) {
        return new Identifier(this, id);
    }



    /** @return the string of the namespace. */
    public String getNamespaceString() {
        return namespace;
    }


    /** @return the result of Namespace#getNamespaceString() */
    @Override
    public String toString() {
        return getNamespaceString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Namespace namespace1 = (Namespace) o;
        return Objects.equals(namespace, namespace1.namespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace);
    }
}

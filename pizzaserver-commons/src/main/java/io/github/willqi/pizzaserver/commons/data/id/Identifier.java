package io.github.willqi.pizzaserver.commons.data.id;

import io.github.willqi.pizzaserver.commons.utils.Util;

import java.util.Objects;
import java.util.regex.Pattern;

public final class Identifier {

    protected final String namespace;
    protected String id;

    public Identifier (String namespace, String id) {
        this(new Namespace(namespace), id);
    }

    // Use Namespace#id(...)
    protected Identifier (Namespace namespace, String id) {
        // Namespace IDs are checked thus can be trusted.
        this.namespace = namespace.getNamespaceString();

        if(id == null || id.trim().length() == 0 || !id.matches("([A-Za-z0-9_.-]*)")) {
            this.id = "gen_"+ Util.generateRandomIdentifier(6, 6); // What are the chances of this clashing

        } else {
            this.id = id.trim().toLowerCase();
        }

    }

    public static Identifier fromStringIdentifier(String string) {
        if(string.contains(":")) {
            String[] components = string.split(Pattern.quote(":"));
            return new Identifier(components[0], components[1]);

        } else {
            return new Identifier((String) null, string);
        }
    }

    /** @return the namespace of the identifier.*/
    public String getNamespace() { return namespace; }
    /** @return the id element of the identifier. */
    public String getObjectId() { return id; }

    /** @return the full string identifier.*/
    public String getID() { return namespace+":"+id; }


    /** @return the result of Identifier#getID() */
    @Override
    public String toString() {
        return getID();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Identifier that = (Identifier) o;
        return Objects.equals(namespace, that.namespace) && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(namespace, id);
    }
}

package io.github.pizzaserver.commons.data;

import io.github.pizzaserver.commons.utils.Check;

public class DataKey<T> {

    private final String key;
    private final Class<T> type;


    protected DataKey(String keyString, Class<T> valueType) {
        this.key = Check.notEmptyString(keyString, "Key Identifier").toLowerCase();
        this.type = Check.notNull(valueType, "Key Type");
    }

    public String getKey() {
        return key;
    }

    public Class<T> getType() {
        return type;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataKey<?> dataKey = (DataKey<?>) o;

        if (!key.equals(dataKey.key)) return false;
        return type.equals(dataKey.type);
    }

    @Override
    public int hashCode() {
        int result = key.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    public static <T> DataKey<T> of(String identifier, Class<T> value_type) {
        return new DataKey<>(identifier, value_type);
    }

}

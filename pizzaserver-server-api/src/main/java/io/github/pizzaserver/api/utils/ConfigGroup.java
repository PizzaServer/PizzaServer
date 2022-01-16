package io.github.pizzaserver.api.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ConfigGroup {

    protected Map<String, Object> properties = new LinkedHashMap<>();


    public ConfigGroup() { }

    public ConfigGroup(Map<String, Object> properties) {
        this.properties = properties;
    }

    public void setString(String key, String value) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), value);
    }

    public String getString(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return (String) group.getProperties().get(getPropertyFromKey(key));
    }

    public void setStringList(String key, List<String> list) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), list);
    }

    public List<String> getStringList(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return ((List<String>) group.getProperties().get(getPropertyFromKey(key))).stream()
                                                                                  .map(Object::toString)
                                                                                  .collect(Collectors.toList());
    }

    public void setBoolean(String key, boolean value) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), value);
    }

    public boolean getBoolean(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return (Boolean) group.getProperties().get(getPropertyFromKey(key));
    }

    public void setBooleanList(String key, List<Boolean> list) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), list);
    }

    public List<Boolean> getBooleanList(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return ((List<Boolean>) group.getProperties().get(getPropertyFromKey(key))).stream()
                                                                                   .map(obj -> Boolean.valueOf(obj.toString()))
                                                                                   .collect(Collectors.toList());
    }

    public void setInteger(String key, int value) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), value);
    }

    public int getInteger(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return (Integer) group.getProperties().get(getPropertyFromKey(key));
    }

    public void setIntegerList(String key, List<Integer> list) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), list);
    }

    public List<Integer> getIntegerList(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return ((List<Object>) group.getProperties().get(getPropertyFromKey(key))).stream()
                                                                                  .mapToInt(obj -> Integer.parseInt(obj.toString()))
                                                                                  .boxed()
                                                                                  .collect(Collectors.toList());
    }

    public void setDouble(String key, double value) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), value);
    }

    public double getDouble(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return Double.parseDouble(group.getProperties().get(getPropertyFromKey(key)).toString());
    }

    public void setDoubleList(String key, List<Double> list) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), list);
    }

    public List<Double> getDoubleList(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return ((List<Object>) group.getProperties().get(getPropertyFromKey(key))).stream()
                                                                                  .mapToDouble(obj -> Double.parseDouble(
                                                                                          obj.toString()))
                                                                                  .boxed()
                                                                                  .collect(Collectors.toList());
    }

    public void setLong(String key, long value) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), value);
    }

    public long getLong(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return (Long) group.getProperties().get(getPropertyFromKey(key));
    }

    public void setLongList(String key, List<Long> list) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), list);
    }

    public List<Long> getLongList(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return ((List<Object>) group.getProperties().get(getPropertyFromKey(key))).stream()
                                                                                  .mapToLong(obj -> Long.parseLong(obj.toString()))
                                                                                  .boxed()
                                                                                  .collect(Collectors.toList());
    }

    public void setGroup(String key, ConfigGroup newGroup) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.getProperties().put(getPropertyFromKey(key), newGroup);
    }

    public ConfigGroup getGroup(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return new ConfigGroup((Map<String, Object>) group.getProperties().get(getPropertyFromKey(key)));
    }

    public void remove(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        group.remove(getPropertyFromKey(key));
    }

    public boolean has(String key) {
        ConfigGroup group = this.getParentGroupFromKey(key);
        return group.getProperties().containsKey(getPropertyFromKey(key));
    }

    protected Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

    /**
     * Given a string such as root.subroot.property
     * This will return the ConfigGroup of root.subroot
     * @param key key to fetch the parent group of
     * @return parent config group of key provided
     */
    protected ConfigGroup getParentGroupFromKey(String key) {
        String[] parts = key.split("\\.");
        if (parts.length == 1) {
            return this;
        }

        Map<String, Object> contents = (Map<String, Object>) this.properties.get(parts[0]);
        for (int i = 1; i < parts.length - 1; i++) {    // the last part is the property
            contents = (Map<String, Object>) contents.get(parts[i]);
        }
        return new ConfigGroup(contents);
    }

    /**
     * Given a string such as root.subroot.property
     * This will return the ending part of the string (in this case, property)
     * @param key key to fetch the property part of
     * @return property part
     */
    protected static String getPropertyFromKey(String key) {
        return key.substring(key.lastIndexOf('.') + 1);
    }

    @Override
    public int hashCode() {
        return this.properties.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof ConfigGroup) {
            return ((ConfigGroup) obj).getProperties().equals(this.getProperties());
        } else {
            return false;
        }
    }
}

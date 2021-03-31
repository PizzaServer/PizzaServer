package io.github.willqi.pizzaserver.utils;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigGroup {

    private final Map<String, Object> properties = new LinkedHashMap<>();

    private final ConfigGroup parent;
    private final String name;

    public ConfigGroup() {
        this.parent = null;
        this.name = null;
    }

    public ConfigGroup(String name) {
        this.parent = null;
        this.name = name;
    }

    public ConfigGroup(String name, ConfigGroup parent) {
        this.parent = parent;
        this.name = name;
    }

    public ConfigGroup getParentGroup() {
        return this.parent;
    }

    public String getName() {
        return this.name;
    }

    public void setString(String name, String value) {
        this.properties.put(name, value);
    }

    public String getString(String name) {
        return String.valueOf(this.properties.get(name));
    }

    public void setStringList(String name, List<String> list) {
        this.properties.put(name, list);
    }

    public List<String> getStringList(String name) {
        return ((Stream<String>)((List)this.properties.get(name))
                .stream()
                .map(Object::toString))
                .collect(Collectors.toList());
    }

    public void setBoolean(String name, boolean value) {
        this.properties.put(name, value);
    }

    public boolean getBoolean(String name) {
        return Boolean.parseBoolean(this.properties.get(name).toString());
    }

    public void setBooleanList(String name, List<Boolean> list) {
        this.properties.put(name, list);
    }

    public List<Boolean> getBooleanList(String name) {
        return ((Stream<Boolean>)((List)this.properties.get(name))
                .stream()
                .map(obj -> Boolean.valueOf(obj.toString())))
                .collect(Collectors.toList());
    }

    public void setInteger(String name, int value) {
        this.properties.put(name, value);
    }

    public int getInteger(String name) {
        return Integer.parseInt(this.properties.get(name).toString());
    }

    public void setIntegerList(String name, List<Integer> list) {
        this.properties.put(name, list);
    }

    public List<Integer> getIntegerList(String name) {
        return ((List)this.properties.get(name))
                .stream()
                .mapToInt(obj -> Integer.parseInt(obj.toString()))
                .boxed()
                .collect(Collectors.toList());
    }

    public void setDouble(String name, double value) {
        this.properties.put(name, value);
    }

    public double getDouble(String name) {
        return Double.parseDouble(this.properties.get(name).toString());
    }

    public void setDoubleList(String name, List<Double> list) {
        this.properties.put(name, list);
    }

    public List<Double> getDoubleList(String name) {
        return ((List)this.properties.get(name))
                .stream()
                .mapToDouble(obj -> Double.parseDouble(obj.toString()))
                .boxed()
                .collect(Collectors.toList());
    }

    public void setLong(String name, long value) {
        this.properties.put(name, value);
    }
    public long getLong(String name) {
        return Long.parseLong(this.properties.get(name).toString());
    }

    public void setLongList(String name, List<Long> list) {
        this.properties.put(name, list);
    }

    public List<Long> getLongList(String name) {
        return ((List)this.properties.get(name))
                .stream()
                .mapToLong(obj -> Long.parseLong(obj.toString()))
                .boxed()
                .collect(Collectors.toList());
    }

    public void setFloat(String name, float value) {
        this.properties.put(name, value);
    }

    public float getFloat(String name) {
        return Float.parseFloat(this.properties.get(name).toString());
    }

    public void setFloatList(String name, List<Float> list) {
        this.properties.put(name, list);
    }

    public List<Float> getFloatList(String name) {
        return ((Stream<Float>)((List)this.properties.get(name))
                .stream()
                .map(obj -> Float.valueOf(obj.toString())))
                .collect(Collectors.toList());
    }

    public void setGroup(String name, ConfigGroup group) {
        this.properties.put(name, group);
    }

    public ConfigGroup getGroup(String name) {
        return (ConfigGroup)this.properties.get(name);
    }

    public void remove(String name) {
        this.properties.remove(name);
    }

    public boolean has(String name) {
        return this.properties.containsKey(name);
    }

    protected Map<String, Object> getProperties() {
        return Collections.unmodifiableMap(this.properties);
    }

}

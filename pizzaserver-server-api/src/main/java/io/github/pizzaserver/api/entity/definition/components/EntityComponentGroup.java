package io.github.pizzaserver.api.entity.definition.components;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class EntityComponentGroup {

    private final String groupId;
    private final Map<Class<? extends EntityComponent>, EntityComponent> components = new HashMap<>();


    public EntityComponentGroup(String groupId, Collection<EntityComponent> components) {
        this(groupId, components.toArray(new EntityComponent[0]));
    }

    public EntityComponentGroup(String groupId, EntityComponent[] components) {
        this.groupId = groupId;

        for (EntityComponent component : components) {
            if (this.components.containsKey(component.getClass())) {
                throw new IllegalArgumentException();
            }

            this.components.put(component.getClass(), component);
        }
    }

    public String getGroupId() {
        return this.groupId;
    }

    public boolean hasComponent(Class<? extends EntityComponent> componentClazz) {
        return this.components.containsKey(componentClazz);
    }

    public Collection<Class<? extends EntityComponent>> getComponentClasses() {
        return this.components.keySet();
    }

    public Collection<? extends EntityComponent> getComponents() {
        return this.components.values();
    }

    @SuppressWarnings("unchecked")
    public <T extends EntityComponent> T getComponent(Class<T> componentClazz) {
        if (!this.hasComponent(componentClazz)) {
            throw new NullPointerException("This component group does not have the provided component.");
        }
        return (T) this.components.get(componentClazz);
    }

}

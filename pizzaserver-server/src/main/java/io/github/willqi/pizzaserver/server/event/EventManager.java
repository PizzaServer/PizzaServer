package io.github.willqi.pizzaserver.server.event;

import io.github.willqi.pizzaserver.server.Server;
import io.github.willqi.pizzaserver.server.event.filter.EventFilter;
import io.github.willqi.pizzaserver.server.event.handler.EventHandler;
import io.github.willqi.pizzaserver.server.event.handler.EventHandlerReference;
import io.github.willqi.pizzaserver.server.event.type.CancellableType;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class EventManager {

    // Catalogues all listener events.
    protected static HashMap
            <
                Class<?>, // Listener's Class
                HashMap // Map tying event types to their event methods within the class.
                    <
                        Class<? extends BaseEvent>,
                        ArrayList<EventHandlerReference>
                    >
            > listenerReference = new HashMap<>();

    protected final Server server;

    protected final ArrayList<EventFilter> filters; // Filter for EVERY listener.
    protected final ArrayList<Object> listeners; // Once a listener is added, it has a permanent place in the listenerReference.
    protected final ArrayList<EventManager> children; // Send events to children too. Only sent if filter is passed.

    public EventManager(Server server, EventFilter... filters) {
        this.server = server;
        this.filters = new ArrayList<>();
        this.listeners = new ArrayList<>();
        this.children = new ArrayList<>();

        this.filters.addAll(Arrays.asList(filters));
    }



    public synchronized void call(BaseEvent event) {
        ArrayList<EventHandlerReference> callList = new ArrayList<>();
        ArrayList<Object> callListeners = new ArrayList<>(); // Ordered to match call list

        // And this is the part where it's probably the least efficient.
        // Would be great to bake this but then I can't really use the FilteredListener.
        // Could maybe filter each method as I go?
        for(Object listener: listeners) {
            HashMap<Class<? extends BaseEvent>, ArrayList<EventHandlerReference>> ref = listenerReference.get(listener.getClass());

            for(EventHandlerReference pair : ref.getOrDefault(event.getClass(), new ArrayList<>())) {

                // Handles generics in class parameters.
                // These would be a nightmare to integrate into the listener's map.
                TypeVariable<? extends Class<?>>[] source = event.getClass().getTypeParameters();
                TypeVariable<? extends Class<?>>[] target = pair.getMethod().getParameterTypes()[0].getTypeParameters();
                if(!(source.length == target.length)) continue; // Not even the right length!

                boolean fail = false;
                for(int i = 0; i < source.length; i++) {
                    TypeVariable<? extends Class<?>> sourceType = source[i];
                    TypeVariable<? extends Class<?>> targetType = target[i];

                    if(!sourceType.getGenericDeclaration().isAssignableFrom(targetType.getGenericDeclaration())){
                        fail = true;
                        break;
                    }
                }

                if(fail) continue;

                // ^ should just be skipped if generics aren't found ^


                boolean added = false;
                int pairPriority = pair.getAnnotation().priority().getValue();
                int originalSize = callList.size();

                for(int i = 0; i < originalSize; i++) {
                    EventHandlerReference p = callList.get(i);

                    if(pairPriority > p.getAnnotation().priority().getValue()) {
                        callList.add(i, pair);
                        callListeners.add(i, listener);
                        added = true;
                        break;
                    }
                }

                if(!added){
                    callList.add(pair);
                    callListeners.add(listener);
                }
            }
        }

        // Separating them to save a tiny bit more time on each iteration.
        if(event instanceof CancellableType) {
            CancellableType cancellable = (CancellableType) event;

            for (int i = 0; i < callList.size(); i++) {
                EventHandlerReference methodPair = callList.get(i);
                Object sourceListener = callListeners.get(i);

                // Skip if cancelled and ignoring cancelled.
                if(cancellable.isCancelled() && methodPair.getAnnotation().ignoreIfCancelled()) continue;
                invokeEvent(sourceListener, event, methodPair);
            }

        } else {

            for (int i = 0; i < callList.size(); i++) {
                EventHandlerReference methodPair = callList.get(i);
                Object sourceListener = callListeners.get(i);

                invokeEvent(sourceListener, event, methodPair);
            }
        }

    }


    /**
     * Creates a child EventManager which passes all the events its
     * parent EventManager receives, applying its own filters.
     *
     * Events called on the child EventManager are only called on itself
     * and its own children.
     */
    public EventManager createChild(EventFilter... filters) {
        EventManager child = new EventManager(this.server, filters);

        synchronized (children) {
            this.children.add(child);
        }

        return child;
    }

    /**
     * Removes an existing child EventManager from this.
     * It does not deactivate the EventManager, rather preventing events
     * from the parent reaching the specified child.
     */
    public synchronized void removeChild(EventManager child) {
        this.children.remove(child);
    }


    /**
     * Registers a listener to this EventManager
     * @param listener the listener to be registered.
     * @return listener for storing an instance.
     */
    public synchronized Object addListener(Object listener) {
        removeListener(listener, true);
        listeners.add(listener);

        // Generate reference for the listener's type if one doesn't already exist
        if(!listenerReference.containsKey(listener.getClass())) {
            HashMap<Class<? extends BaseEvent>, ArrayList<EventHandlerReference>> listenerMethods = new HashMap<>();

            // Get event listening methods.
            for(Method method : listener.getClass().getMethods()) {

                if(method.isAnnotationPresent(EventHandler.class)) {
                    EventHandler annotation = method.getAnnotation(EventHandler.class);
                    Parameter[] parameters = method.getParameters();

                    if(parameters.length == 1){
                        Class<?> type = parameters[0].getType();
                        ArrayList<Class<? extends BaseEvent>> eventClasses = new ArrayList<>();

                        EventHandlerReference pair = new EventHandlerReference(annotation, method);
                        adoptSuperclasses(type, eventClasses); // Get all the categories this method would be in.

                        for(Class<? extends BaseEvent> cls: eventClasses) {

                            if(!listenerMethods.containsKey(cls)){
                                listenerMethods.put(cls, new ArrayList<>()); // Create new handler list if it doesn't exist.
                            }
                            listenerMethods.get(cls).add(pair);
                        }
                    }
                }
            }

            listenerReference.put(listener.getClass(), listenerMethods);
        }

        return listener;
    }

    /**
     * Removes listener from this EventManager and any child EventManager's
     * @param listener the listener to be removed.
     */
    public synchronized void removeListener(Object listener) {
        removeListener(listener, true);
    }

    /**
     * Removes a listener from the managers listener list.
     * @param listener the listener to be removed.
     * @param removeFromChildren should instances of this listener be removed in child EventManager's ?
     */
    public synchronized void removeListener(Object listener, boolean removeFromChildren) {
        listeners.remove(listener);

        if(removeFromChildren) {

            for (EventManager child : children) {
                child.removeListener(listener, true); // Ensure children don't include it either.
            }
        }
    }


    private void invokeEvent(Object owningListener, BaseEvent event, EventHandlerReference methodPair) {
        try {
            methodPair.getMethod().invoke(owningListener, event);

        } catch (Exception err) {
            this.server.getLogger().error("An error was thrown during the invocation of an event:");
            err.printStackTrace();
        }
    }

    /**
     * Checks to see if a class extends an event, adding it to a list of
     * identified classes. It crawls through all the superclasses and interfaces
     * of a class with recursion.
     * @param classIn the class to be checked.
     * @param list a list of all the previously checked classes.
     */
    @SuppressWarnings("unchecked") // It's checked with Class#isAssaignableFrom() :)
    private static void adoptSuperclasses(Class<?> classIn, ArrayList<Class<? extends BaseEvent>> list) {
        if(classIn == null) return;

        if(BaseEvent.class.isAssignableFrom(classIn)){
            list.add((Class<? extends BaseEvent>) classIn);
            adoptSuperclasses(classIn.getSuperclass(), list);

            for(Class<?> cls: classIn.getInterfaces()) {
                adoptSuperclasses(cls, list);
            }
        }
    }

    public synchronized EventFilter[] getFilters() { return filters.toArray(new EventFilter[0]); }
    public synchronized Object[] getListeners() { return listeners.toArray(); }
    public synchronized EventManager[] getChildren() { return children.toArray(new EventManager[0]); }

}

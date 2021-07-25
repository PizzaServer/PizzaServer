package io.github.willqi.pizzaserver.server.event.handler;

import java.lang.reflect.Method;

public final class EventHandlerReference {

    private EventHandler annotation;
    private Method method;

    public EventHandlerReference(EventHandler annotation, Method method) {
        this.annotation = annotation;
        this.method = method;
    }

    public EventHandler getAnnotation() { return annotation; }
    public Method getMethod() { return method; }

}

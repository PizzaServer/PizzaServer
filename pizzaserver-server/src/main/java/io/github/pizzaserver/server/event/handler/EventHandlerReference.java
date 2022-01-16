package io.github.pizzaserver.server.event.handler;

import io.github.pizzaserver.api.event.handler.EventHandler;

import java.lang.reflect.Method;

public final class EventHandlerReference {

    private EventHandler annotation;
    private Method method;

    public EventHandlerReference(EventHandler annotation, Method method) {
        this.annotation = annotation;
        this.method = method;
    }

    public EventHandler getAnnotation() {
        return this.annotation;
    }

    public Method getMethod() {
        return this.method;
    }
}

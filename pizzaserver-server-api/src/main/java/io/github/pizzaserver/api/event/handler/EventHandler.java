package io.github.pizzaserver.api.event.handler;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.METHOD)
public @interface EventHandler {

    Priority priority() default Priority.NORMAL;
    boolean ignoreIfCancelled() default true;
}

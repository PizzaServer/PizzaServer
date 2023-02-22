package io.github.pizzaserver.commons.data;

import io.github.pizzaserver.commons.data.key.DataKey;
import io.github.pizzaserver.commons.data.react.ActionType;

public class DataAction {

    /** Returns the previous value of the container. */
    public static final ActionType<Object> VALUE_PRE_SET = ActionType.of("value_set_pre", Object.class);

    /** Returns the value of the container once it has been set. */
    public static final ActionType<Object> VALUE_SET = ActionType.of("value_set_post", Object.class);


    /** Returns the key of a created container. */
    @SuppressWarnings("rawtypes")
    public static final ActionType<DataKey> CONTAINER_CREATE = ActionType.of("container_create", DataKey.class);

    /** Returns nothing - triggered when a container is marked stale */
    public static final ActionType<Void> CONTAINER_STALE = ActionType.of("container_set_stale", Void.TYPE);

}

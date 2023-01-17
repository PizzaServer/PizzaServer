package io.github.pizzaserver.commons.data;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

import static org.junit.jupiter.api.Assertions.fail;

public class ValueContainerTest {

    @Test
    public void testValueSetReaction() {
        int starting_value = 69;
        int ending_value = 420;

        boolean[] actionMissed = new boolean[] { true, true };

        ValueContainer<Integer> valContainer = ValueContainer.wrap(starting_value);

        valContainer.listenFor(ValueContainer.ACTION_VALUE_PRE_SET, val -> {
            if(val instanceof Integer value) {
                if(value != starting_value)
                    fail(String.format("Value should be equal to %s - was instead %s", starting_value, value));

            } else fail(String.format("Payload for ACTION_VALUE_PRE_SET was %s when it should be an Integer.", val.getClass()));

            actionMissed[0] = false;
        });

        valContainer.listenFor(ValueContainer.ACTION_VALUE_SET, val -> {
            if(val instanceof Integer value) {
                if(value != ending_value)
                    fail(String.format("Value should be equal to %s - was instead %s", ending_value, value));

            } else fail(String.format("Payload for ACTION_VALUE_SET was %s when it should be an Integer.", val.getClass()));

            actionMissed[1] = false;
        });

        valContainer.setValue(ending_value);

        if(actionMissed[0]) fail("ACTION_VALUE_PRE_SET did not get fired.");
        if(actionMissed[1]) fail("ACTION_VALUE_SET did not get fired.");
    }

    @Test
    public void testStaleContainer() {
        ValueContainer<Integer> valContainer = ValueContainer.wrap(2023);

        Consumer<Object> failCondition = val ->
                fail("Action (which wasn't ACTION_SET_STALE) was fired despite the container being stale");

        AtomicBoolean wasStaleSetEventFired = new AtomicBoolean(false);

        valContainer.listenFor(ValueContainer.ACTION_VALUE_PRE_SET, failCondition);
        valContainer.listenFor(ValueContainer.ACTION_VALUE_SET, failCondition);
        valContainer.listenFor(ValueContainer.ACTION_SET_STALE, ignored -> wasStaleSetEventFired.set(true));

        valContainer.stale();
        valContainer.setValue(2077);

        if(!wasStaleSetEventFired.get())
            fail("ACTION_SET_STALE was not fired when the container was marked as stale");
    }

}

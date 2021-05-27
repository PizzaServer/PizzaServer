package io.github.willqi.pizzaserver.nbt.exceptions;

/**
 * Thrown when an NBT exception occurs where a tag cannot be modified due to NBT limit restrictions.
 */
public class NBTLimitException extends RuntimeException {

    public NBTLimitException(String message) {
        super(message);
    }

}

package io.github.willqi.pizzaserver.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

public class Zlib {

    // How big should our temporary zlib array allocate space for?
    private static final int MAX_CHUNK_SIZE = 1048576;    // 1MB

    public static ByteBuf inflateBuffer(ByteBuf buffer) throws DataFormatException {
        byte[] compressed = new byte[buffer.readableBytes()];
        for (int i = 0; i < buffer.readableBytes(); i++) {
            compressed[i] = buffer.readByte();
        }

        Inflater inflater = new Inflater(true);
        inflater.setInput(compressed);

        ByteBuf result = ByteBufAllocator.DEFAULT.buffer();
        byte[] inflated = new byte[MAX_CHUNK_SIZE];
        try {
            while (inflater.getRemaining() > 0) {
                // We don't want to surpass the int limit for arrays
                int length = Math.min(inflater.getRemaining(), MAX_CHUNK_SIZE);
                inflater.inflate(inflated, 0, length);
                result.writeBytes(inflated);
            }
        } catch (DataFormatException exception) {
            if (result.readableBytes() > 0) {
                result.release();
            }
            throw exception;
        }
        return result;
    }

}

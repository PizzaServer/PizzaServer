package io.github.willqi.pizzaserver.server.network.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zlib {

    // How big should our temporary zlib array allocate space for?
    private static final int MAX_CHUNK_SIZE = 1024;

    public static ByteBuf compressBuffer(ByteBuf buffer, int level) {
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        Deflater compressor = new Deflater(level, true);
        compressor.setInput(data);
        compressor.finish();

        byte[] result = new byte[MAX_CHUNK_SIZE];
        ByteBuf resultBuffer = ByteBufAllocator.DEFAULT.ioBuffer(MAX_CHUNK_SIZE);
        while (!compressor.finished()) {
            int writtenBytes = compressor.deflate(result);
            resultBuffer.writeBytes(result, 0, writtenBytes);
        }
        compressor.end();
        return resultBuffer;
    }

    public static ByteBuf decompressBuffer(ByteBuf buffer) throws DataFormatException {
        byte[] compressed = new byte[buffer.readableBytes()];
        buffer.readBytes(compressed);

        Inflater inflater = new Inflater(true);
        inflater.setInput(compressed);

        ByteBuf result = ByteBufAllocator.DEFAULT.buffer();
        try {
            while (!inflater.finished()) {
                byte[] decompressed = new byte[MAX_CHUNK_SIZE];
                int uncompressedBytes = inflater.inflate(decompressed);
                result.writeBytes(decompressed, 0, uncompressedBytes);
            }
        } catch (DataFormatException exception) {
            result.release();
            throw exception;
        }
        inflater.end();
        return result;
    }

}

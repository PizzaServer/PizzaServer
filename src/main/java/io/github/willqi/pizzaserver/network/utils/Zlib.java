package io.github.willqi.pizzaserver.network.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Zlib {

    // How big should our temporary zlib array allocate space for?
    private static final int MAX_CHUNK_SIZE = 1048576 * 2;  // 2 MB

    public static ByteBuf compressBuffer(ByteBuf buffer) {
        byte[] data = new byte[buffer.readableBytes()];
        buffer.readBytes(data);
        Deflater compressor = new Deflater(0, true);
        compressor.setInput(data);
        compressor.finish();

        ByteBuf result = ByteBufAllocator.DEFAULT.buffer();
        while (!compressor.needsInput()) {
            byte[] compressed = new byte[MAX_CHUNK_SIZE];
            int compressedBytesWritten = compressor.deflate(compressed);

            byte[] compressedSlimmed = new byte[compressedBytesWritten];
            System.arraycopy(compressed, 0, compressedSlimmed, 0, compressedBytesWritten);
            result.writeBytes(compressedSlimmed);
        }
        compressor.end();
        return result;
    }

    public static ByteBuf decompressBuffer(ByteBuf buffer) throws DataFormatException {
        byte[] compressed = new byte[buffer.readableBytes()];
        buffer.readBytes(compressed);

        Inflater inflater = new Inflater(true);
        inflater.setInput(compressed);

        ByteBuf result = ByteBufAllocator.DEFAULT.buffer();
        try {
            while (inflater.getRemaining() > 0) {
                byte[] decompressed = new byte[MAX_CHUNK_SIZE];
                int uncompressedBytes = inflater.inflate(decompressed);
                inflater.finished();

                byte[] decompressedSlimmed = new byte[uncompressedBytes];
                System.arraycopy(decompressed, 0, decompressedSlimmed, 0, uncompressedBytes);

                result.writeBytes(decompressedSlimmed);
            }
        } catch (DataFormatException exception) {
            if (result.readableBytes() > 0) {
                result.release();
            }
            throw exception;
        }
        inflater.end();
        return result;
    }

}

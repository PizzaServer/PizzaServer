package io.github.willqi.pizzaserver.server.network.utils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

import java.util.zip.DataFormatException;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;

public class ZlibTests {

    @Test
    public void shouldInflateAndDeflateProperly() {

        //byte[] inputBytes = new byte[]{ 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05, 0x00, 0x01, 0x02, 0x03, 0x04, 0x05 };
        byte[] inputBytes = new byte[]{0x1, 0x5, 0x52};
        ByteBuf inputBuffer = ByteBufAllocator.DEFAULT.buffer();
        inputBuffer.writeBytes(inputBytes);

        ByteBuf compressed = Zlib.compressBuffer(inputBuffer);
        ByteBuf decompressed;
        try {
            decompressed = Zlib.decompressBuffer(compressed);
        } catch (DataFormatException exception) {
            throw new RuntimeException(exception);
        }
        compressed.release();

        byte[] resultingBytes = new byte[decompressed.readableBytes()];
        decompressed.readBytes(resultingBytes);
        decompressed.release();

        assertArrayEquals(inputBytes, resultingBytes);
    }

}

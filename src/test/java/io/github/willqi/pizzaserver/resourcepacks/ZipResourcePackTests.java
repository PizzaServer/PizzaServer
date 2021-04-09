package io.github.willqi.pizzaserver.resourcepacks;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

public class ZipResourcePackTests {

    @Test
    public void shouldRetrieveManifestInformation(@TempDir Path tempDirPath) {
        ResourcePack pack = getResourcePack(tempDirPath);
        assertEquals(pack.getUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        assertEquals(pack.getVersion(), "1.0.0");
    }

    @Test
    public void shouldBeEqualToTheResourcePackFile(@TempDir Path tempDirPath) {
        File resourcePackFile = getResourcePackFile(tempDirPath);
        ResourcePack pack = getResourcePack(tempDirPath);

        InputStream testFileStream = null;
        try {

            testFileStream = new FileInputStream(resourcePackFile);
            for (int chunkIndex = 0; chunkIndex < pack.getChunkCount(); chunkIndex++) {
                byte[] chunk = pack.getChunk(chunkIndex);
                for (byte chunkByte : chunk) {
                    int testFileByte = (byte)testFileStream.read();
                    assertEquals(testFileByte, chunkByte);
                    if (testFileByte == -1) {
                        return;
                    }
                }
            }

        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (testFileStream != null) {
                try {
                    testFileStream.close();
                } catch (IOException exception) {}
            }
        }

    }

    private static ResourcePack getResourcePack(Path tempDirPath) {
        ResourcePack pack;
        try {
            pack = new ZipResourcePack(getResourcePackFile(tempDirPath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return pack;
    }

    private static InputStream getTestResourcePackStream() {
        return ZipResourcePackTests.class.getResourceAsStream("/resourcepack.zip");
    }

    private static File getResourcePackFile(Path tempDirPath) {
        OutputStream stream = null;
        File tempResourcePackFile;
        try {
            tempResourcePackFile = tempDirPath.resolve("pack.zip").toFile();
            if (!tempResourcePackFile.exists()) {
                stream = new FileOutputStream(tempResourcePackFile);
                InputStream inputStream = getTestResourcePackStream();
                IOUtils.write(IOUtils.toByteArray(inputStream), stream);
                inputStream.close();
            }
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException exception) {}
            }
        }
        return tempResourcePackFile;
    }

}

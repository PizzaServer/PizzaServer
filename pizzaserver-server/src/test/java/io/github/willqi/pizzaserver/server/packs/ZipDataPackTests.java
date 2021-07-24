package io.github.willqi.pizzaserver.server.packs;

import static org.junit.jupiter.api.Assertions.assertEquals;

import io.github.willqi.pizzaserver.api.packs.DataPack;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.*;
import java.nio.file.Path;
import java.util.UUID;

public class ZipDataPackTests {

    @Test
    public void shouldRetrieveManifestInformation(@TempDir Path tempDirPath) {
        DataPack pack = getResourcePack(tempDirPath);
        Assertions.assertEquals(pack.getUuid(), UUID.fromString("00000000-0000-0000-0000-000000000000"));
        Assertions.assertEquals(pack.getVersion(), "1.0.0");
    }

    @Test
    public void shouldBeEqualToTheResourcePackFile(@TempDir Path tempDirPath) {
        File resourcePackFile = getResourcePackFile(tempDirPath);
        DataPack pack = getResourcePack(tempDirPath);

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

    private static DataPack getResourcePack(Path tempDirPath) {
        DataPack pack;
        try {
            pack = new ZipDataPack(getResourcePackFile(tempDirPath));
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        return pack;
    }

    private static InputStream getTestResourcePackStream() {
        return ZipDataPackTests.class.getResourceAsStream("/resourcepack.zip");
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

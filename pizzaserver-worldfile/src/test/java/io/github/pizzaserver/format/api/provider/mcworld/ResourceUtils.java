package io.github.pizzaserver.format.api.provider.mcworld;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ResourceUtils {

    public static void copyTestWorld(Path targetDirectoryLocation) throws IOException {

        // Setup
        File targetDirectory = targetDirectoryLocation.toFile();
        File tempZipFile = targetDirectory.toPath().resolve("testworld.zip").toFile();
        Files.copy(ResourceUtils.class.getResourceAsStream("/testworld.zip"), tempZipFile.toPath());

        // Extract contents
        try (ZipFile zipFile = new ZipFile(tempZipFile)) {
            try (ZipInputStream zipStream = new ZipInputStream(new FileInputStream(tempZipFile))) {

                ZipEntry entry = zipStream.getNextEntry();
                while (entry != null) {
                    if (entry.isDirectory()) {
                        targetDirectory.toPath().resolve(entry.getName()).toFile().mkdirs();
                    } else {
                        targetDirectory.toPath().resolve(entry.getName()).toFile().getParentFile().mkdirs();
                        try (InputStream inputStream = zipFile.getInputStream(entry)) {
                            Files.copy(inputStream, targetDirectory.toPath().resolve(entry.getName()));
                        }
                    }

                    entry = zipStream.getNextEntry();
                }

            }
        }
    }

    public static void deleteDirectoryContents(Path targetDirectoryLocation) throws IOException {
        Files.walk(targetDirectoryLocation)
            .forEach(file -> file.toFile().delete());
    }

}

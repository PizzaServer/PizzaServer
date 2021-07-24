package io.github.willqi.pizzaserver.server.packs;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import io.github.willqi.pizzaserver.api.packs.DataPack;
import org.apache.commons.io.IOUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class ZipDataPack implements DataPack {

    private final byte[] hash;
    private UUID uuid;
    private String version;
    private long dataLength;
    private byte[][] chunks;

    public ZipDataPack(File file) throws IOException {
        this.parseManifestFile(file);
        this.parsePackData(file);
        try {
            InputStream fileInputStream = new FileInputStream(file);
            this.hash = MessageDigest.getInstance("SHA-256").digest(IOUtils.toByteArray(fileInputStream));
            fileInputStream.close();
        } catch (NoSuchAlgorithmException exception) {
            throw new AssertionError(exception);
        }
    }

    @Override
    public int getMaxChunkLength() {
        return 1048576; // 1 MB
    }

    @Override
    public UUID getUuid() {
        return this.uuid;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public long getDataLength() {
        return this.dataLength;
    }

    @Override
    public int getChunkCount() {
        return this.chunks.length;
    }

    @Override
    public byte[] getHash() {
        return this.hash;
    }

    @Override
    public byte[] getChunk(int index) {
        return this.chunks[index];
    }

    /**
     * Cache uuid and version of resource pack
     * @param file the resource pack zipped file
     * @throws IOException
     */
    private void parseManifestFile(File file) throws IOException {
        ZipFile zipFile = new ZipFile(file);
        ZipEntry manifestZipEntry = zipFile.getEntry("manifest.json");
        InputStream manifestFile = zipFile.getInputStream(manifestZipEntry);
        String content = IOUtils.toString(manifestFile, "utf8");
        zipFile.close();

        JsonObject manifest = new Gson().fromJson(content, JsonObject.class);
        this.uuid = UUID.fromString(
                manifest.get("header")
                        .getAsJsonObject()
                        .get("uuid")
                        .getAsString()
        );

        JsonArray versionList = manifest.get("header")
                .getAsJsonObject()
                .get("version")
                .getAsJsonArray();

        this.version = String.join(".", versionList.get(0).getAsString(), versionList.get(1).getAsString(), versionList.get(2).getAsString());
    }

    /**
     * Cache resource pack chunks to be sent to client
     * @param file the resource pack zipped file
     * @throws IOException
     */
    private void parsePackData(File file) throws IOException {
        this.dataLength = file.length();

        int chunksLength = (int)Math.ceil((float)this.dataLength / this.getMaxChunkLength());
        this.chunks = new byte[chunksLength][];

        InputStream stream = new FileInputStream(file);
        try {
            for (int i = 0; i < chunksLength - 1; i++) {
                byte[] data = this.parseDataChunk(stream, new byte[this.getMaxChunkLength()]);
                this.chunks[i] = data;
            }

            // The last data chunk doesn't use as much space
            byte[] data = this.parseDataChunk(stream, new byte[(int)(this.dataLength - (chunksLength - 1) * this.getMaxChunkLength())]);
            this.chunks[chunksLength - 1] = data;

        } finally {
            stream.close();
        }
    }

    private byte[] parseDataChunk(InputStream stream, byte[] data) throws IOException {
        for (int byteIndex = 0; byteIndex < this.getMaxChunkLength(); byteIndex++) {
            int currentByte = stream.read();
            if (currentByte == -1) {
                break;
            } else {
                data[byteIndex] = (byte)currentByte;
            }
        }
        return data;
    }

}

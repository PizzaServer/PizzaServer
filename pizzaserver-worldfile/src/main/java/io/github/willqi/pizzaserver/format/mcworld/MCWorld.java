package io.github.willqi.pizzaserver.format.mcworld;

import io.github.willqi.pizzaserver.format.mcworld.world.chunks.MCChunkDatabase;
import io.github.willqi.pizzaserver.format.mcworld.world.info.MCWorldInfo;
import net.daporkchop.ldbjni.LevelDB;
import org.iq80.leveldb.Options;

import java.io.*;

/**
 * Represents a Bedrock world file
 */
public class MCWorld {

    private final static String DB_PATH = "db";
    private final static String LEVEL_DAT_PATH = "level.dat";

    private final File mcWorldDirectory;


    /**
     * Read the contents in a exported Bedrock world file
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorld(File mcWorldDirectory) {
        this.mcWorldDirectory = mcWorldDirectory;
    }

    public MCChunkDatabase openChunkDatabase() throws IOException {
        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH);
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }
        return new MCChunkDatabase(
                LevelDB.PROVIDER.open(dbDirectory, new Options().blockSize(64 * 1024))
        );

    }

    public void setWorldInfo(MCWorldInfo worldInfo) {
        // TODO: write info to disk
    }

    /**
     * Parse the level.dat file and retrieve the world info
     * @throws IOException if file cannot be read
     */
    public MCWorldInfo getWorldInfo() throws IOException {

        File levelDatFile = new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH);
        if (!levelDatFile.exists()) {
            throw new FileNotFoundException("Could not find level.dat file");
        }
        return new MCWorldInfo(
                levelDatFile
        );

    }

}

package io.github.pizzaserver.format.mcworld;

import io.github.pizzaserver.format.mcworld.world.chunks.MCWorldChunkProvider;
import io.github.pizzaserver.format.mcworld.world.info.MCWorldInfo;
import net.daporkchop.ldbjni.LevelDB;
import org.iq80.leveldb.Options;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents a Bedrock world file.
 */
public class MCWorld {

    private static final String DB_PATH = "db";
    private static final String LEVEL_DAT_PATH = "level.dat";

    private final File mcWorldDirectory;


    /**
     * Read the contents in an exported Bedrock world file.
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorld(File mcWorldDirectory) {
        this.mcWorldDirectory = mcWorldDirectory;
    }

    public MCWorldChunkProvider openChunkProvider() throws IOException {
        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH);
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }

        return new MCWorldChunkProvider(LevelDB.PROVIDER.open(dbDirectory, new Options()));
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
        return new MCWorldInfo(levelDatFile);
    }

}
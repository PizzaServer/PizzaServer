package io.github.pizzaserver.format.mcworld;

import io.github.pizzaserver.format.api.BedrockLevel;
import io.github.pizzaserver.format.api.LevelData;
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
public class MCWorldLevel implements BedrockLevel<MCWorldChunkProvider> {

    protected static final String DB_PATH = "db";
    protected static final String LEVEL_DAT_PATH = "level.dat";

    protected final File mcWorldDirectory;
    protected final MCWorldChunkProvider chunkProvider;
    protected LevelData levelData;


    /**
     * Read the contents in an exported Bedrock world file.
     *
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorldLevel(File mcWorldDirectory) throws IOException {
        this.mcWorldDirectory = mcWorldDirectory;

        File levelDatFile = new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH);
        if (!levelDatFile.exists()) {
            throw new FileNotFoundException("Could not find level.dat file");
        }
        this.levelData = new MCWorldInfo(levelDatFile);

        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH);
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }
        this.chunkProvider = new MCWorldChunkProvider(LevelDB.PROVIDER.open(dbDirectory,
                                                                            new Options().createIfMissing(true)));
    }

    @Override
    public MCWorldChunkProvider getChunkProvider() {
        return this.chunkProvider;
    }

    @Override
    public void setLevelData(LevelData worldInfo) throws IOException {
        // TODO: write info to disk
    }

    @Override
    public File getFile() {
        return this.mcWorldDirectory;
    }

    /**
     * Parse the level.dat file and retrieve the world info
     *
     * @throws IOException if file cannot be read
     */
    @Override
    public LevelData getLevelData() {
        return this.levelData;
    }

    @Override
    public void close() throws IOException {
        this.chunkProvider.close();
    }
}

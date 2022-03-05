package io.github.pizzaserver.format.provider.mcworld;

import io.github.pizzaserver.format.dimension.BedrockDimension;
import io.github.pizzaserver.format.BedrockLevel;
import io.github.pizzaserver.format.LevelData;
import io.github.pizzaserver.format.provider.mcworld.info.MCWorldLevelData;
import net.daporkchop.ldbjni.LevelDB;
import org.iq80.leveldb.Options;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents a Bedrock world file.
 */
public class MCWorldLevel implements BedrockLevel {

    protected static final String DB_PATH = "db";
    protected static final String LEVEL_DAT_PATH = "level.dat";

    protected final File mcWorldDirectory;
    protected final MCWorldChunkProvider chunkProvider;
    protected LevelData levelData;


    /**
     * Read the contents in an exported Bedrock world file.
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorldLevel(File mcWorldDirectory) throws IOException {
        this.mcWorldDirectory = mcWorldDirectory;

        File levelDatFile = new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH);
        if (!levelDatFile.exists()) {
            throw new FileNotFoundException("Could not find level.dat file");
        }
        this.levelData = new MCWorldLevelData(levelDatFile);

        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH);
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }

        this.chunkProvider = new MCWorldChunkProvider(LevelDB.PROVIDER.open(dbDirectory, new Options().createIfMissing(true)));
    }

    @Override
    public BedrockDimension getDimension(int dimensionId) {
        return new BedrockDimension(this.chunkProvider, dimensionId);
    }

    @Override
    public void setLevelData(LevelData data) throws IOException {
        // TODO: write info to disk
    }

    @Override
    public LevelData getLevelData() {
        return this.levelData;
    }

    @Override
    public File getFile() {
        return this.mcWorldDirectory;
    }

    @Override
    public void close() throws IOException {
        this.chunkProvider.close();
    }

}

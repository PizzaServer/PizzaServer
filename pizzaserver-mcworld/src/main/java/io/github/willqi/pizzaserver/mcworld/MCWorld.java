package io.github.willqi.pizzaserver.mcworld;

import io.github.willqi.pizzaserver.mcworld.world.info.WorldInfo;
import io.github.willqi.pizzaserver.nbt.streams.le.LittleEndianDataInputStream;
import io.github.willqi.pizzaserver.nbt.streams.nbt.NBTInputStream;
import io.github.willqi.pizzaserver.nbt.tags.NBTCompound;
import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.*;

/**
 * Represents a Bedrock world file
 */
public class MCWorld implements Closeable {

    private final static String DB_PATH = "db";
    private final static String LEVEL_DAT_PATH = "level.dat";

    private final File mcWorldDirectory;
    private final DB database;

    private WorldInfo worldInfo;


    /**
     * Read the contents in a exported Bedrock world file
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorld(File mcWorldDirectory) throws IOException {

        if (!( mcWorldDirectory.exists() && mcWorldDirectory.isDirectory() )) {
            throw new FileNotFoundException("The directory provided could not be found.");
        }
        this.mcWorldDirectory = mcWorldDirectory;
        this.ensureDirectoryIntegrity();

        this.database = Iq80DBFactory.factory.open(
                new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH),
                new Options());

        this.parseWorldInfoFile();

    }

    /**
     * Retrieve the content in the level.dat file
     * @return
     */
    public WorldInfo getWorldInfo() {
        return this.worldInfo.clone();
    }

    public void setWorldInfo(WorldInfo worldInfo) {
        this.worldInfo = worldInfo;
        // TODO: write info to disk
    }

    /**
W      * Ensures all files required exist in the directory.
     * @throws FileNotFoundException if a file is missing
     */
    private void ensureDirectoryIntegrity() throws FileNotFoundException {
        File levelDatFile = new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH);
        if (!levelDatFile.exists()) {
            throw new FileNotFoundException("Could not find level.dat file");
        }

        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), DB_PATH);
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }
    }

    /**
     * Parse the level.dat file and retrieve the world info
     * @throws IOException if file cannot be read
     */
    private void parseWorldInfoFile() throws IOException {
        try (NBTInputStream inputStream = new NBTInputStream(
                new LittleEndianDataInputStream(
                        new FileInputStream(new File(this.mcWorldDirectory.getAbsolutePath(), LEVEL_DAT_PATH))
                )
        )) {
            // the header is 8 bytes.
            inputStream.skip(8);
            NBTCompound compound = inputStream.readCompound();
            // TODO: do something with NBT
        }
    }


    @Override
    public void close() throws IOException {
        this.database.close();
    }

}

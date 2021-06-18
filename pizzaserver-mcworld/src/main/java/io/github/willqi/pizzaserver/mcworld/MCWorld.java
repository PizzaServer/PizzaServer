package io.github.willqi.pizzaserver.mcworld;

import org.iq80.leveldb.DB;
import org.iq80.leveldb.Options;
import org.iq80.leveldb.impl.Iq80DBFactory;

import java.io.Closeable;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Represents a Bedrock world file
 */
public class MCWorld implements Closeable {

    private final File mcWorldDirectory;
    private final DB database;


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
                new File(this.mcWorldDirectory.getAbsolutePath(), "db"),
                new Options());

    }

    /**
     * Ensures all files required exist in the directory.
     * @throws FileNotFoundException if a file is missing
     */
    private void ensureDirectoryIntegrity() throws FileNotFoundException {
        File levelDatFile = new File(this.mcWorldDirectory.getAbsolutePath(), "level.dat");
        if (!levelDatFile.exists()) {
            throw new FileNotFoundException("Could not find level.dat file");
        }

        File dbDirectory = new File(this.mcWorldDirectory.getAbsolutePath(), "db");
        if (!(dbDirectory.exists() && dbDirectory.isDirectory())) {
            throw new FileNotFoundException("Could not find db directory");
        }
    }


    @Override
    public void close() throws IOException {
        this.database.close();
    }

}

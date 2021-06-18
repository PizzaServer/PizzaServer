package io.github.willqi.pizzaserver.mcworld;

import java.io.File;

/**
 * Represents a Bedrock world file
 */
public class MCWorld {

    private final File mcWorldDirectory;


    /**
     * Read the contents in a exported Bedrock world file
     * @param mcWorldDirectory Folder of the unzipped contents in the .mcworld file
     */
    public MCWorld(File mcWorldDirectory) {
        this.mcWorldDirectory = mcWorldDirectory;
    }



}

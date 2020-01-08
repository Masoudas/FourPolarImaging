package fr.fresnel.fourPolar.io;

import java.io.File;
import java.nio.file.Paths;

/**
 * This class generates the paths we need to store or read data.
 */
public class PathFactory {
    /**
     * Returns the path to the 0_Params folder.
     * @param rootFolder
     * @return
     */
    public static File getFolder_0_Params(File rootFolder) {
        return new File(rootFolder, Paths.get("4Polar", "0_Params").toString());
    }
}
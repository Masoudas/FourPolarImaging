package fr.fresnel.fourPolar.io;

import java.io.File;
import java.nio.file.Paths;

/**
 * This class generates the paths we need to store or read data.
 */
public class PathFactory {
    private static String root = "4Polar";  // Root of where we start to put files.
    private static String folder_params = "0_Params";   // Keeps the data about setup, files etc.

    /**
     * Returns the path to the {@folder_params} folder, and makes sure that the folder is created.
     * @param rootFolder
     * @return
     */
    public static File getFolder_4Polar(File rootFolder) {
        File fourPolar = new File(rootFolder, root);
        fourPolar.mkdir();
        return fourPolar;
    }


    /**
     * Returns the path to the {@folder_params} folder, and makes sure that the folder is created.
     * <p>
     * This folder contains all the basic parameters.
     * @param rootFolder
     * @return
     */
    public static File getFolder_0_Params(File rootFolder) {
        File zero_Params = new File(rootFolder, Paths.get(root, folder_params).toString());
        zero_Params.mkdirs();
        return zero_Params;
    }

}
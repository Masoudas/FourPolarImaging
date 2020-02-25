package fr.fresnel.fourPolar.io;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class generates the paths we need to store or read data of a project.
 * 
 */
public class PathFactoryOfProject {
    private static String root = "4Polar"; // Root of where we start to put files. This root is put where
                                           // the user provides the data.

    private static String folder_params = "0_Params"; // Keeps the data about setup, files etc.

    private static String folder_polarizationImages = "2_PolarizationImages"; // The folder that would hold the polarization images.

    /**
     * Returns the path to the {@folder_params} folder, and makes sure that the
     * folder is created.
     * 
     * @param rootFolder
     * @return
     */
    public static File getFolder_4Polar(File rootFolder) {
        Path path = Paths.get(rootFolder.getAbsolutePath(), root);
        File fourPolar = path.toFile();

        if (!fourPolar.exists()) {
            fourPolar.mkdir();
        }

        return fourPolar;
    }

    /**
     * Returns the path to the {@folder_params} folder, and makes sure that the
     * folder is created.
     * <p>
     * This folder contains all the basic parameters.
     * 
     * @param rootFolder
     * @return
     */
    public static File getFolder_Params(File rootFolder) {
        Path path = Paths.get(getFolder_4Polar(rootFolder).getAbsolutePath(), folder_params);
        File zero_Params = path.toFile();

        if (!zero_Params.exists()) {
            zero_Params.mkdirs();
        }

        return zero_Params;
    }

    public static File getFolder_() {
        
    }

}
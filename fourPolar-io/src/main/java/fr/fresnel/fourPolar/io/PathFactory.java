package fr.fresnel.fourPolar.io;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class generates the paths we need to store or read data.
 */
public class PathFactory {
    private static String root_result = "4Polar"; // Root of where we start to put files.
    private static File folder_softwarePrivateRoot = setFolder_softwarePrivateDataRoot(); // This is the private
                                                                                              // root on the home
                                                                                              // folder, where we keep
                                                                                              // setting information.
                                                                                              // The final folder is
                                                                                              // called 4PolarSoftware
                                                                                              // or .4PolarSoftware

    private static String folder_params = "0_Params"; // Keeps the data about setup, files etc.

    /**
     * This method is used for creating the private folder in the home folder, which
     * contains information such as database and settings.
     * 
     * @return
     */
    private static File setFolder_softwarePrivateDataRoot() {
        String homeFolder = System.getProperty("user.home");
        String folderName = "4PolarSoftware";

        // Create the hidden root folder based on the OS.
        File rootFolder;
        if (System.getProperty("os.name").contains("windows")) {
            Path rootPath = Paths.get(homeFolder, folderName);
            try {
                Files.setAttribute(rootPath, "dos:hidden", true);
            } catch (IOException e) {
                System.out.println("Unable to create software hidden folder.");
            }
            rootFolder = rootPath.toFile();
        } else {
            rootFolder = new File(homeFolder, "." + folderName);
        }

        if (!rootFolder.exists()) {
            rootFolder.mkdirs();
        }

        return rootFolder;
    }

    /**
     * Returns the path to the {@folder_params} folder, and makes sure that the
     * folder is created.
     * 
     * @param rootFolder
     * @return
     */
    public static File getFolder_4Polar(File rootFolder) {
        Path path = Paths.get(rootFolder.getAbsolutePath(), root_result);
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
    public static File getFolder_0_Params(File rootFolder) {
        Path path = Paths.get(getFolder_4Polar(rootFolder).getAbsolutePath(), folder_params);
        File zero_Params = path.toFile();

        if (!zero_Params.exists()) {
            zero_Params.mkdirs();
        }

        return zero_Params;
    }

    /**
     * Returns the path to the Data folder in the hidden home folder.
     */
    public static File getHiddenfolder_Data() {
        return new File(folder_softwarePrivateRoot, "Data");
    }

}